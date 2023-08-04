package com.example.projectemail;

public class MailCategorizationService {
    // form, meeting, otp, expiration mail, other
    public String categorizeEmail(String subject, String body) {
        try {
            if (isFormEmail(subject, body)) {
                return "Form";
            } else if (isOTPEmail(subject, body)) {
                return "OTP";
            } else if (isMeetingEmail(subject, body)) {
                return "Meeting";
            } else if (isExpirationEmail(subject, body)) {
                return "Expiration Mail";
            } else {
                return "Other";
            }
        } catch (Exception e) {
            throw new RuntimeException("Error categorizing email: " + e.getMessage());
        }
    }

    private boolean isFormEmail(String subject, String body) {
        String[] formKeywords = { "form", "survey", "questionnaire" };

        for (String keyword : formKeywords) {
            if (subject.toLowerCase().contains(keyword) || body.toLowerCase().contains(keyword)) {
                System.out.println(keyword);
                return true;
            }
        }

        return false;
    }

    private boolean isOTPEmail(String subject, String body) {
        String[] otpKeywords = { "otp", "verification code" };

        for (String keyword : otpKeywords) {
            if (subject.toLowerCase().contains(keyword) || body.toLowerCase().contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    private boolean isMeetingEmail(String subject, String body) {
        String[] meetingKeywords = { "meeting", "appointment" };

        for (String keyword : meetingKeywords) {
            if (subject.toLowerCase().contains(keyword) || body.toLowerCase().contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    // organize -> workshop
    private boolean isExpirationEmail(String subject, String body) {
        String[] expirationKeywords = { "expiration", "expiry", "due date", "valid until" };

        for (String keyword : expirationKeywords) {
            if (subject.toLowerCase().contains(keyword) || body.toLowerCase().contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    // Example handler for exceptions
    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<String> handleException(Exception e) {
    // // Handle the exception and return an appropriate response
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error
    // occurred.");
    // }

    public static void main(String[] args) {
        try {
            String emailSubject = "Meeting Reminder";
            String emailBody = "Hello, this is a meeting reminder for tomorrow.";

            MailCategorizationService service = new MailCategorizationService();
            String category = service.categorizeEmail(emailSubject, emailBody);

            if (category.equals("Form")) {
                System.out.println("Email categorized as Form.");
            } else if (category.equals("OTP")) {
                System.out.println("Email categorized as OTP.");
            } else if (category.equals("Meeting")) {
                System.out.println("Email categorized as Meeting.");
            } else if (category.equals("Expiration")) {
                System.out.println("Email categorized as Expiration Mail.");
            } else {
                System.out.println("Email categorized as Other.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

}
