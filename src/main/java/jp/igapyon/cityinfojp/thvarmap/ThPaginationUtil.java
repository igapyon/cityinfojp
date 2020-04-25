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

        int currentIndex = -1;
        for (int index = 0; index < pagination.getItemList().size(); index++) {
            if (pagination.getItemList().get(index).isCurrent()) {
                currentIndex = index;
                break;
            }
        }

        int fromIndex = -1;
        if (currentIndex < 2) {
            fromIndex = 0;
        } else {
            if (currentIndex > pagination.getItemList().size() - 3) {
                fromIndex = pagination.getItemList().size() - 5;
            } else {
                fromIndex = currentIndex - 2;
            }
        }

        int toIndex = -1;
        if (currentIndex > pagination.getItemList().size() - 3) {
            toIndex = pagination.getItemList().size();
        } else {
            if (currentIndex < 2) {
                toIndex = 4;
            } else {
                toIndex = currentIndex + 2;
            }
        }

        {
            // prev
            if (currentIndex != 0) {
                int prevIndex = currentIndex - 4;
                if (prevIndex < 0) {
                    prevIndex = 0;
                }
                pagination.setPrevItem(pagination.getItemList().get(prevIndex));
            }
        }
        {
            // next 
            if (currentIndex != pagination.getItemList().size() - 1) {
                int nextIndex = currentIndex + 4;
                if (nextIndex > pagination.getItemList().size() - 1) {
                    nextIndex = pagination.getItemList().size() - 1;
                }
                pagination.setNextItem(pagination.getItemList().get(nextIndex));
            }
        }

        // 余分な部分を除去。
        for (int pointer = pagination.getItemList().size() - 1; pointer > toIndex; pointer--) {
            pagination.getItemList().remove(pointer);
        }

        // 余分な部分を除去。
        for (int counter = fromIndex; counter > 0; counter--) {
            pagination.getItemList().remove(0);
        }

        return pagination;
    }
}
