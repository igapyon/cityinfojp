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
import jp.igapyon.cityinfojp.input.entry.CityInfoEntry;

public class Dyn2StaticIndexProcessor {
    public static final void main(String[] args) throws IOException {
        SpringTemplateEngine templateEngine = Dyn2StaticUtil.getStandaloneSpringTemplateEngine();

        dyn2staticIndex(templateEngine);
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
}
