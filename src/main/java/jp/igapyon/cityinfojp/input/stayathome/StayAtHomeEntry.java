package jp.igapyon.cityinfojp.input.stayathome;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * http://www.jsonschema2pojo.org/ をもちいて生成。
 * 
 * @author Toshiki Iga
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "EntryDate", "Pref", "City", "StartDate", "EndDate", "State", "Target", "TargetRange", "URL" })
public class StayAtHomeEntry {
    @JsonProperty("EntryDate")
    private String entryDate;
    @JsonProperty("Pref")
    private String pref;
    @JsonProperty("City")
    private String city;
    @JsonProperty("StartDate")
    private String startDate;
    @JsonProperty("EndDate")
    private String endDate;
    @JsonProperty("State")
    private String state;
    @JsonProperty("Target")
    private String target;
    @JsonProperty("TargetRange")
    private String targetRange;
    @JsonProperty("URL")
    private List<String> url;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("EntryDate")
    public String getEntryDate() {
        return entryDate;
    }

    @JsonProperty("EntryDate")
    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    @JsonProperty("Pref")
    public String getPref() {
        return pref;
    }

    @JsonProperty("Pref")
    public void setPref(String pref) {
        this.pref = pref;
    }

    @JsonProperty("City")
    public String getCity() {
        return city;
    }

    @JsonProperty("City")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("StartDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("StartDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("EndDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("EndDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("State")
    public String getState() {
        return state;
    }

    @JsonProperty("State")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("Target")
    public String getTarget() {
        return target;
    }

    @JsonProperty("Target")
    public void setTarget(String target) {
        this.target = target;
    }

    @JsonProperty("TargetRange")
    public String getTargetRange() {
        return targetRange;
    }

    @JsonProperty("TargetRange")
    public void setTargetRange(String targetRange) {
        this.targetRange = targetRange;
    }

    @JsonProperty("URL")
    public List<String> getURL() {
        return url;
    }

    @JsonProperty("URL")
    public void setURL(List<String> url) {
        this.url = url;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
