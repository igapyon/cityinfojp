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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import jp.igapyon.cityinfojp.dyn.fragment.JumbotronFragmentBean;
import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarBean;

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
        } else if (body.startsWith("/dyn/contributor")) {
            jumbotron.setTitle("Contributor");
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
