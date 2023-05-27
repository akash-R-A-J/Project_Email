import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Controller
    public static class DemoController {

        private final MailManagementService mailManagementService;

        public DemoController(MailManagementService mailManagementService) {
            this.mailManagementService = mailManagementService;
        }

        @RequestMapping("/")
        public String index() {
            return "index";
        }

        @PostMapping("/process")
        public String processForm(@RequestParam("emailBody") String emailBody, Model model) {
            MailManagementService.Mail mail = mailManagementService.processMail("", emailBody);
            String messageToUser = mailManagementService.scheduleDeletion();

            model.addAttribute("extractedData", mail.getDeadline());
            model.addAttribute("category", mail.getCategory());
            model.addAttribute("expirationDate", mail.getExpirationDate());
            model.addAttribute("messageToUser", messageToUser);

            return "result";
        }
    }

    public static class MailManagementService {

        private final MailCategorizer mailCategorizer;
        private final DeadlineExtraction deadlineExtraction;
        private final ExpirationCalculation expirationCalculation;
        private final List<Mail> mails;

        public MailManagementService(MailCategorizer mailCategorizer,
                                     DeadlineExtraction deadlineExtraction,
                                     ExpirationCalculation expirationCalculation) {
            this.mails = new ArrayList<>();
            this.mailCategorizer = mailCategorizer;
            this.deadlineExtraction = deadlineExtraction;
            this.expirationCalculation = expirationCalculation;
        }

        public Mail processMail(String mailSubject, String mailBody) {
            String category = mailCategorizer.categorizeMail(mailSubject, mailBody);
            LocalDate deadline = deadlineExtraction.extractDeadline(mailBody);

            Mail mail = new Mail(category, deadline);
            LocalDate expirationDate = expirationCalculation.setExpiration(mail);
            mail.updateExpirationDate(expirationDate);

            addMail(mail);
            displayDetails(mail);

            return mail;
        }

        public void addMail(Mail mail) {
            mails.add(mail);
        }

        public String scheduleDeletion() {
            LocalDate currentDate = LocalDate.now();
            Iterator<Mail> iterator = mails.iterator();

            while (iterator.hasNext()) {
                Mail mail = iterator.next();
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

            return "Can't be recognized.";
        }

        public void displayDetails(Mail mail) {
            System.out.println("Category : " + mail.getCategory());
            System.out.println("Deadline : " + mail.getDeadline());
            System.out.println("Expiration Date : " + mail.getExpirationDate());
        }

        public static class Mail {
            private final String category;
            private final LocalDate deadline;
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

            public LocalDate getDeadline() {
                return this.deadline;
            }

            public LocalDate getExpirationDate() {
                return this.expirationDate;
            }
        }
    }

    public static class MailCategorizer {

        private static final String[] formKeywords = {"form", "survey", "questionnaire", "feedback", "response",
                "application", "enrollment", "registration"};
        private static final String[] meetingKeywords = {"meeting", "appointment", "conference", "call", "schedule",
                "agenda", "invite", "arrange"};
        private static final String[] expirationKeywords = {"limited time offer", "expires on", "time-sensitive",
                "act now", "valid until", "offer expires", "renewal reminder", "last chance", "urgent"};
        private static final String[] otherKeywords = {"announcement", "invitation", "notification", "update", "news",
                "reminder", "thank you", "confirmation"};

        public String categorizeMail(String mailSubject, String mailBody) {
            for (String keyword : formKeywords) {
                if (containsKeyword(mailSubject, mailBody, keyword)) {
                    return "Form Mail";
                }
            }

            for (String keyword : meetingKeywords) {
                if (containsKeyword(mailSubject, mailBody, keyword)) {
                    return "Meeting Mail";
                }
            }

            for (String keyword : expirationKeywords) {
                if (containsKeyword(mailSubject, mailBody, keyword)) {
                    return "Expiration Mail";
                }
            }

            for (String keyword : otherKeywords) {
                if (containsKeyword(mailSubject, mailBody, keyword)) {
                    return "Other";
                }
            }

            return "Other";
        }

        private boolean containsKeyword(String mailSubject, String mailBody, String keyword) {
            String lowercaseMailSubject = mailSubject.toLowerCase();
            String lowercaseMailBody = mailBody.toLowerCase();
            return lowercaseMailSubject.contains(keyword) || lowercaseMailBody.contains(keyword);
        }
    }

    public static class ExpirationCalculation {

        public LocalDate setExpiration(MailManagementService.Mail mail) {
            String category = mail.getCategory();
            LocalDate expirationDate = null;
            LocalDate deadline = mail.getDeadline();

            if (category == null || category.equals("Other") || (category.equals("Meeting Mail") && deadline == null)) {
                return expirationDate;
            }

            if (category.equals("Meeting Mail")) {
                try {
                    expirationDate = deadline.plusDays(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (category.equals("Form Mail") || category.equals("Expiration Mail")) {
                if (deadline != null) {
                    try {
                        expirationDate = deadline.plusDays(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    expirationDate = LocalDate.now().plusMonths(6);
                }
            }

            return expirationDate;
        }
    }

    public static class DeadlineExtraction {

        public LocalDate extractDeadline(String mailBody) {
            String datePattern = "\\b(?:deadline|due)\\s*:\\s*(\\d{4}-\\d{2}-\\d{2})\\b|" +
                    "\\b(?:\\d{1,2}-(?:January|February|March|April|May|June|July|August|September|October|November|December)(?:,)? \\d{4}|\\d{4}-\\d{2}-\\d{2}|\\d{2}-\\d{2}-\\d{4})\\b";

            Pattern pattern = Pattern.compile(datePattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(mailBody);
            LocalDate date = null;

            if (matcher.find()) {
                String dateString = matcher.group();

                if (dateString.toLowerCase().startsWith("deadline") || dateString.toLowerCase().startsWith("due")) {
                    date = LocalDate.parse(dateString.substring(dateString.indexOf(":") + 1).trim());
                    System.out.println("Extracted Deadline: " + date);
                } else {
                    date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("d-MMM, uuuu"));
                    System.out.println("Extracted Date: " + date);
                }
            } else {
                System.out.println("No date found in the form body.");
            }

            return date;
        }
    }
}
