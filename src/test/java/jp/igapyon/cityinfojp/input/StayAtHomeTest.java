package jp.igapyon.cityinfojp.input;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

class StayAtHomeTest {
    @Test
    void contextLoads() throws Exception {
        String val = FileUtils.readFileToString(
                new File("./src/main/resources/static/input/stayathome/2020/stayathome-saitama-20200405a.json"),
                "UTF-8");

        System.err.println(val);
        /*
        ObjectMapper mapper = new ObjectMapper();
        Model model = mapper.readValue(json, Model.class);
        */
    }
}
