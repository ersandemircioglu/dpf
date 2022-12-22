package ed.dpf.ingestion.ingestor.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ed.dpf.ingestion.ingestor.model.IngestorConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilenameIngestor implements Ingestor {

    private static Pattern GROUP_PATTERN = Pattern.compile("\\(\\?\\<(\\w+)\\>[^)]*\\)");

    private IngestorConfiguration configuration;

    private Pattern pattern;
    private List<String> groupNames;
    private Map<String, FieldToValueParser> fieldToValueParsers;

    public FilenameIngestor(IngestorConfiguration configuration) {
        this.configuration = configuration;
        this.pattern = Pattern.compile(this.configuration.getRegex());
        this.groupNames = findGroups(this.configuration.getRegex());
        this.fieldToValueParsers = new HashMap<>();
        this.configuration.getFieldToValueConfMap().entrySet()
                .forEach(item -> fieldToValueParsers.put(item.getKey(), new FieldToValueParser(item.getValue())));
    }

    private List<String> findGroups(String pattern) {
        Matcher matcher = GROUP_PATTERN.matcher(pattern);
        return matcher.results().map(m -> m.group(1)).collect(Collectors.toList());
    }

    @Override
    public boolean matches(File file) {
        String filename = file.getName();
        return pattern.matcher(filename).matches();
    }

    @Override
    public Map<String, Object> parse(File file) {
        String filename = file.getName();
        Map<String, Object> output = new HashMap<>();
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            output.put("filename", filename);
            output.put("type", configuration.getName());
            for (String group : groupNames) {
                Object value = null;
                if (fieldToValueParsers.containsKey(group)) {
                    value = fieldToValueParsers.get(group).parse(matcher.group(group));
                } else {
                    value = matcher.group(group);
                }
                output.put(group, value);
            }
        }
        return output;
    }
}
