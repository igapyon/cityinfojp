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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import jp.igapyon.cityinfojp.dyn.DynSimpleController;

/**
 * Thymeleaf の Var map をビルドします。
 * 
 * ベーシックな変数をビルドします。
 */
public class ThymVarMapSimpleBuilder {
    /**
     * 変数をビルド。
     */
    public static LinkedHashMap<String, Object> buildVarMap(String sourcePath) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        result.put("jumbotron", DynSimpleController.getJumbotronBean(sourcePath));

        result.put("navbar", DynSimpleController.getNavbarBean(sourcePath));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        result.put("processDateTime", dtf.format(LocalDateTime.now()));

        return result;
    }
}
