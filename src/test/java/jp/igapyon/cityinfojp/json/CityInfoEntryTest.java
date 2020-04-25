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
package jp.igapyon.cityinfojp.json;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.igapyon.cityinfojp.json.JsonCityInfoEntry;
import jp.igapyon.cityinfojp.json.JsonCityInfoEntryUtil;

class CityInfoEntryTest {
    @Test
    void contextLoads() throws Exception {
        {
            List<JsonCityInfoEntry> entryList = JsonCityInfoEntryUtil.readEntryList(
                    new File("./src/main/resources/static/input/2020/202004/11-saitama-request-20200405a.json"));
            for (JsonCityInfoEntry entry : entryList) {
                // System.err.println(entry.getState());
                for (String look : entry.getURL()) {
                    // System.err.println("  " + look);
                }
            }
        }
        {
            List<JsonCityInfoEntry> entryList = JsonCityInfoEntryUtil.readEntryList(
                    new File("./src/main/resources/static/input/2020/202004/12-chiba-closure-20200405a.json"));
            for (JsonCityInfoEntry entry : entryList) {
                // System.err.println(entry.getState());
                for (String look : entry.getURL()) {
                    // System.err.println("  " + look);
                }
            }
        }
    }
}
