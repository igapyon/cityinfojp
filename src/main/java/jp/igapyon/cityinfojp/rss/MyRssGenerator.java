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
package jp.igapyon.cityinfojp.rss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;

import jp.igapyon.cityinfojp.disp.DisplayCityInfoEntry;
import jp.igapyon.cityinfojp.json.JsonCityInfoEntry;
import jp.igapyon.cityinfojp.thvarmap.ThVarMapIndexBuilder;

/**
 * このサイトの rss を生成します。
 * 
 * @author Toshiki Iga
 */
public class MyRssGenerator {
    /**
     * このサイトの rss 生成のエントリポイント。
     * 
     * @param args アプリ起動引数。
     * @throws IOException 入出力例外が発生した場合。
     */
    public static void main(String[] args) throws IOException {
        System.err.println("generate rss for index-all.");
        List<JsonCityInfoEntry> jsonentryList = ThVarMapIndexBuilder.buildEntityList();
        ThVarMapIndexBuilder.sortEntryList(jsonentryList);

        SyndFeed syndFeed = MyRssGenerator.entryList2SyndFeed(jsonentryList);
        MyRssGenerator.write(syndFeed, new File("./src/main/resources/static/index-all.rdf"));
    }

    protected static SyndFeed entryList2SyndFeed(List<JsonCityInfoEntry> jsonentryList) throws IOException {
        List<DisplayCityInfoEntry> dispEntryList = ThVarMapIndexBuilder.entryList2DispEntryList(jsonentryList);

        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_1.0");

        feed.setTitle("cityinfojp Feed Recently");
        feed.setLink("https://cityinfojp.herokuapp.com/");
        feed.setDescription("This feed is updated data entry info regarding cityinfojp.");

        final List<SyndEntry> entries = new ArrayList<>();

        for (int index = 0; index < jsonentryList.size(); index++) {
            final JsonCityInfoEntry jsonentry = jsonentryList.get(index);
            final DisplayCityInfoEntry dispEntry = dispEntryList.get(index);

            SyndEntry entry = new SyndEntryImpl();
            entry.setUri("rss" + index);
            entry.setTitle("[" + dispEntry.getIconText() + "] " + dispEntry.getTitleText());
            entry.setLink(dispEntry.getUrlList().get(0).getUrl());
            try {
                entry.setPublishedDate(new SimpleDateFormat("yyyy-MM-dd").parse(jsonentry.getEntryDate()));
            } catch (ParseException e) {
                throw new IOException(e);
            }

            SyndContent description = new SyndContentImpl();
            if (dispEntry.getUrlList().get(0).isFile() == false) {
                description.setType("text/plain");
            } else {
                description.setType("application/octet-stream");
            }
            description.setValue(dispEntry.getDescText());
            entry.setDescription(description);
            entries.add(entry);
        }

        feed.setEntries(entries);

        return feed;
    }

    protected static void write(SyndFeed feed, File fileOutput) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileOutput), "UTF-8"))) {
            write(feed, writer);
        }
    }

    protected static void write(SyndFeed feed, Writer writer) throws IOException {
        try {
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed, writer);
        } catch (FeedException ex) {
            throw new IOException(ex);
        }
    }
}
