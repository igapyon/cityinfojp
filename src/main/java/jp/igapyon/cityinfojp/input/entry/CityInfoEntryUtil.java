package jp.igapyon.cityinfojp.input.entry;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CityInfoEntryUtil {
    public static List<CityInfoEntry> readEntryList(File jsonInputFile) throws IOException {
        String jsonInput = FileUtils.readFileToString(jsonInputFile, "UTF-8");
        return readEntryList(jsonInput);
    }

    public static List<CityInfoEntry> readEntryList(String jsonInput) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.asList(mapper.readValue(jsonInput, CityInfoEntry[].class));
    }
}
