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
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PrefEntryUtil {
    public static List<PrefEntry> readEntryList(File jsonInputFile) throws IOException {
        String jsonInput = FileUtils.readFileToString(jsonInputFile, "UTF-8");
        return readEntryList(jsonInput);
    }

    public static List<PrefEntry> readEntryList(String jsonInput) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.asList(mapper.readValue(jsonInput, PrefEntry[].class));
    }
}
