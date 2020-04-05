package jp.igapyon.cityinfojp.input;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.igapyon.cityinfojp.input.entry.CityInfoEntry;
import jp.igapyon.cityinfojp.input.entry.CityInfoEntryUtil;

class CityInfoEntryTest {
    @Test
    void contextLoads() throws Exception {
        {
            List<CityInfoEntry> entryList = CityInfoEntryUtil.readEntryList(
                    new File("./src/main/resources/static/input/2020/202004/saitama-stayathome-20200405a.json"));
            for (CityInfoEntry entry : entryList) {
                System.err.println(entry.getState());
                for (String look : entry.getURL()) {
                    System.err.println("  " + look);
                }
            }
        }
        {
            List<CityInfoEntry> entryList = CityInfoEntryUtil.readEntryList(
                    new File("./src/main/resources/static/input/2020/202004/chiba-school-20200405a.json"));
            for (CityInfoEntry entry : entryList) {
                System.err.println(entry.getState());
                for (String look : entry.getURL()) {
                    System.err.println("  " + look);
                }
            }
        }
    }
}
