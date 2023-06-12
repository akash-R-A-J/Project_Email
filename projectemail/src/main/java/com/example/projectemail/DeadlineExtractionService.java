package com.example.projectemail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeadlineExtractionService {
    private static final String[] DEADLINE_KEYWORDS = { "deadline", "due date", "last date", "submission date" };
    private static final String[] DATE_PATTERNS = {
            "\\b\\d{1,2}[-/]\\d{1,2}[-/]\\d{2,4}\\b", // dd/MM/yyyy or dd-MM-yyyy or dd/MM/yy or dd-MM-yy
            "\\b\\d{1,2}[-/](?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[-/]\\d{2,4}\\b", // dd/MMM/yyyy or
                                                                                                 // dd-MMM-yyyy
            "\\b(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\.? \\d{1,2},? \\d{2,4}\\b" // MMM dd, yyyy or
                                                                                                // MMM dd yyyy
    };

    public LocalDate extractDeadline(String emailBody) {

        LocalDate deadline = null;

        // Search for the deadline pattern in the email body
        for (String pattern : DATE_PATTERNS) {
            Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = regex.matcher(emailBody);

            if (matcher.find()) {
                String dateString = matcher.group();
                deadline = parseDateString(dateString);

                // Check if the identified date is a valid deadline based on context or keywords
                if (isDeadlineContextValid(emailBody, dateString) && isKeywordPresent(emailBody)) {
                    break;
                }
            }
        }

        return deadline;
    }

    private LocalDate parseDateString(String dateString) {
        LocalDate deadline = null;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
            deadline = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            // Handle parsing errors if needed
            System.out.println("Error in parsing date: " + e.getMessage());
        }

        return deadline;
    }

    private boolean isDeadlineContextValid(String emailBody, String dateString) {
        // Check if the keyword "deadline" or "due date" is present before the date in
        // the email body
        boolean isKeywordPresent = false;

        for (String keyword : DEADLINE_KEYWORDS) {
            String pattern = "\\b" + keyword + "\\b.*" + dateString;
            Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = regex.matcher(emailBody);

            if (matcher.find()) {
                isKeywordPresent = true;
                break;
            }
        }

        return isKeywordPresent;
    }

    private boolean isKeywordPresent(String emailBody) {
        // Check if any of the deadline keywords are present in the email body
        for (String keyword : DEADLINE_KEYWORDS) {
            if (emailBody.toLowerCase().contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
