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
package jp.igapyon.cityinfojp.th2static;

import org.junit.jupiter.api.Test;

import jp.igapyon.cityinfojp.json.JsonCityInfoEntryMergeProcessor;
import jp.igapyon.cityinfojp.th2static.Th2StaticAreaProcessor;
import jp.igapyon.cityinfojp.th2static.Th2StaticIndexProcessor;
import jp.igapyon.cityinfojp.th2static.Th2StaticPrefProcessor;
import jp.igapyon.cityinfojp.th2static.Th2StaticSimpleProcessor;

class Dyn2StaticHtmlTest {

    @Test
    void dyn2static() throws Exception {
        // JSONを一括マージが必ず先。
        JsonCityInfoEntryMergeProcessor.main(new String[] { "this" });

        // その次に個別の生成

        Th2StaticIndexProcessor.main(new String[] { "this" });

        Th2StaticAreaProcessor.main(new String[] { "this" });

        Th2StaticPrefProcessor.main(new String[] { "this" });

        Th2StaticSimpleProcessor.main(new String[] { "this" });
    }
}
