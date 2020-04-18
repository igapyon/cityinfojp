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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jp.igapyon.cityinfojp.dyn.DisplayPrefEntry;
import jp.igapyon.cityinfojp.dyn.fragment.JumbotronFragmentBean;
import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarBean;
import jp.igapyon.cityinfojp.input.entry.PrefEntry;
import jp.igapyon.cityinfojp.input.entry.PrefEntryUtil;
import jp.igapyon.cityinfojp.input.entry.PrefUrlEntry;
import jp.igapyon.cityinfojp.input.entry.PrefUrlEntryUtil;

/**
 * Thymeleaf の Var map をビルドします。
 * 
 * ベーシックな変数をビルドします。
 */
public class ThymVarMapSimpleBuilder extends AbstractThymVarMapBuilder {
    private String sourcePath;

    public ThymVarMapSimpleBuilder(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    /**
     * 変数をビルド。
     */
    protected LinkedHashMap<String, Object> buildVarMap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        result.put("jumbotron", getJumbotronBean(sourcePath));

        result.put("navbar", getNavbarBean(sourcePath));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        result.put("processDateTime", dtf.format(LocalDateTime.now()));

        // リンク集のみ特別
        // TODO これは別途のクラスに根っこから分けた方がよさそう。
        if (sourcePath.startsWith("/dyn/link")) {
            try {
                List<DisplayPrefEntry> prefList = new ArrayList<>();
                List<PrefEntry> prefAllList = PrefEntryUtil.readEntryListFromClasspath();
                for (PrefEntry prefEntry : prefAllList) {
                    DisplayPrefEntry dispPref = new DisplayPrefEntry();
                    prefList.add(dispPref);
                    dispPref.setText(prefEntry.getName());
                    dispPref.setUrl("/pref/" + prefEntry.getNameen().toLowerCase() + ".html");

                    List<PrefUrlEntry> prefUrlEntryList = PrefUrlEntryUtil.readEntryListFromClasspath();
                    for (PrefUrlEntry urlEntry : prefUrlEntryList) {
                        if (prefEntry.getName().equals(urlEntry.getName())) {
                            dispPref.getUrlList().addAll(urlEntry.getUrl());
                        }
                    }
                }
                result.put("prefList", prefList);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }

    public static String getPathStringWithoutExt(String requestURI) {
        if (requestURI.lastIndexOf('.') < 0) {
            return requestURI;
        }
        String body = requestURI.substring(0, requestURI.lastIndexOf('.'));
        return body;
    }

    public static JumbotronFragmentBean getJumbotronBean(String requestURI) {
        JumbotronFragmentBean jumbotron = new JumbotronFragmentBean();

        String body = getPathStringWithoutExt(requestURI);
        if (body.startsWith("/dyn/about")) {
            jumbotron.setTitle("About");
        } else if (body.startsWith("/dyn/arch")) {
            jumbotron.setTitle("Architecture");
        } else if (body.startsWith("/dyn/contributor")) {
            jumbotron.setTitle("Contributor");
        } else if (body.startsWith("/dyn/faq")) {
            jumbotron.setTitle("FAQ");
        } else if (body.startsWith("/dyn/link")) {
            jumbotron.setTitle("関連リンク");
        } else if (body.startsWith("/dyn/policy")) {
            jumbotron.setTitle("Policy");
        }

        return jumbotron;
    }

    public static NavbarBean getNavbarBean(String requestURI) {
        NavbarBean navbar = NavbarUtil.buildNavbar(null);
        String body = getPathStringWithoutExt(requestURI);
        if (body.startsWith("/dyn/about")) {
            navbar.getItemList().get(4).setCurrent(true);
        } else if (body.startsWith("/dyn/contributor")) {
            navbar.getItemList().get(3).setCurrent(true);
        } else if (body.startsWith("/dyn/link")) {
            navbar.getItemList().get(2).setCurrent(true);
        } else if (body.startsWith("/dyn/policy")) {
            navbar.getItemList().get(4).setCurrent(true);
        }
        return navbar;
    }
}
