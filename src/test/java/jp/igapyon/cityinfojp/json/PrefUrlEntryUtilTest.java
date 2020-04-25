package jp.igapyon.cityinfojp.json;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.igapyon.cityinfojp.json.JsonPrefUrlEntry;
import jp.igapyon.cityinfojp.json.JsonPrefUrlEntryUtil;

class PrefUrlEntryUtilTest {

    @Test
    void test() throws IOException {
        List<JsonPrefUrlEntry> entryList = JsonPrefUrlEntryUtil.readEntryListFromClasspath();
        for (JsonPrefUrlEntry entry : entryList) {
            // System.err.println(entry.getName() + ":" + entry.getUrl().get(0));
        }
    }
}
