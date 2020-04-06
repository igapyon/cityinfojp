/*
 * Copyright 2020 Toshiki Iga
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.igapyon.cityinfojp.dyn2static;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

public class Dyn2StaticSimpleProcessor {
    public static final void main(String[] args) throws IOException {
        SpringTemplateEngine templateEngine = Dyn2StaticProcessorUtil.getStandaloneSpringTemplateEngine();

        dyn2staticSimple(templateEngine, "dyn/about", "src/main/resources/static/about.html");
        dyn2staticSimple(templateEngine, "dyn/link", "src/main/resources/static/link.html");
    }

    static void dyn2staticSimple(SpringTemplateEngine templateEngine, String sourcePath, String targetPath)
            throws IOException {
        System.err.println(sourcePath + " を静的ファイル化.");

        final IContext ctx = new Context();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ((Context) ctx).setVariable("processDateTime", dtf.format(LocalDateTime.now()));

        String result = templateEngine.process(sourcePath, ctx);
        FileUtils.writeStringToFile(new File(targetPath), result, "UTF-8");
    }
}