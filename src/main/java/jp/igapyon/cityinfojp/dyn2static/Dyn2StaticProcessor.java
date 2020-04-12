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
                List<CityInfoDisplayEntry> dispEntryList = DynIndexController.entryList2DispEntryList(allEntryList);

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

    }
}
