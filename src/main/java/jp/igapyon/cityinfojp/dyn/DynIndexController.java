package jp.igapyon.cityinfojp.dyn;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DynIndexController {
    @GetMapping({ "/dyn", "/dyn/", "/dyn/index.html" })
    public String index(Model model) {
        return "dyn/index";
    }

}
