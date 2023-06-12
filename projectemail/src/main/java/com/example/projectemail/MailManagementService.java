package com.example.projectemail;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class MailManagementService {
    // neccessary object of services used
    private MailCategorizationService _mailCategorizationService;
    private MailArrivalDateService _mailArrivalDateService;
    private DeadlineExtractionService _deadlineExtractionService;
    private ExpirationCalculationService _expirationCalculationService;
    private static List<Mail> mails = new ArrayList<>();

    // initializes all object
    public MailManagementService() {
        _mailCategorizationService = new MailCategorizationService();
        _mailArrivalDateService = new MailArrivalDateService();
        _deadlineExtractionService = new DeadlineExtractionService();
        _expirationCalculationService = new ExpirationCalculationService();
    }

    // function to get all types of information about a mail and store it to Mail
    // object
    public MailManagementService.Mail getEmailInformation(String subject, String body) {
        String category = _mailCategorizationService.categorizeEmail(subject, body);
        // do not proceed from here if category : others
        Mail mail = new Mail();
        mail.setCategory(category);
        mail.setArrivalDate(_mailArrivalDateService.extractArrivalDate(subject));
        mail.setDeadline(_deadlineExtractionService.extractDeadline(subject));
        mail.setExtractedDate(_mailArrivalDateService.extractArrivalDate(body));
        mail.setExpirationDate(_expirationCalculationService.expirationCalculation(mail));

        // LocalDate arrivalDate = _mailArrivalDateService.extractArrivalDate(subject);
        // LocalDate deadline = _deadlineExtractionService.extractDeadline(subject);
        // LocalDate extractedDate = _mailArrivalDateService.extractArrivalDate(body);
        // LocalDate expirationDate =
        // _expirationCalculationService.expirationCalculation(mail);

        addMail(mail);
        displayInformation(mail);

        return mail;
    }

    public String scheduleDeletion(MailManagementService.Mail mail) {

        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = mail.getExpirationDate();

        if (expirationDate != null) {
            if (expirationDate.isBefore(currentDate)) {
                mails.remove(mail);
                return "Mail has been deleted successfully.";
            } else {
                return "Mail is scheduled to be deleted on " + expirationDate.toString();
            }
        } else {
            return "No expiration date is found.";
        }
    }

    // adds mail to the mail list
    public void addMail(MailManagementService.Mail mail) {
        mails.add(mail);
    }

    public class Mail {
        private String category;
        private LocalDate arrivalDate;
        private LocalDate deadline;
        private LocalDate extractedDate;
        private LocalDate expirationDate;

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCategory() {
            return this.category;
        }

        public void setArrivalDate(LocalDate arrivalDate) {
            this.arrivalDate = arrivalDate;
        }

        public LocalDate getArrivalDate() {
            return this.arrivalDate;
        }

        public void setDeadline(LocalDate deadline) {
            this.deadline = deadline;
        }

        public LocalDate getDeadline() {
            return this.deadline;
        }

        public void setExtractedDate(LocalDate extractedDate) {
            this.extractedDate = extractedDate;
        }

        public LocalDate getExtractedDate() {
            return this.extractedDate;
        }

        public void setExpirationDate(LocalDate expirationDate) {
            this.expirationDate = expirationDate;
        }

        public LocalDate getExpirationDate() {
            return this.expirationDate;
        }
    }

    public void displayInformation(Mail mail) {
        System.out.println("Category : " + mail.getCategory());
        System.out.println("Arrival Date : " + mail.getArrivalDate());
        System.out.println("Deadline : " + mail.getDeadline());
        System.out.println("Extracted Date : " + mail.getExtractedDate());
        System.out.println("ExpirationDate : " + mail.getExpirationDate());
    }
}
