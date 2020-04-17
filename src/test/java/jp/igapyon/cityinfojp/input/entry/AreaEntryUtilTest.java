package jp.igapyon.cityinfojp.input.entry;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

class AreaEntryUtilTest {

    @Test
    void test() throws IOException {
        List<AreaEntry> entryList = AreaEntryUtil.readEntryListFromClasspath();
        for (AreaEntry entry : entryList) {
            System.err.println(entry.getName() + ":" + entry.getNameen());
            for (String look : entry.getPref()) {
                System.err.println("  " + look);
            }
        }
    }
}
