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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import jp.igapyon.cityinfojp.dyn.thymvarmap.ThymVarMapPrefBuilder;
import jp.igapyon.cityinfojp.dyn.thymvarmap.ThymVarMapUtil;
import jp.igapyon.cityinfojp.input.entry.PrefEntry;
import jp.igapyon.cityinfojp.input.entry.PrefEntryUtil;

public class Dyn2StaticPrefProcessor {

    public static final void main(String[] args) throws IOException {
        SpringTemplateEngine templateEngine = Dyn2StaticProcessorUtil.getStandaloneSpringTemplateEngine();

        dyn2staticPrefList(templateEngine);
    }

    static void dyn2staticPrefList(SpringTemplateEngine templateEngine) throws IOException {
        System.err.println("都道府県 html を静的ファイル化.");

        // pref ごとに処理

        try {
            List<PrefEntry> prefList = PrefEntryUtil.readEntryListFromClasspath();
            for (PrefEntry pref : prefList) {
                final IContext ctx = new Context();

                LinkedHashMap<String, Object> map = ThymVarMapPrefBuilder.buildVarMap(pref.getNameen(), pref.getName());
                ThymVarMapUtil.setContextVariable(ctx, map);

                String result = templateEngine.process("/dyn/pref/pref", ctx);
                FileUtils.writeStringToFile(
                        new File("src/main/resources/static/pref/" + pref.getNameen().toLowerCase() + ".html"), result,
                        "UTF-8");
            }
        } catch (IOException ex) {
            System.err.println("Unexpected exception: " + ex.toString());
            ex.printStackTrace();
        }
    }
}
