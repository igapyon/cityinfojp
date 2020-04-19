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
package jp.igapyon.cityinfojp.disp;

import java.util.ArrayList;
import java.util.List;

/**
 * cityinfo の HTML レンダリング用のエントリ
 * 
 * @author Toshiki Iga
 */
public class DisplayCityInfoEntry {
    private String iconText;
    private String iconColor;
    private String iconTextColor;
    private String titleText;
    /**
     * タイトル行から都道府県へのリンク
     */
    private String prefUrl;
    private String descText;
    private List<DisplayCityInfoUrlEntry> urlList = new ArrayList<>();

    public String getPrefUrl() {
        return prefUrl;
    }

    public void setPrefUrl(String prefUrl) {
        this.prefUrl = prefUrl;
    }

    public String getIconText() {
        return iconText;
    }

    public void setIconText(String iconText) {
        this.iconText = iconText;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }

    public List<DisplayCityInfoUrlEntry> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<DisplayCityInfoUrlEntry> url) {
        this.urlList = url;
    }

    public String getIconTextColor() {
        return iconTextColor;
    }

    public void setIconTextColor(String iconTextColor) {
        this.iconTextColor = iconTextColor;
    }
}
