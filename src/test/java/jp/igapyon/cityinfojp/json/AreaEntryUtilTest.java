package jp.igapyon.cityinfojp.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.igapyon.cityinfojp.dyn.DisplayAreaEntry;
import jp.igapyon.cityinfojp.dyn.DisplayPrefEntry;
import jp.igapyon.cityinfojp.json.JsonAreaEntry;
import jp.igapyon.cityinfojp.json.JsonAreaEntryUtil;
import jp.igapyon.cityinfojp.json.JsonPrefEntry;
import jp.igapyon.cityinfojp.json.JsonPrefEntryUtil;

class AreaEntryUtilTest {

    @Test
    void test() throws IOException {
        List<DisplayAreaEntry> dispAreaList = new ArrayList<>();

        List<JsonAreaEntry> areaList = JsonAreaEntryUtil.readEntryListFromClasspath();
        List<JsonPrefEntry> prefList = JsonPrefEntryUtil.readEntryListFromClasspath();
        for (JsonAreaEntry entry : areaList) {
            DisplayAreaEntry dispArea = new DisplayAreaEntry();
            dispAreaList.add(dispArea);
            dispArea.setName(entry.getName());
            dispArea.setNameen(entry.getNameen());

            // System.err.println(entry.getName() + ":" + entry.getNameen());
            for (String look : entry.getPref()) {
                for (JsonPrefEntry prefLookup : prefList) {
                    if (look.equals(prefLookup.getCode())) {
                        DisplayPrefEntry newPref = new DisplayPrefEntry();
                        dispArea.getPrefList().add(newPref);
                        newPref.setText(prefLookup.getName());
                        newPref.setUrl("/pref/" + prefLookup.getNameen().toLowerCase());
                    }
                }
                // System.err.println("  " + look);
            }
        }
    }
}
