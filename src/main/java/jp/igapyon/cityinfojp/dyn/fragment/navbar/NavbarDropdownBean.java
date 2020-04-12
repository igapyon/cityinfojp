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
package jp.igapyon.cityinfojp.dyn.fragment.navbar;

import java.util.ArrayList;
import java.util.List;

public class NavbarDropdownBean {
    private String text;

    private List<NavbarItemBean> itemList = new ArrayList<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<NavbarItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<NavbarItemBean> itemList) {
        this.itemList = itemList;
    }
}
