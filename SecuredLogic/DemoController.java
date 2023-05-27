package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DemoController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/process")
    public String processForm(@RequestParam("emailBody") String emailBody, Model model) {
        try {
            MailManagementService managementService = new MailManagementService();
            MailManagementService.Mail mail = managementService.processMail("", emailBody);
            String messageToUser = managementService.scheduleDeletion();

            model.addAttribute("extractedData", mail.getDeadline());
            model.addAttribute("category", mail.getCategory());
            model.addAttribute("expirationDate", mail.getExpirationDate());
            model.addAttribute("messageToUser", messageToUser);
            
            // Handle success and return the appropriate view
            return "result";
        } catch (Exception e) {
            // Handle the exception and return an appropriate error message or redirect to an error page
            model.addAttribute("errorMessage", "An error occurred during processing. Please try again.");
            return "error";
        }
    }
}
