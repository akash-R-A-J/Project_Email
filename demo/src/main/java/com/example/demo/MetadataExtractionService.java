package com.example.demo; 

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// WORK : extract metadata from email body
// Input : email body
// Output : metadata 

public class MetadataExtractionService {
    private Map<String, String> patternMapping; // <keys, values>
    // private Map<String, String> metadata; // stores metadata <keys, values>

    public MetadataExtractionService() { // default constructors

        Map<String, String> patternMapping = new HashMap<>();
        patternMapping.put("Deadline", "Deadline: (\\d{4}-\\d{2}-\\d{2})"); // for date
        patternMapping.put("Meeting", "Meeting: (.+)");
        patternMapping.put("Promotion", "Promotion: (.+)");
        // patternMapping.put("Time", "Time: (\\d{2}:\\d{2})");// for time

        this.patternMapping = patternMapping;
    }

    public void display() {
        System.out.println("\nMetadata-Extraction-Service");
        System.out.println(this.patternMapping);
    }

    public Map<String, String> extractMetadata(String emailBody) {
        Map<String, String> metadata = new HashMap<>(); // contains keys and values extracted from emailBody

        for (Map.Entry<String, String> entry : patternMapping.entrySet()) { // ??? Map.Entry<>, .entrySet()
            String key = entry.getKey(); // key
            String pattern = entry.getValue(); // value
            Pattern regexPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = regexPattern.matcher(emailBody);
            if (matcher.find()) {
                String value = matcher.group(1);
                metadata.put(key, value);
            }
        }

        return metadata;
    }

    // Example usage
    public static void main(String[] args) {

        MetadataExtractionService metadataService = new MetadataExtractionService();
        String emailBody = "Dear Team,\n\nPlease be informed that the deadline for Project XYZ is approaching. " +
                "Make sure to complete your tasks by the following deadline: 2023-05-31.\n\nRegards,\nJohn Smith";

        Map<String, String> metadata = metadataService.extractMetadata(emailBody);
        System.out.println(metadata);
    }
}
