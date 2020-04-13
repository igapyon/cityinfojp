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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jp.igapyon.cityinfojp.dyn.CityInfoDisplayEntry;
import jp.igapyon.cityinfojp.dyn.DynIndexController;
import jp.igapyon.cityinfojp.dyn.DynPrefController;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntry;

/**
 * Thymeleaf の Var map をビルドします。
 * 
 * 都道府県の単位でビルドします。
 */
public class ThymVarMapPrefBuilder {
    /**
     * final String name, final Object value
     */
    public static LinkedHashMap<String, Object> buildVarMap(String prefNameen, String prefName) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        try {
            List<CityInfoEntry> allEntryList = DynIndexController.buildEntityList();
            DynIndexController.sortEntryList(allEntryList);

            // pref で絞り込み
            List<CityInfoEntry> entryList = new ArrayList<>();
            for (CityInfoEntry lookup : allEntryList) {
                if (prefName.equals(lookup.getPref())) {
                    entryList.add(lookup);
                }
            }

            // 絞り込み後のデータを利用
            List<CityInfoDisplayEntry> dispEntryList = DynIndexController.entryList2DispEntryList(entryList);

            // Prefでしぼりこみ
            result.put("dispEntryList", dispEntryList);

            result.put("jumbotron", DynPrefController.getJumbotronBean(prefName));

            result.put("navbar", DynPrefController.getNavbarBean(prefNameen));

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            result.put("processDateTime", dtf.format(LocalDateTime.now()));
        } catch (IOException ex) {
            System.err.println("Unexpected exception: " + ex.toString());
            ex.printStackTrace();
        }

        return result;
    }
}
