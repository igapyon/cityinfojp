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
 * pref の HTML レンダリング用のエントリ
 * 
 * @author Toshiki Iga
 */
public class DisplayPrefEntry {
    private String text;
    private int infoCount = 0;
    private String url;

    /**
     * リンク集のみで利用
     */
    private List<String> urlList = new ArrayList<>();

    public List<String> getUrlList() {
        return urlList;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getInfoCount() {
        return infoCount;
    }

    public void setInfoCount(int count) {
        this.infoCount = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
