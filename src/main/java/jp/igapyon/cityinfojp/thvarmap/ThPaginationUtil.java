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

import jp.igapyon.cityinfojp.fragment.pagination.PaginationBean;
import jp.igapyon.cityinfojp.fragment.pagination.PaginationItemBean;

/**
 * このサイト用の Pagination を構築するためのユーティリィ。
 *
 * @author Toshiki Iga
 */
public class ThPaginationUtil {
    public static PaginationBean buildPagination() {
        PaginationBean pagination = new PaginationBean();

        {
            PaginationItemBean item = new PaginationItemBean();
            pagination.getItemList().add(item);
            pagination.getItemList().add(item);
            pagination.getItemList().add(item);
            pagination.getItemList().add(item);
            pagination.getItemList().add(item);
        }

        return pagination;
    }
}
