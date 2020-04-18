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
package jp.igapyon.cityinfojp.thvarmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import jp.igapyon.cityinfojp.disp.DisplayCityInfoEntry;
import jp.igapyon.cityinfojp.disp.DisplaySearchButton;
import jp.igapyon.cityinfojp.fragment.jumbotron.JumbotronFragmentBean;
import jp.igapyon.cityinfojp.fragment.navbar.NavbarBean;
import jp.igapyon.cityinfojp.json.JsonCityInfoEntry;
import jp.igapyon.cityinfojp.json.JsonCityInfoEntryUtil;
import jp.igapyon.cityinfojp.json.JsonPrefUrlEntry;
import jp.igapyon.cityinfojp.json.JsonPrefUrlEntryUtil;

/**
 * Thymeleaf の Var map をビルドします。
 * 
 * 都道府県の単位でビルドします。
 */
public class ThVarMapPrefBuilder extends AbstractThVarMapBuilder {
    private String prefNameen;
    String prefName;

    public ThVarMapPrefBuilder(String prefNameen, String prefName) {
        this.prefNameen = prefNameen;
        this.prefName = prefName;
    }

    /**
     * final String name, final Object value
     */
    protected LinkedHashMap<String, Object> buildVarMap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        try {
            result.put("dispSearchButtonList", buildSearchButtonList(prefName));

            List<JsonCityInfoEntry> allEntryList = ThVarMapIndexBuilder.buildEntityList();
            ThVarMapIndexBuilder.sortEntryList(allEntryList);

            // pref で絞り込み
            List<JsonCityInfoEntry> entryList = new ArrayList<>();
            for (JsonCityInfoEntry lookup : allEntryList) {
                if (prefName.equals(lookup.getPref())) {
                    entryList.add(lookup);
                }
            }

            // 絞り込み後のデータを利用
            List<DisplayCityInfoEntry> dispEntryList = ThVarMapIndexBuilder.entryList2DispEntryList(entryList);

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

    public static List<JsonCityInfoEntry> buildEntityList() throws IOException {
        List<JsonCityInfoEntry> allEntryList = new ArrayList<JsonCityInfoEntry>();

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
            List<JsonCityInfoEntry> entryList = JsonCityInfoEntryUtil.readEntryList(buf.toString());
            allEntryList.addAll(entryList);
        }
        return allEntryList;
    }

    public static void sortEntryList(final List<JsonCityInfoEntry> allEntryList) {
        Collections.sort(allEntryList, new Comparator<JsonCityInfoEntry>() {
            @Override
            public int compare(JsonCityInfoEntry left, JsonCityInfoEntry right) {
                if (left.getEntryDate().compareTo(right.getEntryDate()) != 0) {
                    // 降順
                    return -left.getEntryDate().compareTo(right.getEntryDate());
                }

                // TODO 同日エントリのソート順が未記述
                return -1;
            }
        });
    }

    public static List<DisplayCityInfoEntry> entryList2DispEntryList(final List<JsonCityInfoEntry> allEntryList) {
        List<DisplayCityInfoEntry> dispEntryList = new ArrayList<DisplayCityInfoEntry>();
        for (JsonCityInfoEntry entry : allEntryList) {
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

    /**
     * 特殊
     * 
     * @throws IOException 入出力例外が発生。
     */
    @SuppressWarnings("deprecation")
    public static List<DisplaySearchButton> buildSearchButtonList(String prefName) throws IOException {
        // 基準日
        final Date searchBaseDate = new Date(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 検索ボタン生成
        List<DisplaySearchButton> dispSearchButtonList = new ArrayList<>();
        List<JsonPrefUrlEntry> urlList = JsonPrefUrlEntryUtil.readEntryListFromClasspath();
        for (JsonPrefUrlEntry prefUrl : urlList) {
            if (prefName.equals(prefUrl.getName())) {
                boolean isPrimary = true;
                for (String url : prefUrl.getUrl()) {
                    DisplaySearchButton button = new DisplaySearchButton();
                    button.setText("緊急");
                    button.setSearchUrl("https://www.google.com/search?pws=0&q=site:" + URLEncoder.encode(url)
                            + "+after:" + sdf.format(searchBaseDate) + "+緊急");
                    button.setPrimary(isPrimary);
                    isPrimary = false;
                    dispSearchButtonList.add(button);
                }
                isPrimary = true;
                for (String url : prefUrl.getUrl()) {
                    DisplaySearchButton button = new DisplaySearchButton();
                    button.setText("宣言");
                    button.setSearchUrl("https://www.google.com/search?pws=0&q=site:" + URLEncoder.encode(url)
                            + "+after:" + sdf.format(searchBaseDate) + "+宣言");
                    button.setPrimary(isPrimary);
                    isPrimary = false;
                    dispSearchButtonList.add(button);
                }
                isPrimary = true;
                for (String url : prefUrl.getUrl()) {
                    DisplaySearchButton button = new DisplaySearchButton();
                    button.setText("要請");
                    button.setSearchUrl("https://www.google.com/search?pws=0&q=site:" + URLEncoder.encode(url)
                            + "+after:" + sdf.format(searchBaseDate) + "+要請");
                    button.setPrimary(isPrimary);
                    isPrimary = false;
                    dispSearchButtonList.add(button);
                }
                isPrimary = true;
                for (String url : prefUrl.getUrl()) {
                    DisplaySearchButton button = new DisplaySearchButton();
                    button.setText("休校");
                    button.setSearchUrl("https://www.google.com/search?pws=0&q=site:" + URLEncoder.encode(url)
                            + "+after:" + sdf.format(searchBaseDate) + "+休校");
                    button.setPrimary(isPrimary);
                    isPrimary = false;
                    dispSearchButtonList.add(button);
                }
            }
        }
        return dispSearchButtonList;
    }
}
