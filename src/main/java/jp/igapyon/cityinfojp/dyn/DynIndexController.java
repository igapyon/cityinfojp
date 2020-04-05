package jp.igapyon.cityinfojp.dyn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.igapyon.cityinfojp.input.entry.CityInfoEntry;

@Controller
public class DynIndexController {
    @GetMapping({ "/dyn", "/dyn/", "/dyn/index.html" })
    public String index(Model model) throws IOException {

        List<CityInfoEntry> recentEntryList = new ArrayList<CityInfoEntry>();
        /*
        customers.add(new Customer(1 , "Miura", "Kazuyoshi"));
        customers.add(new Customer(2 , "Kitazawa", "Tsuyoshi"));
        customers.add(new Customer(3 , "Hashiratani", "Tetsuji"));
        */

        // 「src/main/resources/hoge.csv」を読み込む
        try (InputStream is = new ClassPathResource("static/input/2020/202004/saitama-stayathome-20200405a.json")
                .getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            StringBuffer buf = new StringBuffer();
            char[] copyBuf = new char[8192];
            for (;;) {
                int length = br.read(copyBuf);
                if (length <= 0) {
                    break;
                }
                buf.append(new String(copyBuf, 0, length));
            }
            System.err.println(buf.toString());
        }

        model.addAttribute("recentEntryList", recentEntryList);

        return "dyn/index";
    }

}
