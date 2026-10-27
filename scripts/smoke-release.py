#!/usr/bin/env python3
"""Check the exact release JARs and run the application outside its source tree."""

import argparse
import hashlib
import json
import os
from pathlib import Path
import re
import shutil
import subprocess
import tempfile
import time
import urllib.request
import zipfile


def require(condition, message):
    if not condition:
        raise RuntimeError(message)


def inspect_jars(application, sources):
    with zipfile.ZipFile(application) as jar:
        entries = set(jar.namelist())
        manifest = jar.read("META-INF/MANIFEST.MF").decode("utf-8")
        manifest = manifest.replace("\r\n", "\n").replace("\n ", "")
        require("Main-Class: org.springframework.boot.loader.launch.JarLauncher\n" in manifest,
                "The application must be a repackaged Spring Boot JAR")
        require("Start-Class: jp.igapyon.cityinfojp.CityinfojpApplication\n" in manifest,
                "Unexpected application entry point")
        for entry in ("BOOT-INF/classes/application.properties",
                      "BOOT-INF/classes/templates/dyn/about.html",
                      "BOOT-INF/classes/templates/dyn/fragment/navbar.html",
                      "BOOT-INF/classes/static/input/prefjp.json",
                      "BOOT-INF/classes/static/input/merged/merged-cityinfoentry-all.json"):
            require(entry in entries, "Missing application resource: " + entry)
        require(any(name.startswith("BOOT-INF/lib/commons-io-2.22.0.jar") for name in entries),
                "The updated Commons IO dependency is missing")
        classes = [name for name in entries
                   if name.startswith("BOOT-INF/classes/") and name.endswith(".class")]
        require(classes, "No application classes found")
        for name in classes:
            header = jar.read(name)[:8]
            require(header[:4] == b"\xca\xfe\xba\xbe" and int.from_bytes(header[6:8], "big") == 52,
                    "Application class is not Java 8 format: " + name)
        print("Application JAR: %d classes, all major 52; Boot layout and resources OK" % len(classes))
    with zipfile.ZipFile(sources) as jar:
        entries = set(jar.namelist())
        for entry in ("jp/igapyon/cityinfojp/CityinfojpApplication.java",
                      "application.properties", "templates/dyn/about.html", "static/input/prefjp.json"):
            require(entry in entries, "Missing sources JAR entry: " + entry)
        print("Sources JAR: Java sources, configuration, templates and bundled data OK")
    for path in (application, sources):
        digest = hashlib.sha256()
        with path.open("rb") as stream:
            for chunk in iter(lambda: stream.read(1024 * 1024), b""):
                digest.update(chunk)
        print("SHA-256 %s  %s" % (digest.hexdigest(), path.name))


def smoke_application(application, timeout):
    java = str(Path(os.environ["JAVA_HOME"]) / "bin/java") if os.environ.get("JAVA_HOME") else shutil.which("java")
    require(java, "Set JAVA_HOME or put Java 17 on PATH")
    # Ignore host proxy settings: every request must stay on the loopback interface.
    opener = urllib.request.build_opener(urllib.request.ProxyHandler({}))
    with tempfile.TemporaryDirectory(prefix="cityinfojp-smoke-") as work:
        log_path = Path(work) / "application.log"
        with log_path.open("w", encoding="utf-8") as log:
            process = subprocess.Popen([java, "-jar", str(application),
                                        "--server.address=127.0.0.1", "--server.port=0"],
                                       cwd=work, stdout=log, stderr=subprocess.STDOUT)
            try:
                deadline = time.monotonic() + timeout
                port = None
                while time.monotonic() < deadline:
                    output = log_path.read_text(encoding="utf-8", errors="replace")
                    match = re.search(r"Tomcat started on port (\d+)", output)
                    if match:
                        port = int(match.group(1))
                        break
                    require(process.poll() is None, "Application exited before starting")
                    time.sleep(0.25)
                require(port is not None, "Application startup timed out")
                base = "http://127.0.0.1:%d" % port
                for path, marker in (("/dyn/about.html", "About cityinfojp"),
                                     ("/dyn/index.html", "cityinfojp"),
                                     ("/dyn/pref/hokkaido.html", "北海道"),
                                     ("/dataentry.html", "Data Entry")):
                    with opener.open(base + path, timeout=15) as response:
                        body = response.read().decode("utf-8")
                        require(response.status == 200 and marker in body,
                                "Unexpected HTTP response: " + path)
                        require("th:text=" not in body and "th:insert=" not in body,
                                "Template was not rendered: " + path)
                    print("HTTP 200 and rendered template: " + path)
                for path, marker in (("/input/prefjp.json", "北海道"),
                                     ("/input/merged/merged-cityinfoentry-all.json", "EntryDate")):
                    with opener.open(base + path, timeout=15) as response:
                        body = response.read().decode("utf-8")
                        require(response.status == 200 and marker in body and json.loads(body),
                                "Bundled JSON is missing or empty: " + path)
                    print("HTTP 200 and bundled JSON: " + path)
            except BaseException:
                print(log_path.read_text(encoding="utf-8", errors="replace"))
                raise
            finally:
                if process.poll() is None:
                    process.terminate()
                    try:
                        process.wait(timeout=15)
                    except subprocess.TimeoutExpired:
                        process.kill()
                        process.wait(timeout=15)
        print("Application process stopped")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("application", type=Path)
    parser.add_argument("sources", type=Path)
    parser.add_argument("--timeout", type=float, default=60)
    args = parser.parse_args()
    application = args.application.resolve(strict=True)
    sources = args.sources.resolve(strict=True)
    inspect_jars(application, sources)
    smoke_application(application, args.timeout)
    print("Release JAR smoke passed")


if __name__ == "__main__":
    main()
