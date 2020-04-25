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
import java.util.List;

import jp.igapyon.cityinfojp.fragment.pagination.PaginationBean;
import jp.igapyon.cityinfojp.fragment.pagination.PaginationItemBean;
import jp.igapyon.cityinfojp.json.JsonPrefEntry;
import jp.igapyon.cityinfojp.json.JsonPrefEntryUtil;

/**
 * このサイト用の Pagination を構築するためのユーティリィ。
 *
 * @author Toshiki Iga
 */
public class ThPaginationUtil {
    public static PaginationBean buildPagination(String pref) throws IOException {
        PaginationBean pagination = new PaginationBean();

        // すべての都道府県情報
        List<JsonPrefEntry> prefList = JsonPrefEntryUtil.readEntryListFromClasspath();

        // とりあえず全てを登録してみる。
        for (JsonPrefEntry lookup : prefList) {
            PaginationItemBean item = new PaginationItemBean();
            pagination.getItemList().add(item);
            item.setTitle(lookup.getName());
            item.setUrl("/pref/" + lookup.getNameen().toLowerCase() + ".html");
            if (lookup.getName().equals(pref)) {
                item.setCurrent(true);
            }
        }

        return pagination;
    }
}
