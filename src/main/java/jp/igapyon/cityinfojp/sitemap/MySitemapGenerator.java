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
package jp.igapyon.cityinfojp.sitemap;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jp.igapyon.sitemapgenerator4j.SitemapEntry;
import jp.igapyon.sitemapgenerator4j.SitemapEntryUrl;
import jp.igapyon.sitemapgenerator4j.SitemapGenerator4j;

/**
 * 
 * @see https://www.sitemaps.org/protocol.html
 * @author Toshiki Iga
 */
public class MySitemapGenerator {
    public static void main(String[] args) throws IOException {
        SitemapGenerator4j gen = new SitemapGenerator4j();
        SitemapEntry entry = new SitemapEntry();

        {
            SitemapEntryUrl url = new SitemapEntryUrl();
            entry.getUrlList().add(url);
            url.setLoc("https://cityinfojp.herokuapp.com/");
            url.setLastmod(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()));
            url.setChangefreq("daily");
            url.setPriority("0.8");
        }

        {
            SitemapEntryUrl url = new SitemapEntryUrl();
            entry.getUrlList().add(url);
            url.setLoc("https://cityinfojp.herokuapp.com/about.html");
            url.setLastmod(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()));
            url.setChangefreq("monthly");
            url.setPriority("0.6");
        }

        {
            SitemapEntryUrl url = new SitemapEntryUrl();
            entry.getUrlList().add(url);
            url.setLoc("https://cityinfojp.herokuapp.com/link.html");
            url.setLastmod(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()));
            url.setChangefreq("monthly");
            url.setPriority("0.2");
        }

        gen.write(entry, new File("./src/main/resources/static/sitemap.xml"));
    }
}
