package com.example.demo;

import java.time.LocalDate;

public class ExpirationCalculation {
    public LocalDate setExpiration(MailManagementService.Mail mail) throws Exception {
        String category = mail.getCategory();
        LocalDate expirationDate = null;
        LocalDate deadline = mail.getDeadline();

        if (category == null || category.equals("Other") || (category.equals("Meeting Mail") && deadline == null)) {
            return expirationDate;
        }

        if (category.equals("Meeting Mail")) {
            if (deadline != null) {
                expirationDate = deadline.plusDays(2);
            } else {
                throw new Exception("No deadline provided for Meeting Mail.");
            }
        } else if (category.equals("Form Mail") || category.equals("Expiration Mail")) {
            if (deadline != null) {
                expirationDate = deadline.plusDays(1);
            } else {
                expirationDate = LocalDate.now().plusMonths(6);
            }
        }

        return expirationDate;
    }
}
