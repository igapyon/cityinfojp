package jp.igapyon.cityinfojp.input;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.igapyon.cityinfojp.input.stayathome.StayAtHomeEntry;
import jp.igapyon.cityinfojp.input.stayathome.StayAtHomeEntryUtil;

class StayAtHomeTest {
    @Test
    void contextLoads() throws Exception {
        File jsonInputFile = new File(
                "./src/main/resources/static/input/2020/202004/stayathome-saitama-20200405a.json");

        List<StayAtHomeEntry> entryList = StayAtHomeEntryUtil.readEntryList(jsonInputFile);
        for (StayAtHomeEntry entry : entryList) {
            System.err.println(entry.getState());
        }
    }
}
