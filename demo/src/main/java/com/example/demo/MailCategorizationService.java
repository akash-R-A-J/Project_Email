package com.example.demo;

import java.util.Arrays;

public class MailCategorizationService {
    // added some keywords
    private static final String[] FORM_KEYWORDS = { "form", "response", "deadline", "today is the last day" };
    private static final String[] MEETING_KEYWORDS = { "meeting", "appointment", "conference", "call", "schedule",
            "agenda", "invite", "arrange" };
    private static final String[] EXPIRATION_KEYWORDS = { "limited time offer", "expires on", "time-sensitive",
            "act now", "valid until", "offer expires", "renewal reminder", "last chance", "urgent" };

    // convert all keywords to lowercase if needed
    private static final String[] FORM_KEYWORDS_LOWER = Arrays.stream(FORM_KEYWORDS).map(String::toLowerCase)
            .toArray(String[]::new);
    private static final String[] MEETING_KEYWORDS_LOWER = Arrays.stream(MEETING_KEYWORDS).map(String::toLowerCase)
            .toArray(String[]::new);
    private static final String[] EXPIRATION_KEYWORDS_LOWER = Arrays.stream(EXPIRATION_KEYWORDS)
            .map(String::toLowerCase).toArray(String[]::new);

    public String categorizeMail(String mailSubject, String mailBody) {

        // Convert mailSubject and mailBody to lowercase
        String subjectLower = mailSubject.toLowerCase();
        String bodyLower = mailBody.toLowerCase();

        for (String keyword : FORM_KEYWORDS_LOWER) {
            if (subjectLower.toLowerCase().contains(keyword) || bodyLower.toLowerCase().contains(keyword)) {
                return "Form Mail";
            }
        }

        for (String keyword : MEETING_KEYWORDS_LOWER) {
            if (subjectLower.toLowerCase().contains(keyword) || bodyLower.toLowerCase().contains(keyword)) {
                return "Meeting Mail";
            }
        }

        for (String keyword : EXPIRATION_KEYWORDS_LOWER) {
            if (subjectLower.toLowerCase().contains(keyword) || bodyLower.toLowerCase().contains(keyword)) {
                return "Expiration Mail";
            }
        }

        return "Other";
    }

    // Example usage:
    public static void main(String[] args) {
        String subject = "Limited Time Offer - Expires Soon!";
        String body = "Act now to avail of this exclusive limited-time offer.";
        MailCategorizationService m = new MailCategorizationService();
        String mailCategory = m.categorizeMail(subject, body);
        System.out.println("Mail Category: " + mailCategory);
    }
}
