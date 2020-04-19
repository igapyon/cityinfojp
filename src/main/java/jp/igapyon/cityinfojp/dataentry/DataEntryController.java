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
package jp.igapyon.cityinfojp.dataentry;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jp.igapyon.cityinfojp.fragment.jumbotron.JumbotronFragmentBean;
import jp.igapyon.cityinfojp.fragment.navbar.NavbarBean;
import jp.igapyon.cityinfojp.json.JsonCityInfoEntry;
import jp.igapyon.cityinfojp.json.JsonPrefEntry;
import jp.igapyon.cityinfojp.json.JsonPrefEntryUtil;
import jp.igapyon.cityinfojp.thvarmap.ThNavbarUtil;

/**
 * データエントリ画面(/dataentry.html)のコントローラ。
 * 
 * @author Toshiki Iga
 */
@Controller
public class DataEntryController {
    /**
     * データエントリ /dataentry.html への GET リクエストを処理します。
     * 
     * こちらは通常系の入り口としていったん入る場所です。
     * 
     * @param model モデル。
     * @param form フォーム。
     * @param result バインド結果。
     * @return 次画面名。
     * @throws IOException 入出力例外が発生した場合。
     */
    @RequestMapping(value = { "/dataentry.html" }, method = { RequestMethod.GET })
    public String dataentryhtml(Model model, DataEntryForm form, BindingResult result) throws IOException {
        return dataentry(model, form, result);
    }

    /**
     * データエントリ /dataentry への GET/POST リクエストを処理します。
     * 
     * こちらはポストした後に何度も到達することを許容する入り口でしたが、現状は /dataentry.html で完結するようになっています。
     * 
     * @param model モデル。
     * @param form フォーム。
     * @param result バインド結果。
     * @return 次画面名。
     * @throws IOException 入出力例外が発生した場合。
     */
    @RequestMapping(value = { "/dataentry" }, params = { "update" }, method = { RequestMethod.GET, RequestMethod.POST })
    public String dataentry(Model model, DataEntryForm form, BindingResult result) throws IOException {

        model.addAttribute("jumbotron", getJumbotronBean());

        model.addAttribute("navbar", getNavbarBean());

        model.addAttribute("dataentry", form);

        try {
            List<JsonPrefEntry> prefList = JsonPrefEntryUtil.readEntryListFromClasspath();
            model.addAttribute("prefList", prefList);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (form.getPref() != null && form.getPref().trim().length() > 0) {
            JsonCityInfoEntry entry = form2Entry(form);
            List<JsonCityInfoEntry> entryList = new ArrayList<>();
            entryList.add(entry);
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String resultJson = mapper.writeValueAsString(entryList);
            form.setResultJson(resultJson);
        }

        return "dataentry";
    }

    static JsonCityInfoEntry form2Entry(DataEntryForm form) {
        JsonCityInfoEntry entry = new JsonCityInfoEntry();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        entry.setEntryDate(dtf.format(LocalDateTime.now()));
        entry.setPref(form.getPref());
        entry.setCity(form.getCity());
        entry.setStartDate(form.getStartDate());
        entry.setEndDate(form.getEndDate());
        entry.setState(form.getState());
        entry.setTarget(form.getTarget());
        entry.setTargetRange(form.getTargetRange());
        entry.setReason(form.getReason());
        entry.getURL().add(form.getUrl1());
        if (form.getUrl2() != null && form.getUrl2().trim().length() > 0) {
            entry.getURL().add(form.getUrl2());
        }

        return entry;
    }

    /**
     * データエントリの結果を JSON 形式でダウンロードするリクエストを処理します。
     *
     * @param model モデル。
     * @param form フォーム。
     * @param response HTTP レスポンス。
     * @return 必ず null を戻します。
     * @throws IOException 入出力例外が発生した場合。
     */
    @RequestMapping(value = "/dataentry", params = "download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String dataentryDownload(Model model, DataEntryForm form, HttpServletResponse response) throws IOException {
        byte[] resultData = new byte[0];

        if (form.getPref() != null && form.getPref().trim().length() > 0) {
            JsonCityInfoEntry entry = form2Entry(form);
            List<JsonCityInfoEntry> entryList = new ArrayList<>();
            entryList.add(entry);
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String resultJson = mapper.writeValueAsString(entryList);
            resultData = resultJson.getBytes("UTF-8");
        }

        // 指示 
        // instruct
        //
        // 宣言
        // declare
        //
        // 要請
        // request
        //
        // 休校 (閉鎖)
        // closure

        String state = "other";
        if ("指示".equals(form.getState())) {
            state = "instruct";
        } else if ("宣言".equals(form.getState())) {
            state = "declare";
        } else if ("要請".equals(form.getState())) {
            state = "request";
        } else if ("休校".equals(form.getState()) || "閉鎖".equals(form.getState())) {
            state = "closure";
        }

        String prefString = "99-dummy";
        List<JsonPrefEntry> prefList = JsonPrefEntryUtil.readEntryListFromClasspath();
        for (JsonPrefEntry pref : prefList) {
            if (pref.getName().equals(form.getPref())) {
                prefString = pref.getCode() + "-" + pref.getNameen().toLowerCase();
                break;
            }
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        String filename = prefString + "-" + state + "-" + dtf.format(LocalDateTime.now()) + "a.json";

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setContentLength(resultData.length);

        ByteArrayInputStream inStream = new ByteArrayInputStream(resultData);
        OutputStream outStream = new BufferedOutputStream(response.getOutputStream());
        StreamUtils.copy(inStream, outStream);
        outStream.flush();

        return null;
    }

    /**
     * 画面描画用の Jumbotron フラグメント Bean オブジェクトを生成します。
     * 
     * @return Jumbotron を表示するための情報。
     */
    public static JumbotronFragmentBean getJumbotronBean() {
        JumbotronFragmentBean jumbotron = new JumbotronFragmentBean();
        jumbotron.setTitle("Data Entry");
        return jumbotron;
    }

    /**
     * 画面描画用の Navbar Bean オブジェクトを生成します。
     * 
     * @return Navbar Bean の情報。
     */
    public static NavbarBean getNavbarBean() {
        NavbarBean navbar = ThNavbarUtil.buildNavbar(null);
        navbar.getItemList().get(3).setCurrent(true);
        return navbar;
    }
}
