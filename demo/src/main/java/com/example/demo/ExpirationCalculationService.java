package com.example.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpirationCalculationService {
    public LocalDate expirationCalculation(MailManagementService.Mail mail) {

        String category = mail.getCategory();
        LocalDate deadline = mail.getDeadline();
        LocalDate extractedDate = mail.getMetadata();

        if (category == null || category.equals("Other")) {
            return null;
        }

        LocalDate expirationDate = null;

        if (category.equals("Meeting")) {

            try {
                if (deadline != null) {
                    expirationDate = deadline.plusDays(2); // Add 2 day to the meeting date
                } else if (extractedDate != null) {
                    expirationDate = extractedDate.plusMonths(1); // add 1 month to the meeting date
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (category.equals("Form") || category.equals("Expiration Mail")) {

            if (deadline != null) {
                try {
                    expirationDate = deadline.plusDays(1); // Add 1 day to the form deadline
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (mail.getArrivalDate() != null)
                    expirationDate = mail.getArrivalDate().plusMonths(6); // Add 6 month if no deadline is given to a
                else
                    expirationDate = LocalDate.now().plusYears(1); // form if arrival date is not mentioned
            }
        }

        System.out.println("Expiration date: " + expirationDate);

        return expirationDate;
    }

    public LocalDate extractDate(String subject) {
        String DATE_FORMAT = "yyyy-MM-dd";
        String patternString = "\\b\\d{4}-\\d{2}-\\d{2}\\b|\\b\\d{1,2}-(?:January|February|March|April|May|June|July|August|September|October|November|December)(?:,)? \\d{4}\\b";

        // String patternString =
        // "\\b\\d{4}-\\d{2}-\\d{2}\\b|\\b\\d{1,2}-(?:January|February|March|April|May|June|July|August|September|October|November|December)(?:,)?
        // \\d{4}\\b";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(subject);

        if (matcher.find()) {
            String dateString = matcher.group();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate extractedDate = LocalDate.parse(dateString, formatter);
            System.out.println("Extracted Date : " + extractedDate);
            return extractedDate;
        }

        return null;
    }
}
