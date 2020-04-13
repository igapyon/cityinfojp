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
package jp.igapyon.cityinfojp.dyn;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jp.igapyon.cityinfojp.dyn.thymvarmap.ThymVarMapPrefBuilder;
import jp.igapyon.cityinfojp.input.entry.PrefEntry;
import jp.igapyon.cityinfojp.input.entry.PrefEntryUtil;

@Controller
public class DynPrefController {
    @GetMapping({ "/dyn/pref/{pref}" })
    public String index(Model model, @PathVariable("pref") String pref) throws IOException {
        if (pref.endsWith(".html")) {
            pref = pref.substring(0, pref.length() - ".html".length());
        }

        String prefName = "";
        try {
            List<PrefEntry> prefList = PrefEntryUtil.readEntryListFromClasspath();
            for (PrefEntry prefEntry : prefList) {
                if (prefEntry.getNameen().equalsIgnoreCase(pref)) {
                    prefName = prefEntry.getName();
                    break;
                }
            }
        } catch (IOException ex) {
            System.err.println("Unexpected exception: " + ex.toString());
            ex.printStackTrace();
        }

        new ThymVarMapPrefBuilder(pref, prefName).applyModelAttr(model);

        return "dyn/pref/pref";
    }
}
