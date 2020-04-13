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

import java.util.LinkedHashMap;
import java.util.Map;

import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

public class ThymVarMapUtil {

    /**
     * 与えられた map 情報を Context に設定します。
     * 
     * @param ctx Target Context. must be instance of Context.
     * @param map Map of value.
     */
    public static void setContextVariable(IContext ctx, LinkedHashMap<String, Object> map) {
        for (Map.Entry<String, Object> look : map.entrySet()) {
            ((Context) ctx).setVariable(look.getKey(), look.getValue());
        }
    }
}
