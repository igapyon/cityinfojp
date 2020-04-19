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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import jp.igapyon.cityinfojp.thvarmap.ThVarMapAreaBuilder;

public class Th2StaticAreaProcessor {

    public static final void main(String[] args) throws IOException {
        SpringTemplateEngine templateEngine = Th2StaticUtil.getStandaloneSpringTemplateEngine();

        dyn2staticAreaList(templateEngine);
    }

    static void dyn2staticAreaList(SpringTemplateEngine templateEngine) throws IOException {
        // areaごと静的ページ

        try {
            for (int index = 0; index < ThVarMapAreaBuilder.AREA_INFO.length; index++) {
                final IContext ctx = new Context();

                String[] area = ThVarMapAreaBuilder.AREA_INFO[index];

                new ThVarMapAreaBuilder(area[0], area[1], ThVarMapAreaBuilder.AREA_PREF_CODES[index])
                        .applyContextVariable(ctx);

                String result = templateEngine.process("/dyn/pref/area", ctx);
                FileUtils.writeStringToFile(
                        new File("src/main/resources/static/pref/" + area[0].toLowerCase() + ".html"), result, "UTF-8");
            }
        } catch (IOException ex) {
            System.err.println("Unexpected exception: " + ex.toString());
            ex.printStackTrace();
        }
    }
}
