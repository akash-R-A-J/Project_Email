package com.example.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MailManagementService {
    private MailCategorizationService mailCategory;
    private DeadlineExtractionService mailDeadline;
    private ExpirationCalculationService mailExpiration;
    private List<MailManagementService.Mail> mails; // keeps track of scheduled
    // mail to be deleted

    public MailManagementService() {
        this.mails = new ArrayList<>();
        mailCategory = new MailCategorizationService();
        mailDeadline = new DeadlineExtractionService();
        mailExpiration = new ExpirationCalculationService();
    }

    public static void main(String[] args) throws Exception { // main method
        String mailSubject = "";
        String mailBody = "Please fill out the form by the deadline: 2023-06-30." +
                "Don't forget! The meeting is scheduled for 23-May, 2023.";

        MailManagementService service = new MailManagementService();
        service.processMail(mailSubject, mailBody);
        System.out.println(service.scheduleDeletion());
    }

    public void displayDetails(Mail mail) {
        System.out.println("Category : " + mail.getCategory());
        System.out.println("Flag : " + mail.getFlag());
        System.out.println("Arrival Date : " + mail.getArrivalDate());
        System.out.println("Extracted Date : " + mail.getMetadata());
        System.out.println("Deadline : " + mail.getDeadline()); // exact deadline from mail body
        System.out.println("Expiration Date : " + mail.getExpirationDate());
    }

    // process only one mail
    public Mail processMail(String mailSubject, String mailBody) {
        String category = mailCategory.categorizeMail(mailSubject, mailBody); // category
        LocalDate deadline = mailDeadline.extractDeadline(mailBody); // deadline

        Mail mail = new Mail(category, deadline);

        mail.setMetadata(mailExpiration.extractDate(mailBody)); // extract date from body
        mail.setArrivalDate(mailExpiration.extractDate(mailSubject)); // extract arrival date from mail subject
        mail.setExpirationDate(mailExpiration.expirationCalculation(mail)); // expiration date

        // set flag to check if the current mail is already processed or not
        if (deadline != null) {
            mail.setFlag(1);
        } else if (category == "other") {
            mail.setFlag(2);
        }

        addMail(mail); // add mail to the mail list

        displayDetails(mail); // display mail details

        return mail;
    }

    public void addMail(MailManagementService.Mail mail) {
        mails.add(mail);
    }

    public String scheduleDeletion() {

        LocalDate currentDate = LocalDate.now();
        Iterator<MailManagementService.Mail> iterator = mails.iterator();

        while (iterator.hasNext()) {

            LocalDate expirationDate = iterator.next().getExpirationDate();

            // this should be done in mail deletion service
            if (expirationDate != null) {
                if (expirationDate.isBefore(currentDate)) {
                    iterator.remove(); // deletes mail (from mail list)
                    System.out.println("The mail has been deleted successfully.");
                    return "The mail has been deleted successfully.";

                } else if (expirationDate.isAfter(currentDate)) {
                    System.out.println("The mail is scheduled to be deleted on " + expirationDate + ".");
                    return "The mail is scheduled to be deleted on " + expirationDate + ".";
                }
            } else {
                System.out.println("This mail will not be deleted.");
                return "This mail will not be deleted.";
            }
        }

        return "Can't be recognized.";
    }

    // Inner Mail class
    public static class Mail { // stores important info about a mail

        private String category;
        private LocalDate deadline;
        private LocalDate expirationDate;
        private LocalDate arrivalDate;
        private LocalDate extractedDate;
        private int flag; // a mail should passed only once
        // initiate = 0, deadline = 1, others = 2;

        public Mail(String category, LocalDate deadline) {
            this.category = category;
            this.deadline = deadline;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return this.flag;
        }

        public String getCategory() {
            return this.category;
        }

        public LocalDate getDeadline() {
            return this.deadline;
        }

        public void setMetadata(LocalDate extractedDate) {
            this.extractedDate = extractedDate;
        }

        public LocalDate getMetadata() {
            return this.extractedDate;
        }

        public void setArrivalDate(LocalDate arrivalDate) {
            this.arrivalDate = arrivalDate;
        }

        public LocalDate getArrivalDate() {
            return this.arrivalDate;
        }

        public void setExpirationDate(LocalDate expirationDate) {
            this.expirationDate = expirationDate;
        }

        public LocalDate getExpirationDate() {
            return this.expirationDate;
        }
    }

}