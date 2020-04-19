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
package jp.igapyon.cityinfojp.th2static;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import jp.igapyon.cityinfojp.thvarmap.ThVarMapIndexBuilder;

public class Th2StaticIndexProcessor {
    public static final void main(String[] args) throws IOException {
        SpringTemplateEngine templateEngine = Th2StaticUtil.getStandaloneSpringTemplateEngine();

        dyn2staticIndex(templateEngine);
    }

    static void dyn2staticIndex(SpringTemplateEngine templateEngine) throws IOException {
        System.err.println("index.html を静的ファイル化.");

        final IContext ctx = new Context();

        new ThVarMapIndexBuilder().applyContextVariable(ctx);

        String result = templateEngine.process("/dyn/index", ctx);
        FileUtils.writeStringToFile(new File("src/main/resources/static/index.html"), result, "UTF-8");
    }
}
