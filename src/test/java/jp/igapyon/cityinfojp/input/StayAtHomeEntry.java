package jp.igapyon.cityinfojp.input;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "firstName", "lastName", "address", "pets" })

/*
"": "2020-04-05",
"": "埼玉県",
"City": "-",
"StartDate": "-",
"EndDate": "2020-04-19",
"State": "要請",
"Target": "外出自粛",
"TargetRange": "週末",
"URL": "https://www.pref.saitama.lg.jp/a0701/message2020040202.html"
*/

public class StayAtHomeEntry {
    @JsonProperty("EntryDate")
    public String entryDate;

    @JsonProperty("Pref")
    public String pref;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
