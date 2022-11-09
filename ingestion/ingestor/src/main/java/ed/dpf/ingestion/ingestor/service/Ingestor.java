package ed.dpf.ingestion.ingestor.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ed.dpf.ingestion.ingestor.model.IngestorConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Ingestor {
    private IngestorConfiguration configuration;
    private Pattern regex;
    private List<String> patternGroups;

    public Ingestor(IngestorConfiguration configuration) {
        this.configuration = configuration;
        this.regex = Pattern.compile(configuration.getPattern());
        this.patternGroups = findPatternGroups(configuration.getPattern());
    }

    private List<String> findPatternGroups(String pattern) {
        Pattern groupPattern = Pattern.compile("\\(\\?\\<(\\w+)\\>[^)]*\\)");
        Matcher matcher = groupPattern.matcher(pattern);
        return matcher.results().map(m -> m.group(1)).collect(Collectors.toList());
    }

    public Map<String, Object> parse(String filename) {
        Map<String, Object> output = null;
        Matcher matcher = regex.matcher(filename);
        if(matcher.find()) {
            output = new HashMap<>();
            for(String group : patternGroups) {
                output.put(group, matcher.group(group));
            }
        }
        return output;
    }
}
