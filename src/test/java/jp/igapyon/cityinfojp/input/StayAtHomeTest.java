package jp.igapyon.cityinfojp.input;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.igapyon.cityinfojp.input.stayathome.StayAtHomeEntry;
import jp.igapyon.cityinfojp.input.stayathome.StayAtHomeEntryUtil;

class StayAtHomeTest {
    @Test
    void contextLoads() throws Exception {
        {
            List<StayAtHomeEntry> entryList = StayAtHomeEntryUtil.readEntryList(
                    new File("./src/main/resources/static/input/2020/202004/saitama-stayathome-20200405a.json"));
            for (StayAtHomeEntry entry : entryList) {
                System.err.println(entry.getState());
            }
        }
        {
            List<StayAtHomeEntry> entryList = StayAtHomeEntryUtil.readEntryList(
                    new File("./src/main/resources/static/input/2020/202004/chiba-school-20200405a.json"));
            for (StayAtHomeEntry entry : entryList) {
                System.err.println(entry.getState());
            }
        }
    }
}
