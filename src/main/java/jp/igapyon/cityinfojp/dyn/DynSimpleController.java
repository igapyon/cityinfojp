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
package jp.igapyon.cityinfojp.dyn;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.igapyon.cityinfojp.dyn.fragment.JumbotronFragmentBean;

@Controller
public class DynSimpleController {
    @GetMapping({ "/dyn/about.html", "/dyn/link.html" })
    public String index(Model model, HttpServletRequest request) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        model.addAttribute("processDateTime", dtf.format(LocalDateTime.now()));

        model.addAttribute("jumbotron", getJumbotronBean(request.getRequestURI()));

        return getPathStringWithoutExt(request.getRequestURI());
    }

    public static String getPathStringWithoutExt(String requestURI) {
        if (requestURI.lastIndexOf('.') < 0) {
            return requestURI;
        }
        String body = requestURI.substring(0, requestURI.lastIndexOf('.'));
        return body;
    }

    public static JumbotronFragmentBean getJumbotronBean(String requestURI) {
        JumbotronFragmentBean jumbotron = new JumbotronFragmentBean();

        String body = getPathStringWithoutExt(requestURI);
        if (body.startsWith("/dyn/about")) {
            jumbotron.setTitle("About cityinfojp");
        } else if (body.startsWith("/dyn/link")) {
            jumbotron.setTitle("cityinfojp 関連リンク");
        }

        return jumbotron;
    }
}
