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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jp.igapyon.cityinfojp.dyn.fragment.JumbotronFragmentBean;
import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarBean;
import jp.igapyon.cityinfojp.dyn.thymvarmap.ThymVarMapPrefBuilder;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntry;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntryUtil;
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

        LinkedHashMap<String, Object> map = ThymVarMapPrefBuilder.buildVarMap(pref, prefName);
        for (Map.Entry<String, Object> look : map.entrySet()) {
            model.addAttribute(look.getKey(), look.getValue());
        }

        return "dyn/pref/pref";
    }

    public static JumbotronFragmentBean getJumbotronBean(String prefName) {
        JumbotronFragmentBean jumbotron = new JumbotronFragmentBean();
        jumbotron.setTitle(prefName);
        return jumbotron;
    }

    public static NavbarBean getNavbarBean(String pref) {
        NavbarBean navbar = NavbarUtil.buildNavbar(pref);
        navbar.getItemList().get(1).setCurrent(true);
        return navbar;
    }

    public static List<CityInfoEntry> buildEntityList() throws IOException {
        List<CityInfoEntry> allEntryList = new ArrayList<CityInfoEntry>();

        // 個別エントリではなくマージ済みjsonファイルを利用して読み込み。
        try (InputStream is = new ClassPathResource("static/input/merged/merged-cityinfoentry-all.json")
                .getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            StringBuffer buf = new StringBuffer();
            char[] copyBuf = new char[8192];
            for (;;) {
                int length = br.read(copyBuf);
                if (length <= 0) {
                    break;
                }
                buf.append(new String(copyBuf, 0, length));
            }
            List<CityInfoEntry> entryList = CityInfoEntryUtil.readEntryList(buf.toString());
            allEntryList.addAll(entryList);
        }
        return allEntryList;
    }

    public static void sortEntryList(final List<CityInfoEntry> allEntryList) {
        Collections.sort(allEntryList, new Comparator<CityInfoEntry>() {
            @Override
            public int compare(CityInfoEntry left, CityInfoEntry right) {
                if (left.getEntryDate().compareTo(right.getEntryDate()) != 0) {
                    // 降順
                    return -left.getEntryDate().compareTo(right.getEntryDate());
                }

                // TODO 同日エントリのソート順が未記述
                return -1;
            }
        });
    }

    public static List<CityInfoDisplayEntry> entryList2DispEntryList(final List<CityInfoEntry> allEntryList) {
        List<CityInfoDisplayEntry> dispEntryList = new ArrayList<CityInfoDisplayEntry>();
        for (CityInfoEntry entry : allEntryList) {
            CityInfoDisplayEntry dispEntry = new CityInfoDisplayEntry();

            if ("要請".equals(entry.getState())) {
                dispEntry.setIconText("要請");
                dispEntry.setIconColor("#ffc107");
                dispEntry.setIconTextColor("#000000");
            } else if ("指示".equals(entry.getState())) {
                dispEntry.setIconText("指示");
                dispEntry.setIconColor("#dc3545");
                dispEntry.setIconTextColor("#ffffff");
            } else if ("休校".equals(entry.getState())) {
                dispEntry.setIconText("休校");
                dispEntry.setIconColor("#17a2b8");
                dispEntry.setIconTextColor("#ffffff");
            } else {
                dispEntry.setIconText("その他");
                dispEntry.setIconColor("#6c757d");
                dispEntry.setIconTextColor("#000000");
            }

            dispEntry.setTitleText(entry.getPref()
                    + (null == entry.getCity() || entry.getCity().trim().length() == 0 ? "" : entry.getCity()));

            String descText = "";
            descText = "(" + entry.getEntryDate() + "起票) " + entry.getTarget() + " "
                    + (null == entry.getTargetRange() || entry.getTargetRange().trim().length() == 0 ? ""
                            : entry.getTargetRange())
                    + " : " + entry.getReason();
            dispEntry.setDescText(descText);
            dispEntryList.add(dispEntry);
        }

        return dispEntryList;
    }
}
