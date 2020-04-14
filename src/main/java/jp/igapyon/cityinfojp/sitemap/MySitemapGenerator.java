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
import java.util.Date;

import jp.igapyon.sitemapgenerator4j.SitemapGenerator4j;
import jp.igapyon.sitemapgenerator4j.SitemapInfo;
import jp.igapyon.sitemapgenerator4j.SitemapInfoUrl;

/**
 * 
 * @see https://www.sitemaps.org/protocol.html
 * @author Toshiki Iga
 */
public class MySitemapGenerator {
    public static void main(String[] args) throws IOException {
        SitemapGenerator4j gen = new SitemapGenerator4j();
        SitemapInfo entry = new SitemapInfo();

        {
            SitemapInfoUrl url = new SitemapInfoUrl();
            entry.getUrlList().add(url);
            url.setLoc("https://cityinfojp.herokuapp.com/");
            url.setLastmod(new Date());
            url.setChangefreq(SitemapInfoUrl.Changefreq.Daily);
            url.setPriority("0.8");
        }

        {
            SitemapInfoUrl url = new SitemapInfoUrl();
            entry.getUrlList().add(url);
            url.setLoc("https://cityinfojp.herokuapp.com/about.html");
            // 更新日付は出力しない
            url.setChangefreq(SitemapInfoUrl.Changefreq.Monthly);
            url.setPriority("0.6");
        }

        {
            SitemapInfoUrl url = new SitemapInfoUrl();
            entry.getUrlList().add(url);
            url.setLoc("https://cityinfojp.herokuapp.com/link.html");
            // 更新日付は出力しない
            url.setChangefreq(SitemapInfoUrl.Changefreq.Monthly);
            url.setPriority("0.2");
        }

        gen.write(entry, new File("./src/main/resources/static/sitemap.xml"));
    }
}
