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

import jp.igapyon.cityinfojp.dyn.DynPrefController;
import jp.igapyon.cityinfojp.input.entry.PrefEntry;
import jp.igapyon.cityinfojp.input.entry.PrefEntryUtil;

/**
 * Thymeleaf の Var map をビルドします。
 * 
 * エリアの単位でビルドします。
 */
public class ThymVarMapAreaBuilder extends AbstractThymVarMapBuilder {
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

        List<PrefEntry> prefList = new ArrayList<>();
        try {
            List<PrefEntry> prefAllList = PrefEntryUtil.readEntryListFromClasspath();
            for (PrefEntry prefEntry : prefAllList) {
                for (String lookPrefCode : prefs) {
                    if (lookPrefCode.equals(prefEntry.getCode())) {
                        prefList.add(prefEntry);
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Unexpected exception: " + ex.toString());
            ex.printStackTrace();
        }

        result.put("prefList", prefList);

        // "dispEntryList"は不要

        result.put("jumbotron", DynPrefController.getJumbotronBean(areaName));

        result.put("navbar", DynPrefController.getNavbarBean(areaId));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        result.put("processDateTime", dtf.format(LocalDateTime.now()));

        return result;
    }
}