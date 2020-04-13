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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import jp.igapyon.cityinfojp.dyn.CityInfoDisplayEntry;
import jp.igapyon.cityinfojp.dyn.fragment.JumbotronFragmentBean;
import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarBean;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntry;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntryUtil;

/**
 * Thymeleaf の Var map をビルドします。
 * 
 * 都道府県の単位でビルドします。
 */
public class ThymVarMapPrefBuilder extends AbstractThymVarMapBuilder {
    private String prefNameen;
    String prefName;

    public ThymVarMapPrefBuilder(String prefNameen, String prefName) {
        this.prefNameen = prefNameen;
        this.prefName = prefName;
    }

    /**
     * final String name, final Object value
     */
    protected LinkedHashMap<String, Object> buildVarMap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        try {
            List<CityInfoEntry> allEntryList = ThymVarMapIndexBuilder.buildEntityList();
            ThymVarMapIndexBuilder.sortEntryList(allEntryList);

            // pref で絞り込み
            List<CityInfoEntry> entryList = new ArrayList<>();
            for (CityInfoEntry lookup : allEntryList) {
                if (prefName.equals(lookup.getPref())) {
                    entryList.add(lookup);
                }
            }

            // 絞り込み後のデータを利用
            List<CityInfoDisplayEntry> dispEntryList = ThymVarMapIndexBuilder.entryList2DispEntryList(entryList);

            // Prefでしぼりこみ
            result.put("dispEntryList", dispEntryList);

            result.put("jumbotron", getJumbotronBean(prefName));

            result.put("navbar", getNavbarBean(prefNameen));

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            result.put("processDateTime", dtf.format(LocalDateTime.now()));
        } catch (IOException ex) {
            System.err.println("Unexpected exception: " + ex.toString());
            ex.printStackTrace();
        }

        return result;
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
