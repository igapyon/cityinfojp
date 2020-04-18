package jp.igapyon.cityinfojp.input.entry;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

class PrefUrlEntryUtilTest {

    @Test
    void test() throws IOException {
        List<PrefUrlEntry> entryList = PrefUrlEntryUtil.readEntryListFromClasspath();
        for (PrefUrlEntry entry : entryList) {
            // System.err.println(entry.getName() + ":" + entry.getUrl().get(0));
        }
    }
}
