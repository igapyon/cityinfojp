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

package jp.igapyon.cityinfojp.input;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.igapyon.cityinfojp.input.entry.CityInfoEntry;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntryUtil;

class MergeCityInfoEntryTest {
    @Test
    void contextLoads() throws Exception {
        List<CityInfoEntry> mergedEntryList = new ArrayList<CityInfoEntry>();
        {
            List<CityInfoEntry> entryList = CityInfoEntryUtil.readEntryList(
                    new File("./src/main/resources/static/input/2020/202004/saitama-stayathome-20200405a.json"));
            mergedEntryList.addAll(entryList);
        }
        {
            List<CityInfoEntry> entryList = CityInfoEntryUtil.readEntryList(
                    new File("./src/main/resources/static/input/2020/202004/chiba-school-20200405a.json"));
            mergedEntryList.addAll(entryList);
        }

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(mergedEntryList);
        System.err.println(result);
    }
}
