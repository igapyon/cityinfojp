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

import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarBean;
import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarDropdownBean;
import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarDropdownItemBean;
import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarItemBean;
import jp.igapyon.cityinfojp.input.entry.PrefEntry;
import jp.igapyon.cityinfojp.input.entry.PrefEntryUtil;

public class NavbarUtil {
    public static NavbarBean buildNavbar() {
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

            try {
                List<PrefEntry> prefList = PrefEntryUtil.readEntryListFromClasspath();
                for (PrefEntry pref : prefList) {
                    NavbarDropdownItemBean dropdownItem = new NavbarDropdownItemBean();
                    dropdown.getItemList().add(dropdownItem);
                    dropdownItem.setText(pref.getName());
                    dropdownItem.setHref("/pref/" + pref.getNameen().toLowerCase() + ".html");
                }
            } catch (IOException ex) {
                System.err.println("Unexpected exception: " + ex.toString());
                ex.printStackTrace();
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
