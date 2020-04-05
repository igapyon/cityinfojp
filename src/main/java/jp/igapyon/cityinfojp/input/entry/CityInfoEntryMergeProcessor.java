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
package jp.igapyon.cityinfojp.input.entry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CityInfoEntryMergeProcessor {
    public static final void main(String[] args) throws IOException {
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
        FileUtils.write(new File("./src/main/resources/static/input/merged/merged-cityinfoentry-all.json"), result,
                "UTF-8");
    }
}
