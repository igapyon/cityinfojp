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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JSON の 都市情報 の JSON を1つにまとめあげるためのマージ処理。
 * 
 * @author Tosihki Iga
 */
public class JsonCityInfoEntryMergeProcessor {
    public static final void main(String[] args) throws IOException {
        Collection<File> filesCollection = FileUtils.listFiles( //
                new File("./src/main/resources/static/input/2020/"), new String[] { "json" }, true);
        List<File> fileList = new ArrayList<>();
        fileList.addAll(filesCollection);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File left, File right) {
                return left.getAbsolutePath().compareTo(right.getAbsolutePath());
            }
        });

        List<JsonCityInfoEntry> mergedEntryList = new ArrayList<JsonCityInfoEntry>();
        for (Iterator<File> ite = fileList.iterator(); ite.hasNext();) {
            File look = ite.next();

            System.err.println("merging:" + look.getAbsolutePath());
            {
                List<JsonCityInfoEntry> entryList = JsonCityInfoEntryUtil.readEntryList(look);
                mergedEntryList.addAll(entryList);

                // Verify
                for (JsonCityInfoEntry cityInfo : entryList) {
                    for (String checkUrl : cityInfo.getURL()) {
                        boolean isUrlValid = false;
                        List<JsonPrefUrlEntry> prefUrlList = JsonPrefUrlEntryUtil.readEntryListFromClasspath();
                        for (JsonPrefUrlEntry prefUrl : prefUrlList) {
                            for (String lookUrl : prefUrl.getUrl()) {
                                if (checkUrl.startsWith(lookUrl)) {
                                    isUrlValid = true;
                                }
                            }
                        }
                        if (isUrlValid == false) {
                            System.err.println("  Non org based URL: " + checkUrl);
                        }
                    }
                }
            }

        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String result = mapper.writeValueAsString(mergedEntryList);
        FileUtils.write(new File("./src/main/resources/static/input/merged/merged-cityinfoentry-all.json"), result,
                "UTF-8");
    }
}
