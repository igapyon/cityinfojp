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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import jp.igapyon.cityinfojp.dyn.CityInfoDisplayEntry;
import jp.igapyon.cityinfojp.dyn.DynIndexController;
import jp.igapyon.cityinfojp.dyn.DynPrefController;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntry;
import jp.igapyon.cityinfojp.input.entry.PrefEntry;
import jp.igapyon.cityinfojp.input.entry.PrefEntryUtil;

public class Dyn2StaticProcessor {
    public static final void main(String[] args) throws IOException {
        SpringTemplateEngine templateEngine = Dyn2StaticProcessorUtil.getStandaloneSpringTemplateEngine();

        dyn2staticIndex(templateEngine);

        dyn2staticPrefList(templateEngine);
    }

    static void dyn2staticIndex(SpringTemplateEngine templateEngine) throws IOException {
        System.err.println("index.html を静的ファイル化.");

        final IContext ctx = new Context();

        List<CityInfoEntry> allEntryList = DynIndexController.buildEntityList();
        DynIndexController.sortEntryList(allEntryList);
        List<CityInfoDisplayEntry> dispEntryList = DynIndexController.entryList2DispEntryList(allEntryList);

        ((Context) ctx).setVariable("dispEntryList", dispEntryList);

        ((Context) ctx).setVariable("jumbotron", DynIndexController.getJumbotronBean());

        ((Context) ctx).setVariable("navbar", DynIndexController.getNavbarBean());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ((Context) ctx).setVariable("processDateTime", dtf.format(LocalDateTime.now()));

        String result = templateEngine.process("/dyn/index", ctx);
        FileUtils.writeStringToFile(new File("src/main/resources/static/index.html"), result, "UTF-8");
    }

    static void dyn2staticPrefList(SpringTemplateEngine templateEngine) throws IOException {
        System.err.println("都道府県 html を静的ファイル化.");

        // pref ごとに処理

        try {
            List<PrefEntry> prefList = PrefEntryUtil.readEntryListFromClasspath();
            for (PrefEntry pref : prefList) {
                final IContext ctx = new Context();

                List<CityInfoEntry> allEntryList = DynIndexController.buildEntityList();
                DynIndexController.sortEntryList(allEntryList);

                // pref で絞り込み
                List<CityInfoEntry> entryList = new ArrayList<>();
                for (CityInfoEntry lookup : allEntryList) {
                    if (pref.getName().equals(lookup.getPref())) {
                        entryList.add(lookup);
                    }
                }

                // 絞り込み後のデータを利用
                List<CityInfoDisplayEntry> dispEntryList = DynIndexController.entryList2DispEntryList(entryList);

                // Prefでしぼりこみ
                ((Context) ctx).setVariable("dispEntryList", dispEntryList);

                ((Context) ctx).setVariable("jumbotron", DynPrefController.getJumbotronBean(pref.getName()));

                ((Context) ctx).setVariable("navbar", DynPrefController.getNavbarBean(pref.getNameen()));

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                ((Context) ctx).setVariable("processDateTime", dtf.format(LocalDateTime.now()));

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

        String[][] AREA_INFO = new String[][] { { "tohoku", "東北" }, { "kanto", "関東" }, { "chubu", "中部" },
                { "kinki", "近畿" }, { "chugoku", "中国" }, { "shikoku", "四国" }, { "kyushuokinawa", "九州沖縄" } };
        String[][] AREA_PREF_CODES = new String[][] { { "02", "03", "04", "05", "06", "07" },
                { "08", "09", "10", "11", "12", "13", "14" }, { "15", "16", "17", "18", "19", "20", "21", "22", "23" },
                { "24", "25", "26", "27", "28", "29", "30" }, { "31", "32", "33", "34", "35" },
                { "36", "37", "38", "39" }, { "40", "41", "42", "43", "44", "45", "46", "47" } };

        try {
            for (int index = 0; index < AREA_INFO.length; index++) {
                String[] area = AREA_INFO[index];

                List<PrefEntry> prefList = new ArrayList<>();
                try {
                    String[] localareacodes = AREA_PREF_CODES[index];
                    List<PrefEntry> prefAllList = PrefEntryUtil.readEntryListFromClasspath();
                    for (PrefEntry prefEntry : prefAllList) {
                        for (String lookPrefCode : localareacodes) {
                            if (lookPrefCode.equals(prefEntry.getCode())) {
                                prefList.add(prefEntry);
                            }
                        }
                    }
                } catch (IOException ex) {
                    System.err.println("Unexpected exception: " + ex.toString());
                    ex.printStackTrace();
                }

                final IContext ctx = new Context();

                ((Context) ctx).setVariable("prefList", prefList);

                // "dispEntryList"は不要

                ((Context) ctx).setVariable("jumbotron", DynPrefController.getJumbotronBean(area[1]));

                ((Context) ctx).setVariable("navbar", DynPrefController.getNavbarBean(area[0]));

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                ((Context) ctx).setVariable("processDateTime", dtf.format(LocalDateTime.now()));

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
