package jp.igapyon.cityinfojp.input.entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.igapyon.cityinfojp.dyn.DisplayAreaEntry;
import jp.igapyon.cityinfojp.dyn.DisplayPrefEntry;

class AreaEntryUtilTest {

    @Test
    void test() throws IOException {
        List<DisplayAreaEntry> dispAreaList = new ArrayList<>();

        List<AreaEntry> areaList = AreaEntryUtil.readEntryListFromClasspath();
        List<PrefEntry> prefList = PrefEntryUtil.readEntryListFromClasspath();
        for (AreaEntry entry : areaList) {
            DisplayAreaEntry dispArea = new DisplayAreaEntry();
            dispAreaList.add(dispArea);
            dispArea.setName(entry.getName());
            dispArea.setNameen(entry.getNameen());

            System.err.println(entry.getName() + ":" + entry.getNameen());
            for (String look : entry.getPref()) {
                for (PrefEntry prefLookup : prefList) {
                    if (look.equals(prefLookup.getCode())) {
                        DisplayPrefEntry newPref = new DisplayPrefEntry();
                        dispArea.getPrefList().add(newPref);
                        newPref.setText(prefLookup.getName());
                        newPref.setUrl("/pref/" + prefLookup.getNameen().toLowerCase());
                    }
                }
                System.err.println("  " + look);
            }
        }
    }
}
