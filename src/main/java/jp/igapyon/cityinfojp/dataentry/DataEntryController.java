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

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jp.igapyon.cityinfojp.dyn.NavbarUtil;
import jp.igapyon.cityinfojp.dyn.fragment.JumbotronFragmentBean;
import jp.igapyon.cityinfojp.dyn.fragment.navbar.NavbarBean;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntry;

@Controller
public class DataEntryController {

    @RequestMapping(value = { "/dataentry" }, method = { RequestMethod.GET, RequestMethod.POST })
    public String index(Model model, DataEntryForm form, BindingResult result) throws IOException {

        model.addAttribute("jumbotron", getJumbotronBean());

        model.addAttribute("navbar", getNavbarBean());

        model.addAttribute("dataentry", form);

        form.setResultJson("USOUSO");

        if (form.getPref() != null && form.getPref().trim().length() > 0) {
            CityInfoEntry entry = new CityInfoEntry();
            entry.setEntryDate("2000-01-01");
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

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String resultJson = mapper.writeValueAsString(entry);
            form.setResultJson(resultJson);
        }

        return "dataentry";
    }

    public static JumbotronFragmentBean getJumbotronBean() {
        JumbotronFragmentBean jumbotron = new JumbotronFragmentBean();
        jumbotron.setTitle("Data Entry");
        return jumbotron;
    }

    public static NavbarBean getNavbarBean() {
        NavbarBean navbar = NavbarUtil.buildNavbar();
        navbar.getItemList().get(3).setCurrent(true);
        return navbar;
    }
}
