package com.example.demo;

import java.time.LocalDate;

public class ExpirationCalculation {
    public LocalDate setExpiration(MailManagementService.Mail mail) {

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
                expirationDate = LocalDate.now().plusMonths(6); // Add 6 month if no deadline is given to a form
            }
        }

        return expirationDate;
    }
}
