package jp.igapyon.cityinfojp.input.stayathome;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StayAtHomeEntryUtil {
    public static List<StayAtHomeEntry> readEntryList(File jsonInputFile) throws IOException {
        String jsonInput = FileUtils.readFileToString(jsonInputFile, "UTF-8");
        return readEntryList(jsonInput);
    }

    public static List<StayAtHomeEntry> readEntryList(String jsonInput) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.asList(mapper.readValue(jsonInput, StayAtHomeEntry[].class));
    }
}
