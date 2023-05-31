package com.example.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpirationCalculationService {
    public LocalDate expirationCalculation(MailManagementService.Mail mail) {

        String category = mail.getCategory();
        LocalDate expirationDate = null;
        LocalDate deadline = mail.getDeadline();

        if (category == null || category.equals("Other") || (category.equals("Meeting Mail") && deadline == null)) {
            return expirationDate;
        }

        if (category.equals("Meeting Mail")) {

            try {
                expirationDate = deadline.plusDays(2); // Add 2 day to the meeting date
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (category.equals("Form Mail") || category.equals("Expiration Mail")) {

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

        return expirationDate;
    }

    public LocalDate extractArrivalDate(String header) {
        String DATE_FORMAT = "yyyy-MM-dd";
        String patternString = "\\s*(\\d{4}-\\d{2}-\\d{2})\\b|" +
                "\\b(?:\\d{1,2}-(?:January|February|March|April|May|June|July|August|September|October|November|December)(?:,)? \\d{4}|\\d{4}-\\d{2}-\\d{2}|\\d{2}-\\d{2}-\\d{4})\\b";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(header);
        LocalDate arrivalDate = null;

        if (matcher.find()) {
            String dateString = matcher.group(1);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            arrivalDate = LocalDate.parse(dateString, formatter);

        }

        return arrivalDate;
    }
}
