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
package jp.igapyon.cityinfojp.thvarmap;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.igapyon.cityinfojp.disp.DisplayCityInfoEntry;
import jp.igapyon.cityinfojp.disp.DisplayPrefEntry;
import jp.igapyon.cityinfojp.json.JsonAreaEntry;
import jp.igapyon.cityinfojp.json.JsonCityInfoEntry;
import jp.igapyon.cityinfojp.json.JsonPrefEntry;
import jp.igapyon.cityinfojp.json.JsonPrefEntryUtil;

/**
 * Thymeleaf の Var map をビルドします。
 * 
 * エリアの単位でビルドします。
 */
public class ThVarMapAreaBuilder extends AbstractThVarMapBuilder {
    private String areaId;
    private String areaName;
    private List<String> prefs;

    public ThVarMapAreaBuilder(JsonAreaEntry areaEntry) {
        this.areaId = areaEntry.getNameen();
        this.areaName = areaEntry.getName();
        this.prefs = areaEntry.getPref();
    }

    /**
     * Thymeleaf のオンライン処理・バッチ処理の両方で利用可能なキー値マップを構成。
     * 
     * @return 抽象化されたキー値マップ。
     */
    @Override
    protected LinkedHashMap<String, Object> buildVarMap() throws IOException {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        List<DisplayPrefEntry> prefList = new ArrayList<>();
        {
            List<JsonPrefEntry> prefAllList = JsonPrefEntryUtil.readEntryListFromClasspath();
            for (JsonPrefEntry prefEntry : prefAllList) {
                for (String lookPrefCode : prefs) {
                    if (lookPrefCode.equals(prefEntry.getCode())) {
                        DisplayPrefEntry dispPref = new DisplayPrefEntry();
                        prefList.add(dispPref);
                        dispPref.setText(prefEntry.getName());
                        dispPref.setUrl("/pref/" + prefEntry.getNameen().toLowerCase() + ".html");

                        List<JsonCityInfoEntry> allEntryList = ThVarMapIndexBuilder.buildEntityList();
                        ThVarMapIndexBuilder.sortEntryList(allEntryList);

                        // cityinfo の数を pref でカウントアップ
                        for (JsonCityInfoEntry lookup : allEntryList) {
                            if (prefEntry.getName().equals(lookup.getPref())) {
                                dispPref.setInfoCount(dispPref.getInfoCount() + 1);
                            }
                        }
                    }
                }
            }
        }

        result.put("prefList", prefList);

        // "dispEntryList"
        List<JsonCityInfoEntry> allEntryList = ThVarMapIndexBuilder.buildEntityList();
        ThVarMapIndexBuilder.sortEntryList(allEntryList);

        // pref で絞り込み
        List<JsonCityInfoEntry> entryList = new ArrayList<>();
        Map<String, String> mapTargetPref = new HashMap<>();
        List<JsonPrefEntry> prefAllList = JsonPrefEntryUtil.readEntryListFromClasspath();
        for (JsonPrefEntry prefEntry : prefAllList) {
            for (String prefCode : prefs) {
                if (prefCode.equals(prefEntry.getCode())) {
                    mapTargetPref.put(prefEntry.getName(), prefEntry.getNameen());
                }
            }
        }
        for (JsonCityInfoEntry look : allEntryList) {
            if (mapTargetPref.get(look.getPref()) != null) {
                entryList.add(look);
            }
        }

        // 絞り込み後のデータを利用
        List<DisplayCityInfoEntry> dispEntryList = ThVarMapIndexBuilder.entryList2DispEntryList(entryList);

        result.put("dispEntryList", dispEntryList);

        result.put("jumbotron", ThVarMapPrefBuilder.getJumbotronBean(areaName));

        result.put("navbar", ThVarMapPrefBuilder.getNavbarBean(areaId));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        result.put("processDateTime", dtf.format(LocalDateTime.now()));

        return result;
    }
}
