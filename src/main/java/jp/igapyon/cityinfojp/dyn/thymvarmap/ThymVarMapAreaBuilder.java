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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jp.igapyon.cityinfojp.dyn.DisplayPrefEntry;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntry;
import jp.igapyon.cityinfojp.input.entry.PrefEntry;
import jp.igapyon.cityinfojp.input.entry.PrefEntryUtil;

/**
 * Thymeleaf の Var map をビルドします。
 * 
 * エリアの単位でビルドします。
 */
public class ThymVarMapAreaBuilder extends AbstractThymVarMapBuilder {
    public static final String[][] AREA_INFO = new String[][] { { "tohoku", "東北" }, { "kanto", "関東" },
            { "chubu", "中部" }, { "kinki", "近畿" }, { "chugoku", "中国" }, { "shikoku", "四国" },
            { "kyushuokinawa", "九州沖縄" } };
    public static final String[][] AREA_PREF_CODES = new String[][] { { "02", "03", "04", "05", "06", "07" },
            { "08", "09", "10", "11", "12", "13", "14" }, { "15", "16", "17", "18", "19", "20", "21", "22", "23" },
            { "24", "25", "26", "27", "28", "29", "30" }, { "31", "32", "33", "34", "35" }, { "36", "37", "38", "39" },
            { "40", "41", "42", "43", "44", "45", "46", "47" } };

    private String areaId;
    private String areaName;
    private String[] prefs;

    public ThymVarMapAreaBuilder(String areaId, String areaName, String[] prefs) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.prefs = prefs;
    }

    /**
     * 変数をビルド。
     */
    protected LinkedHashMap<String, Object> buildVarMap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        List<DisplayPrefEntry> prefList = new ArrayList<>();
        try {
            List<PrefEntry> prefAllList = PrefEntryUtil.readEntryListFromClasspath();
            for (PrefEntry prefEntry : prefAllList) {
                for (String lookPrefCode : prefs) {
                    if (lookPrefCode.equals(prefEntry.getCode())) {
                        DisplayPrefEntry dispPref = new DisplayPrefEntry();
                        prefList.add(dispPref);
                        dispPref.setText(prefEntry.getName());
                        dispPref.setUrl("/pref/" + prefEntry.getNameen().toLowerCase() + ".html");

                        List<CityInfoEntry> allEntryList = ThymVarMapIndexBuilder.buildEntityList();
                        ThymVarMapIndexBuilder.sortEntryList(allEntryList);

                        // pref で絞り込み
                        for (CityInfoEntry lookup : allEntryList) {
                            if (prefEntry.getName().equals(lookup.getPref())) {
                                dispPref.setInfoCount(dispPref.getInfoCount() + 1);
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Unexpected exception: " + ex.toString());
            ex.printStackTrace();
        }

        result.put("prefList", prefList);

        // "dispEntryList"は不要

        result.put("jumbotron", ThymVarMapPrefBuilder.getJumbotronBean(areaName));

        result.put("navbar", ThymVarMapPrefBuilder.getNavbarBean(areaId));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        result.put("processDateTime", dtf.format(LocalDateTime.now()));

        return result;
    }
}
