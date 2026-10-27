# cityinfojpのビルドとRelease運用

## バージョンとJavaの互換性

本体版は`pom.xml`のproject versionを正とします。現在の開発版は`1.2.0`で、対応するReleaseタグは`v1.2.0`です。

ビルド・アプリ実行にはJDK 17以上、ビルドにはMaven 3.6.3以上を使います。CIの検証環境はTemurin JDK 17です。本体は`maven.compiler.release=8`でコンパイルし、本体classをJava 8形式（major 52）に保ちます。Bootローダーと依存ライブラリを含むアプリ全体の実行にはJDK 17以上が必要です。

Spring Boot 4.0.9のBOMでSpring Framework 7.0.10、Tomcat 11.0.27、Thymeleaf 3.1.5.RELEASEを管理します。既存のBoot 3.5.16とSpring 7.0.9の組合せは、削除された`ThemeResolver`をBootのMVC自動構成が参照して起動に失敗したため、利用者の選択によりBoot 4系列へ移行しました。[公式移行ガイド](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide)と[Boot 4.0の実行要件](https://docs.spring.io/spring-boot/4.0/system-requirements.html)を参照しています。

MVCには`spring-boot-starter-webmvc`を使います。MVC用のJackson 3.1.7と、既存のJSON読込・生成コード用のJackson 2.21.7はBootのそれぞれのBOMで管理します。既存JSONコードのJackson 3 APIへの全面移行は行っていません。Commons IOは2.22.0、既存のLog4j 2.26.1／Logback 1.5.38の指定は維持しています。

## ローカルビルドとテスト

```sh
mvn --batch-mode --no-transfer-progress clean verify
```

9テストを実行し、`target/cityinfojp-<version>.jar`と`target/cityinfojp-<version>-sources.jar`を作ります。`CityinfojpApplicationTests`は実際のSpringコンテキストとTomcatをloopbackの一時ポートで起動し、テスト終了時に閉じます。

既存の静的HTML・RSS・sitemap・JSON生成テストは、`process-test-resources`でコピーした`target/test-work/src/main/resources/`を作業対象にします。Surefireの作業ディレクトリを`target/test-work/`に指定しており、ソース内の公開データや生成済みHTMLを書き換えません。

Bootの`repackage`が依存込み実行JARを作ります。`.jar.original`は再パッケージ前のJARなので、Releaseの実行JARとして使いません。sources JARにはJavaソース、設定、テンプレート、同梱データを含めます。GPG署名executionはないため、署名鍵やGPG skip指定は不要です。

## ローカル起動と配布物の検証

```sh
java -jar target/cityinfojp-1.2.0.jar --server.address=127.0.0.1 --server.port=8080
```

`http://127.0.0.1:8080/dyn/about.html`を開きます。終了時はCtrl+Cを使います。

```sh
python3 scripts/smoke-release.py target/cityinfojp-1.2.0.jar target/cityinfojp-1.2.0-sources.jar
```

このスクリプトはPython 3の標準ライブラリとJDKを使います。`JAVA_HOME`があればその`bin/java`、なければPATH上の`java`を使います。

- ManifestのBootローダーとStart-Class、`BOOT-INF/classes`、依存、テンプレート、設定、データ、sourcesの格納を確認します。
- 本体の全classがmajor 52であることと、2ファイルのSHA-256を確認します。
- ソースのない一時ディレクトリからJARを起動し、loopbackの空きポートをTomcatに割り当てます。
- About、Home、北海道、Data Entryの動的ページ4件と、都道府県・統合情報のJSON2件についてHTTP 200と内容を確認します。ブラウザによる外部CDN取得は行いません。
- 成否にかかわらず検証プロセスを終了します。本番サービスへの書込みやデプロイは行いません。

## CIとタグ起点のGitHub Release

[build.yml](../.github/workflows/build.yml)は`devel`へのpush、`devel`宛てPR、手動実行で動きます。JDK 17で`clean verify`を実行し、生成したJARのsmokeも実行します。

[release.yml](../.github/workflows/release.yml)は`v*`タグのpushでタグのソースを取得します。`v`以降とPOMのproject versionを完全一致で照合し、`clean verify`を実行します。次の2ファイルをステージし、smokeに成功した後で、そのタグのGitHub Releaseを作成または更新します。同名の添付ファイルは置き換えます。

- `cityinfojp-<version>.jar`：依存・テンプレート・データ込みのBoot実行JAR
- `cityinfojp-sources-<version>.jar`：Mavenのsources JARを配布名へコピーしたもの

公開後は2ファイルをGitHub Releaseからダウンロードし、ステージ済みファイルとのSHA-256一致と、ダウンロードしたJARの同じsmokeを確認します。公開後の検証で失敗した場合、Releaseは既に存在するので、Actionsログと実際の添付物を確認してください。後段の失敗を公開成功だけで完了扱いにしません。

通常CIは`contents: read`、Releaseは`contents: write`で動きます。Actionsの固定コミットとRelease構成は[igapyonv3](https://github.com/igapyon/igapyonv3/blob/bf9e9e509bdfee754975ded625ad320ce3cd837f/.github/workflows/release.yml)およびblancoCgの整備済み構成を基準にしています。

人間が差分を確認してcommit・push・PR mergeを行った後、workflowを含みPOM版が一致する対象コミットへタグを作成してpushします。

```sh
git tag v1.2.0 <確認済みのmerge-commit>
git push origin v1.2.0
```

Actionsの通常CIとReleaseの成功、タグ・POM・merge commitの一致、上記2添付JAR、ダウンロード後の検証成功を確認します。Dependabotは共有ブランチへ反映後の再評価を確認し、残るアラートは実効依存と照合します。

## 作業領域と既存ホスティング

`workplace/`は`.gitignore`でGit管理から除外しています。追跡済みファイルがないことは`git ls-files workplace`、ignore判定は`git check-ignore workplace/check.log`で確認します。検証ログや生成JARはGitへ登録しません。

Herokuで公開していたサイトは閉鎖済みです。README・テンプレート・RSS・sitemapに残る`https://cityinfojp.herokuapp.com/`などのURLは過去の参照先です。今回のGitHub ReleaseはJAR配布を行います。

## 整備時の検証（2026-10-27）

新しいMavenキャッシュから依存とプラグインを解決し、JDK 17で`clean verify`が成功しました。9テストには実コンテキストの起動・終了を含みます。実効依存ツリーでBoot 4.0.9、Spring 7.0.10、Tomcat 11.0.27、Jackson 2.21.7／3.1.7、Commons IO 2.22.0を確認しています。

生成物のない最終コピーで再ビルドし、配布名に揃えた2つのJARを検証しました。本体54classはすべてmajor 52で、ソース外の一時ディレクトリから動的ページ4件・同梱JSON2件のHTTP 200と内容を確認しました。再パッケージ前の`.jar.original`を検証へ渡すと拒否すること、2つのworkflowが`actionlint`を通ること、Mavenで取得したproject versionが`1.2.0`であることも確認しています。

ローカルのビルド・起動検証と、GitHub上の公開後検証の結果は別に記録します。整備時点では新workflowをリモートへ反映しておらず、GitHub側のCI・Release成功や公開JARの起動確認は未実施です。GitHubのCommons IOアラート2件もopenで、共有ブランチへ反映後の再評価が残っています。

既存テンプレートの省略形fragment式にはThymeleafの非推奨警告があり、既存データの生成テストには公式URL判定の診断メッセージが残ります。これらは今回のテスト失敗ではありません。
