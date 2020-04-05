package jp.igapyon.cityinfojp.input;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.igapyon.cityinfojp.input.entry.PrefEntry;
import jp.igapyon.cityinfojp.input.entry.PrefEntryUtil;

class PrefEntryTest {
    @Test
    void contextLoads() throws Exception {
        {
            List<PrefEntry> entryList = PrefEntryUtil
                    .readEntryList(new File("./src/main/resources/static/input/prefjp.json"));
            for (PrefEntry entry : entryList) {
                System.err.println(entry.getCode() + ":" + entry.getName() + ":"
                        + (entry.getNameen() == null ? "" : entry.getNameen()));
            }
        }
    }
}
