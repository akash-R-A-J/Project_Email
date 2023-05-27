package com.example.demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MailManagementService {
    private MailCategorizer mailCategory;
    private DeadlineExtraction mailDeadline;
    private ExpirationCalculation mailExpiration;
    private List<MailManagementService.Mail> mails;

    public MailManagementService() {
        this.mails = new ArrayList<>();
        mailCategory = new MailCategorizer();
        mailDeadline = new DeadlineExtraction();
        mailExpiration = new ExpirationCalculation();
    }

    public void displayDetails(Mail mail) {
        System.out.println("Category: " + mail.getCategory());
        System.out.println("Deadline: " + mail.getDeadline());
        System.out.println("Expiration Date: " + mail.getExpirationDate());
    }

    public Mail processMail(String mailSubject, String mailBody) throws Exception {
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
            LocalDate expirationDate = mail.getExpirationDate();

            if (expirationDate != null) {
                if (expirationDate.isBefore(currentDate)) {
                    iterator.remove();
                    return "The mail has been deleted successfully.";
                } else if (expirationDate.isAfter(currentDate)) {
                    return "The mail is scheduled to be deleted on " + expirationDate + ".";
                }
            } else {
                return "This mail will not be deleted.";
            }
        }

        return "Cannot be recognized.";
    }

    public static class Mail {
        private String category;
        private LocalDate deadline;
        private LocalDate expirationDate;

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

        public LocalDate getExpirationDate() {
            return this.expirationDate;
        }

        public LocalDate getDeadline() {
            return this.deadline;
        }
    }
}
