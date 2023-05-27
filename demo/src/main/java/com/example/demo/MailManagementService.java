package com.example.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MailManagementService {
    private MailCategorizer mailCategory;
    private DeadlineExtraction mailDeadline;
    private ExpirationCalculation mailExpiration;
    private List<MailManagementService.Mail> mails; // keeps track of scheduled mail to be deleted

    public MailManagementService() {
        this.mails = new ArrayList<>();
        mailCategory = new MailCategorizer();
        mailDeadline = new DeadlineExtraction();
        mailExpiration = new ExpirationCalculation();
    }

    public static void main(String[] args) throws Exception { // main method
        String mailSubject = "";
        String mailBody = "Please fill out the form by the deadline: 2023-06-30. Don't forget! The meeting is scheduled for 23-May, 2023.";

        MailManagementService service = new MailManagementService();
        service.processMail(mailSubject, mailBody);
        System.out.println(service.scheduleDeletion());
    }

    public void displayDetails(Mail mail) {
        System.out.println("Category : " + mail.getCategory());
        System.out.println("Deadline : " + mail.getDeadline());
        System.out.println("Expiration Date : " + mail.getExpirationDate());
    }

    public Mail processMail(String mailSubject, String mailBody) {
        String category = mailCategory.categorizeMail(mailSubject, mailBody);
        LocalDate deadline = mailDeadline.extractDeadline(mailBody);

        Mail mail = new Mail(category, deadline);
        LocalDate expirationDate = mailExpiration.setExpiration(mail);
        mail.updateExpirationDate(expirationDate);

        addMail(mail);
        displayDetails(mail);

        return mail;
    }

    public void addMail(MailManagementService.Mail mail) {
        mails.add(mail);
    }

    public String scheduleDeletion() {
        LocalDate currentDate = LocalDate.now();
        Iterator<MailManagementService.Mail> iterator = mails.iterator();

        while (iterator.hasNext()) {

            MailManagementService.Mail mail = iterator.next();
            LocalDate expirationDate = mail.getExpirationDate(); // return expirationDateTime

            // this should be done in mail deletion service
            if (expirationDate != null) {
                if (expirationDate.isBefore(currentDate)) {
                    iterator.remove(); // deletes mail (from mail list)
                    return "The mail has been deleted successfully.";

                } else if (expirationDate.isAfter(currentDate)) {
                    return "The mail is scheduled to be deleted on " + expirationDate + ".";
                }
            } else {
                return "This mail will not be deleted.";
            }
        }

        return "Can't be recognized.";
    }

    // Inner Mail class
    public static class Mail { // stores important info about a mail

        private String category;
        private Map<String, String> metadata;
        private LocalDate deadline;
        private LocalDate expirationDate;

        public Mail(String category, Map<String, String> metadata, LocalDate deadline) {
            this.category = category;
            this.metadata = metadata;
            this.deadline = deadline;
        }

        public Mail(String category, LocalDate deadline) {
            this.category = category;
            this.deadline = deadline;
        }

        public void updateExpirationDate(LocalDate expirationDate) {
            this.expirationDate = expirationDate;
        }

        public String getCategory() {
            return this.category;
        }

        public Map<String, String> getMetadata() {
            return this.metadata;
        }

        public LocalDate getExpirationDate() {
            return this.expirationDate;
        }

        public LocalDate getDeadline() {
            return this.deadline;
        }
    }

}
