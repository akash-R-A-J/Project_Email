package com.example.demo;

public class MailCategorizer {
    public String categorizeMail(String mailSubject, String mailBody) {
        String[] formKeywords = { "form", "survey", "questionnaire", "feedback", "response", "application",
                "enrollment", "registration" };
        String[] meetingKeywords = { "meeting", "appointment", "conference", "call", "schedule", "agenda", "invite",
                "arrange" };
        String[] expirationKeywords = { "limited time offer", "expires on", "time-sensitive", "act now", "valid until",
                "offer expires", "renewal reminder", "last chance", "urgent" };
        String[] otherKeywords = { "announcement", "invitation", "notification", "update", "news", "reminder",
                "thank you", "confirmation" };

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
