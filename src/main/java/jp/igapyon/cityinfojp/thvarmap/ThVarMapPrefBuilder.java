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

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import jp.igapyon.cityinfojp.disp.DisplayCityInfoEntry;
import jp.igapyon.cityinfojp.disp.DisplaySearchButton;
import jp.igapyon.cityinfojp.fragment.jumbotron.JumbotronFragmentBean;
import jp.igapyon.cityinfojp.fragment.navbar.NavbarBean;
import jp.igapyon.cityinfojp.json.JsonCityInfoEntry;
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
     * Thymeleaf のオンライン処理・バッチ処理の両方で利用可能なキー値マップを構成。
     * 
     * @return 抽象化されたキー値マップ。
     */
    @Override
    protected LinkedHashMap<String, Object> buildVarMap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        try {
            result.put("dispOfficialButtonList", buildOfficialButtonList(prefName));

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
        NavbarBean navbar = ThNavbarUtil.buildNavbar(pref);
        navbar.getItemList().get(1).setCurrent(true);
        return navbar;
    }

    /**
     * 公式ボタンのリストを生成。
     * 
     * @param prefName 都道府県名。
     * @return 表示用検索ボタンのリスト。
     * @throws IOException 入出力例外が発生。
     */
    public static List<DisplaySearchButton> buildOfficialButtonList(String prefName) throws IOException {
        // 公式ボタン生成
        List<DisplaySearchButton> dispSearchButtonList = new ArrayList<>();
        List<JsonPrefUrlEntry> urlList = JsonPrefUrlEntryUtil.readEntryListFromClasspath();
        for (JsonPrefUrlEntry prefUrl : urlList) {
            if (prefName.equals(prefUrl.getName())) {
                boolean isPrimary = true;
                for (String url : prefUrl.getUrl()) {
                    DisplaySearchButton button = new DisplaySearchButton();
                    button.setText("公式");
                    button.setSearchUrl(url);
                    button.setPrimary(isPrimary);
                    isPrimary = false;
                    dispSearchButtonList.add(button);
                }
            }
        }
        return dispSearchButtonList;
    }

    /**
     * 検索ボタンのリストを生成。
     * 
     * @param prefName 都道府県名。
     * @return 表示用検索ボタンのリスト。
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
