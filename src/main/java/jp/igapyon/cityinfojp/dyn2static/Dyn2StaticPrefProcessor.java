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

import jp.igapyon.cityinfojp.dyn.thymvarmap.ThymVarMapAreaBuilder;
import jp.igapyon.cityinfojp.dyn.thymvarmap.ThymVarMapPrefBuilder;
import jp.igapyon.cityinfojp.input.entry.PrefEntry;
import jp.igapyon.cityinfojp.input.entry.PrefEntryUtil;

public class Dyn2StaticPrefProcessor {
    public static final String[][] AREA_INFO = new String[][] { { "tohoku", "東北" }, { "kanto", "関東" },
            { "chubu", "中部" }, { "kinki", "近畿" }, { "chugoku", "中国" }, { "shikoku", "四国" },
            { "kyushuokinawa", "九州沖縄" } };
    public static final String[][] AREA_PREF_CODES = new String[][] { { "02", "03", "04", "05", "06", "07" },
            { "08", "09", "10", "11", "12", "13", "14" }, { "15", "16", "17", "18", "19", "20", "21", "22", "23" },
            { "24", "25", "26", "27", "28", "29", "30" }, { "31", "32", "33", "34", "35" }, { "36", "37", "38", "39" },
            { "40", "41", "42", "43", "44", "45", "46", "47" } };

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
                for (Map.Entry<String, Object> look : map.entrySet()) {
                    ((Context) ctx).setVariable(look.getKey(), look.getValue());
                }

                String result = templateEngine.process("/dyn/pref/pref", ctx);
                FileUtils.writeStringToFile(
                        new File("src/main/resources/static/pref/" + pref.getNameen().toLowerCase() + ".html"), result,
                        "UTF-8");
            }
        } catch (IOException ex) {
            System.err.println("Unexpected exception: " + ex.toString());
            ex.printStackTrace();
        }

        // areaごと静的ページ

        try {
            for (int index = 0; index < AREA_INFO.length; index++) {
                final IContext ctx = new Context();

                String[] area = AREA_INFO[index];

                LinkedHashMap<String, Object> map = ThymVarMapAreaBuilder.buildVarMap(area[0], area[1],
                        AREA_PREF_CODES[index]);

                for (Map.Entry<String, Object> look : map.entrySet()) {
                    ((Context) ctx).setVariable(look.getKey(), look.getValue());
                }

                String result = templateEngine.process("/dyn/pref/area", ctx);
                FileUtils.writeStringToFile(
                        new File("src/main/resources/static/pref/" + area[0].toLowerCase() + ".html"), result, "UTF-8");
            }
        } catch (IOException ex) {
            System.err.println("Unexpected exception: " + ex.toString());
            ex.printStackTrace();
        }
    }
}
