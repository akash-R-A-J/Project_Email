package com.example.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeadlineExtraction {

    public LocalDate extractDeadline(String mailBody) {
        // Define a regular expression pattern to match both deadline and general date
        // formats
        String datePattern = "\\b(?:deadline|due)\\s*:\\s*(\\d{4}-\\d{2}-\\d{2})\\b|" +
                "\\b(?:\\d{1,2}-(?:January|February|March|April|May|June|July|August|September|October|November|December)(?:,)? \\d{4}|\\d{4}-\\d{2}-\\d{2}|\\d{2}-\\d{2}-\\d{4})\\b";

        // Create a pattern object and matcher to find the date in the form body
        Pattern pattern = Pattern.compile(datePattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mailBody);
        LocalDate date = null; // Parse the extracted date string into a LocalDate object

        // Find the first occurrence of the date in the form body
        if (matcher.find()) {
            String dateString = matcher.group();

            // Check if the extracted date is in the deadline format or general date format
            if (dateString.toLowerCase().startsWith("deadline") || dateString.toLowerCase().startsWith("due")) {
                date = LocalDate.parse(dateString.substring(dateString.indexOf(":") + 1).trim());
                System.out.println("Extracted Deadline: " + date);
            } else {
                date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("d-MMM, uuuu"));
                System.out.println("Extracted Date: " + date);
            }
        } else {
            System.out.println("No date found in the form body.");
        }

        return date;
    }

    public static void main(String[] args) {
        String mailBody = "Please fill out the form by the deadline: 2023-06-30. Don't forget! The meeting is scheduled for 23-May, 2023.";
        DeadlineExtraction de = new DeadlineExtraction();
        de.extractDeadline(mailBody);
    }
}
