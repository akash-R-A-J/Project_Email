package com.example.projectemail;

import java.time.LocalDate;

public class ExpirationCalculationService {
    public LocalDate expirationCalculation(MailManagementService.Mail mail) {

        String category = mail.getCategory();
        LocalDate deadline = mail.getDeadline();
        LocalDate arrivalDate = mail.getArrivalDate();
        LocalDate extractedDate = mail.getExtractedDate();

        LocalDate expirationDate = null;

        if (category.equals("Meeting")) {

            try {
                if (deadline != null) {
                    expirationDate = deadline.plusDays(2); // Add 2 day to the meeting date
                } else if (extractedDate != null) {
                    expirationDate = extractedDate.plusMonths(4); // add 4 month to the meeting date
                } else if (arrivalDate != null) {
                    expirationDate = arrivalDate.plusMonths(5); // add 5 month to the meeting date
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
                if (arrivalDate != null)
                    expirationDate = arrivalDate.plusMonths(6); // Add 6 month to arrival date if no deadline is given
                else if (extractedDate != null) {
                    expirationDate = extractedDate.plusMonths(3); // add 4 month to the meeting date
                } else
                    expirationDate = LocalDate.now().plusYears(1); // form if arrival date is not mentioned
            }
        } else if (category.equals("OTP")) {
            if (arrivalDate != null)
                expirationDate = arrivalDate.plusDays(15); // add 15 days to arrival date
        }

        return expirationDate;
    }
}
