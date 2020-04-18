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
package jp.igapyon.cityinfojp.thymvarmap;

import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarBean;
import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarDropdownBean;
import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarItemBean;

public class NavbarUtil {
    public static NavbarBean buildNavbar(String pref) {
        NavbarBean navbar = new NavbarBean();
        navbar.setText("cityinfojp");
        {
            NavbarItemBean item = new NavbarItemBean();
            navbar.getItemList().add(item);
            item.setText("Home");
            item.setHref("/index.html");
        }
        {
            NavbarItemBean item = new NavbarItemBean();
            navbar.getItemList().add(item);
            item.setText("都道府県");
            item.setDropdown(true);

            NavbarDropdownBean dropdown = new NavbarDropdownBean();
            item.setDropdownBean(dropdown);

            NavbarItemBean dropdownMenuItem = new NavbarItemBean();
            dropdown.getItemList().add(dropdownMenuItem);
            dropdownMenuItem.setText("日本国");
            dropdownMenuItem.setHref("/pref/japan.html");
            if ("japan".equalsIgnoreCase(pref)) {
                dropdownMenuItem.setCurrent(true);
            }

            // 八地方区分
            // 北海道、東北、関東、中部、近畿、中国、四国、九州沖縄
            dropdownMenuItem = new NavbarItemBean();
            dropdown.getItemList().add(dropdownMenuItem);
            dropdownMenuItem.setText("北海道");
            dropdownMenuItem.setHref("/pref/hokkaido.html");
            if ("hokkaido".equalsIgnoreCase(pref)) {
                dropdownMenuItem.setCurrent(true);
            }

            dropdownMenuItem = new NavbarItemBean();
            dropdown.getItemList().add(dropdownMenuItem);
            dropdownMenuItem.setText("東北");
            dropdownMenuItem.setHref("/pref/tohoku.html");
            if ("tohoku".equalsIgnoreCase(pref)) {
                dropdownMenuItem.setCurrent(true);
            }

            dropdownMenuItem = new NavbarItemBean();
            dropdown.getItemList().add(dropdownMenuItem);
            dropdownMenuItem.setText("関東");
            dropdownMenuItem.setHref("/pref/kanto.html");
            if ("kanto".equalsIgnoreCase(pref)) {
                dropdownMenuItem.setCurrent(true);
            }

            dropdownMenuItem = new NavbarItemBean();
            dropdown.getItemList().add(dropdownMenuItem);
            dropdownMenuItem.setText("中部");
            dropdownMenuItem.setHref("/pref/chubu.html");
            if ("chubu".equalsIgnoreCase(pref)) {
                dropdownMenuItem.setCurrent(true);
            }

            dropdownMenuItem = new NavbarItemBean();
            dropdown.getItemList().add(dropdownMenuItem);
            dropdownMenuItem.setText("近畿");
            dropdownMenuItem.setHref("/pref/kinki.html");
            if ("kinki".equalsIgnoreCase(pref)) {
                dropdownMenuItem.setCurrent(true);
            }

            dropdownMenuItem = new NavbarItemBean();
            dropdown.getItemList().add(dropdownMenuItem);
            dropdownMenuItem.setText("中国");
            dropdownMenuItem.setHref("/pref/chugoku.html");
            if ("chugoku".equalsIgnoreCase(pref)) {
                dropdownMenuItem.setCurrent(true);
            }

            dropdownMenuItem = new NavbarItemBean();
            dropdown.getItemList().add(dropdownMenuItem);
            dropdownMenuItem.setText("四国");
            dropdownMenuItem.setHref("/pref/shikoku.html");
            if ("shikoku".equalsIgnoreCase(pref)) {
                dropdownMenuItem.setCurrent(true);
            }

            dropdownMenuItem = new NavbarItemBean();
            dropdown.getItemList().add(dropdownMenuItem);
            dropdownMenuItem.setText("九州沖縄");
            dropdownMenuItem.setHref("/pref/kyushuokinawa.html");
            if ("kyushuokinawa".equalsIgnoreCase(pref)) {
                dropdownMenuItem.setCurrent(true);
            }
        }
        {
            NavbarItemBean item = new NavbarItemBean();
            navbar.getItemList().add(item);
            item.setText("Link");
            item.setHref("/link.html");
        }
        {
            NavbarItemBean item = new NavbarItemBean();
            navbar.getItemList().add(item);
            item.setText("Contributor");
            item.setHref("/contributor.html");
        }
        {
            NavbarItemBean item = new NavbarItemBean();
            navbar.getItemList().add(item);
            item.setText("About");
            item.setHref("/about.html");
        }

        return navbar;
    }
}
