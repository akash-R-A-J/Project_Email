package com.example.demo;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SecureMailCategorizer {
    private static final Logger LOGGER = Logger.getLogger(MailCategorizer.class.getName());

    public String categorizeMail(String mailSubject, String mailBody) {
        String[] formKeywords = { "form", "survey", "questionnaire", "feedback", "response", "application",
                "enrollment", "registration" };
        String[] meetingKeywords = { "meeting", "appointment", "conference", "call", "schedule", "agenda", "invite",
                "arrange" };
        String[] expirationKeywords = { "limited time offer", "expires on", "time-sensitive", "act now", "valid until",
                "offer expires", "renewal reminder", "last chance", "urgent" };
        String[] otherKeywords = { "announcement", "invitation", "notification", "update", "news", "reminder",
                "thank you", "confirmation" };

        try {
            for (String keyword : formKeywords) {
                if (mailSubject.toLowerCase().contains(keyword) || mailBody.toLowerCase().contains(keyword)) {
                    return "Form Mail";
                }
            }

            for (String keyword : meetingKeywords) {
                if (mailSubject.toLowerCase().contains(keyword) || mailBody.toLowerCase().contains(keyword)) {
                    return "Meeting Mail";
                }
            }

            for (String keyword : expirationKeywords) {
                if (mailSubject.toLowerCase().contains(keyword) || mailBody.toLowerCase().contains(keyword)) {
                    return "Expiration Mail";
                }
            }

            for (String keyword : otherKeywords) {
                if (mailSubject.toLowerCase().contains(keyword) || mailBody.toLowerCase().contains(keyword)) {
                    return "Other";
                }
            }

            return "Other";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error occurred during mail categorization: " + e.getMessage(), e);
            return "Error";
        }
    }

    // Example usage:
    public static void main(String[] args) {
        String subject = "Limited Time Offer - Expires Soon!";
        String body = "Act now to avail of this exclusive limited-time offer.";
        MailCategorizer m = new MailCategorizer();
        String mailCategory = m.categorizeMail(subject, body);
        System.out.println("Mail Category: " + mailCategory);
    }
}
