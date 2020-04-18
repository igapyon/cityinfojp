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

import jp.igapyon.cityinfojp.dyn.DisplayAreaEntry;
import jp.igapyon.cityinfojp.dyn.DisplayCityInfoEntry;
import jp.igapyon.cityinfojp.dyn.DisplayPrefEntry;
import jp.igapyon.cityinfojp.dyn.fragment.JumbotronFragmentBean;
import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarBean;
import jp.igapyon.cityinfojp.input.entry.AreaEntry;
import jp.igapyon.cityinfojp.input.entry.AreaEntryUtil;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntry;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntryUtil;
import jp.igapyon.cityinfojp.input.entry.PrefEntry;
import jp.igapyon.cityinfojp.input.entry.PrefEntryUtil;

/**
 * Thymeleaf の Var map をビルドします。
 * 
 * Indexの単位でビルドします。
 */
public class ThymVarMapIndexBuilder extends AbstractThymVarMapBuilder {
    /**
     * 変数をビルド。
     */
    protected LinkedHashMap<String, Object> buildVarMap() throws IOException {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        List<CityInfoEntry> allEntryList = buildEntityList();
        sortEntryList(allEntryList);
        List<DisplayCityInfoEntry> dispEntryList = entryList2DispEntryList(allEntryList);

        result.put("dispAreaList", buildAreaList());

        result.put("dispEntryList", dispEntryList);

        result.put("jumbotron", getJumbotronBean());

        result.put("navbar", getNavbarBean());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        result.put("processDateTime", dtf.format(LocalDateTime.now()));

        return result;
    }

    public static JumbotronFragmentBean getJumbotronBean() {
        JumbotronFragmentBean jumbotron = new JumbotronFragmentBean();
        jumbotron.setTitle("cityinfojp");
        return jumbotron;
    }

    public static NavbarBean getNavbarBean() {
        NavbarBean navbar = NavbarUtil.buildNavbar(null);
        navbar.getItemList().get(0).setCurrent(true);
        return navbar;
    }

    /**
     * エリア一覧を取得する。
     * @return 表示用のエリア一覧。
     * @throws IOException
     */
    public static List<DisplayAreaEntry> buildAreaList() throws IOException {
        List<DisplayAreaEntry> dispAreaList = new ArrayList<>();

        List<AreaEntry> areaList = AreaEntryUtil.readEntryListFromClasspath();
        List<PrefEntry> prefList = PrefEntryUtil.readEntryListFromClasspath();
        for (AreaEntry entry : areaList) {
            DisplayAreaEntry dispArea = new DisplayAreaEntry();
            dispAreaList.add(dispArea);
            dispArea.setName(entry.getName());
            dispArea.setNameen(entry.getNameen());
            dispArea.setUrl("/pref/" + entry.getNameen().toLowerCase() + ".html");

            // System.err.println(entry.getName() + ":" + entry.getNameen());
            for (String look : entry.getPref()) {
                for (PrefEntry prefLookup : prefList) {
                    if (look.equals(prefLookup.getCode())) {
                        DisplayPrefEntry newPref = new DisplayPrefEntry();
                        dispArea.getPrefList().add(newPref);
                        newPref.setText(prefLookup.getName());
                        newPref.setUrl("/pref/" + prefLookup.getNameen().toLowerCase());
                    }
                }
                // System.err.println("  " + look);
            }
        }

        return dispAreaList;
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
                if (left == null && right == null) {
                    return 0;
                }
                if (left == null) {
                    return -1;
                }
                if (right == null) {
                    return 1;
                }

                if (left.getEntryDate().compareTo(right.getEntryDate()) != 0) {
                    // 降順
                    return -left.getEntryDate().compareTo(right.getEntryDate());
                }

                // TODO 同日エントリのソート順が未記述
                return 0;
            }
        });
    }

    public static List<DisplayCityInfoEntry> entryList2DispEntryList(final List<CityInfoEntry> allEntryList)
            throws IOException {
        List<PrefEntry> prefList = PrefEntryUtil.readEntryListFromClasspath();

        List<DisplayCityInfoEntry> dispEntryList = new ArrayList<DisplayCityInfoEntry>();
        for (CityInfoEntry entry : allEntryList) {
            DisplayCityInfoEntry dispEntry = new DisplayCityInfoEntry();

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
            } else if ("宣言".equals(entry.getState())) {
                dispEntry.setIconText("宣言");
                dispEntry.setIconColor("#6610f2");
                dispEntry.setIconTextColor("#ffffff");
            } else {
                dispEntry.setIconText("その他");
                dispEntry.setIconColor("#6c757d");
                dispEntry.setIconTextColor("#000000");
            }

            dispEntry.setTitleText(entry.getPref() //
                    + (null == entry.getCity() || entry.getCity().trim().length() == 0 //
                            ? "" //
                            : " (" + entry.getCity() + ")"));

            for (PrefEntry pref : prefList) {
                if (pref.getName().equals(entry.getPref())) {
                    dispEntry.setPrefUrl("/pref/" + pref.getNameen().toLowerCase() + ".html");
                    break;
                }
            }

            String descText = "";
            descText = "(" + entry.getEntryDate() + "起票) " + entry.getTarget() + " "
                    + (null == entry.getTargetRange() || entry.getTargetRange().trim().length() == 0 ? ""
                            : entry.getTargetRange());
            if (entry.getStartDate() != null && entry.getStartDate().trim().length() > 0) {
                descText += " " + entry.getStartDate() + "から";
            }
            if (entry.getEndDate() != null && entry.getEndDate().trim().length() > 0) {
                descText += " " + entry.getEndDate() + "まで";
            }
            if (entry.getReason() != null && entry.getReason().trim().length() > 0) {
                descText += " : " + entry.getReason();
            }
            dispEntry.setDescText(descText);
            dispEntry.setUrls(entry.getURL());
            dispEntryList.add(dispEntry);
        }

        return dispEntryList;
    }
}
