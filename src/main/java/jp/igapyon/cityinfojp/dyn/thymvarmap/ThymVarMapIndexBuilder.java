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
package jp.igapyon.cityinfojp.dyn.thymvarmap;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;

import jp.igapyon.cityinfojp.dyn.CityInfoDisplayEntry;
import jp.igapyon.cityinfojp.dyn.DynIndexController;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntry;

/**
 * Thymeleaf の Var map をビルドします。
 * 
 * Indexの単位でビルドします。
 */
public class ThymVarMapIndexBuilder {
    /**
     * 変数をビルド。
     */
    public static LinkedHashMap<String, Object> buildVarMap() throws IOException {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        List<CityInfoEntry> allEntryList = DynIndexController.buildEntityList();
        DynIndexController.sortEntryList(allEntryList);
        List<CityInfoDisplayEntry> dispEntryList = DynIndexController.entryList2DispEntryList(allEntryList);

        result.put("dispEntryList", dispEntryList);

        result.put("jumbotron", DynIndexController.getJumbotronBean());

        result.put("navbar", DynIndexController.getNavbarBean());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        result.put("processDateTime", dtf.format(LocalDateTime.now()));

        return result;
    }
}
