package com.example.projectemail;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProjectemailController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/process-email")
    public String processEmail(@RequestParam("emailSubject") String emailSubject,
            @RequestParam("emailBody") String emailBody,
            Model model) {

        // Apply your Java logic to process the email subject and body
        // Extract data, schedule deletion, etc.
        // You can use the emailSubject and emailBody parameters here
        MailManagementService service = new MailManagementService();
        MailManagementService.Mail mail = service.getEmailInformation(emailSubject, emailBody);
        String messageToUser = service.scheduleDeletion(mail);

        // Add appropiate attribute to the model
        model.addAttribute("category", mail.getCategory());
        model.addAttribute("arrivalDate", mail.getArrivalDate());
        model.addAttribute("messageToUser", messageToUser);

        if (mail.getCategory() == "Others") {
            model.addAttribute("extractedDate", "Not Required");
            model.addAttribute("expirationDate", "Not Required");
        } else if (mail.getCategory() == "OTP") {
            model.addAttribute("extractedDate", "Not Required");
            if (mail.getExpirationDate() != null)
                model.addAttribute("expirationDate", mail.getExpirationDate());
            else
                model.addAttribute("expirationDate", "Not Found");
        } else {
            if (mail.getExtractedDate() != null)
                model.addAttribute("extractedDate", mail.getExtractedDate());
            else
                model.addAttribute("extractedDate", "Not Found");
            if (mail.getExpirationDate() != null)
                model.addAttribute("expirationDate", mail.getExpirationDate());
            else
                model.addAttribute("expirationDate", "Not Found");
        }

        return "result";
    }
}
