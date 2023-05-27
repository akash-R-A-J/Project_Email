package com.example.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeadlineExtraction {

    public LocalDate extractDeadline(String mailBody) throws Exception {
        String datePattern = "\\b(?:deadline|due)\\s*:\\s*(\\d{4}-\\d{2}-\\d{2})\\b|" +
                "\\b(?:\\d{1,2}-(?:January|February|March|April|May|June|July|August|September|October|November|December)(?:,)? \\d{4}|\\d{4}-\\d{2}-\\d{2}|\\d{2}-\\d{2}-\\d{4})\\b";

        Pattern pattern = Pattern.compile(datePattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mailBody);
        LocalDate date = null;

        if (matcher.find()) {
            String dateString = matcher.group();
            if (dateString.toLowerCase().startsWith("deadline") || dateString.toLowerCase().startsWith("due")) {
                date = LocalDate.parse(dateString.substring(dateString.indexOf(":") + 1).trim());
                System.out.println("Extracted Deadline: " + date);
            } else {
                date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("d-MMM, uuuu"));
                System.out.println("Extracted Date: " + date);
            }
        } else {
            throw new Exception("No date found in the mail body.");
        }

        return date;
    }
}
