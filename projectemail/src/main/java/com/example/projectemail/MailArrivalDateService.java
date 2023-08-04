package com.example.projectemail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MailArrivalDateService {
    private static final String[] DATE_PATTERNS = {
            "\\b\\d{1,2}[-/]\\d{1,2}[-/]\\d{2,4}\\b", // dd/MM/yyyy or dd-MM-yyyy or dd/MM/yy or dd-MM-yy
            "\\b\\d{1,2}[-/](?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[-/]\\d{2,4}\\b", // dd/MMM/yyyy or
                                                                                                 // dd-MMM-yyyy
            "\\b(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\.? \\d{1,2},? \\d{2,4}\\b", // MMM dd, yyyy or MMM
                                                                                                 // dd yyyy
            "\\b(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{1,2},? \\d{2,4}\\b" // MMM dd, yyyy
    };

    public LocalDate extractArrivalDate(String subject) {

        String pattern = String.join("|", DATE_PATTERNS);
        Pattern datePattern = Pattern.compile(pattern);
        Matcher matcher = datePattern.matcher(subject);

        if (matcher.find()) {
            String dateString = matcher.group();

            DateTimeFormatter[] formatters = {
                    DateTimeFormatter.ofPattern("MMM dd, yyyy"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                    DateTimeFormatter.ofPattern("dd/MM/yy"),
                    DateTimeFormatter.ofPattern("dd-MM-yy"),
                    DateTimeFormatter.ofPattern("MMM dd,yyyy"),
                    DateTimeFormatter.ofPattern("MMM-dd-yyyy"),
                    DateTimeFormatter.ofPattern("MMM dd-yyyy"),
                    DateTimeFormatter.ofPattern("MMM dd,yy"),
                    DateTimeFormatter.ofPattern("MMM-dd-yy"),
                    DateTimeFormatter.ofPattern("MMM dd-yy"),
                    DateTimeFormatter.ofPattern("dd MMM, yyyy"),
                    DateTimeFormatter.ofPattern("dd-MMM-yyyy"),
                    DateTimeFormatter.ofPattern("dd MMM yyyy"),
                    DateTimeFormatter.ofPattern("dd-MMM-yy"),
                    DateTimeFormatter.ofPattern("dd MMM yy"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ofPattern("yy/MM/dd"),
                    DateTimeFormatter.ofPattern("yy-MM-dd"),
                    DateTimeFormatter.ofPattern("dd/MMM/yyyy"),
                    DateTimeFormatter.ofPattern("dd-MMM-yyyy"),
                    // Add more patterns as needed
            };

            for (DateTimeFormatter formatter : formatters) {
                try {
                    LocalDate arrivalDate = LocalDate.parse(dateString, formatter);
                    return arrivalDate;
                } catch (DateTimeParseException e) {
                    // Date string does not match this formatter, try the next one
                    System.out.println("Exception caught in extractArrivalDate(): " + e.getMessage());
                }
            }
        }
        return null; // If no formatter matches the date string
    }

    public static void main(String[] args) {

        MailArrivalDateService service = new MailArrivalDateService();
        String subject = "Arrival Date: 25-Mar-2023";
        LocalDate arrivalDate = service.extractArrivalDate(subject);

        if (arrivalDate != null) {
            System.out.println("Arrival Date: " + arrivalDate);
        } else {
            System.out.println("No arrival date found in the subject.");
        }
    }
}
