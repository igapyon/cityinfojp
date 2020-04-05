package jp.igapyon.cityinfojp.input;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class StayAtHomeTest {
    @Test
    void contextLoads() throws Exception {
        String val = FileUtils.readFileToString(
                new File("./src/main/resources/static/input/stayathome/2020/stayathome-saitama-20200405a.json"),
                "UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        List<StayAtHomeEntry> entryList = Arrays.asList(mapper.readValue(val, StayAtHomeEntry[].class));
        for (StayAtHomeEntry entry : entryList) {
            System.err.println(entry.getState());
        }
    }
}
