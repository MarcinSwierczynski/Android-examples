package net.swierczynski.android_examples.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterResults {
    private List<TwitterEntry> results;

    public List<TwitterEntry> getResults() {
        return results;
    }

    public void setResults(List<TwitterEntry> results) {
        this.results = results;
    }
}
