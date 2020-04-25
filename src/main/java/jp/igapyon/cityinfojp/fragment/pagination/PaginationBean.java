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
package jp.igapyon.cityinfojp.fragment.pagination;

import java.util.ArrayList;
import java.util.List;

/**
 * Thymeleaf + Bootstrap の Pagination Fragment のための Bean.
 * 
 * @author Toshiki Iga
 */
public class PaginationBean {
    private PaginationItemBean prevItem;
    private PaginationItemBean nextItem;

    private List<PaginationItemBean> itemList = new ArrayList<>();

    public boolean hasPrev() {
        return prevItem != null;
    }

    public boolean hasNext() {
        return nextItem != null;
    }

    public PaginationItemBean getPrevItem() {
        return prevItem;
    }

    public void setPrevItem(PaginationItemBean prevItem) {
        this.prevItem = prevItem;
    }

    public PaginationItemBean getNextItem() {
        return nextItem;
    }

    public void setNextItem(PaginationItemBean nextItem) {
        this.nextItem = nextItem;
    }

    public List<PaginationItemBean> getItemList() {
        return itemList;
    }
}
