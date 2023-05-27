package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// import java.util.Map;
// import java.time.LocalDate;

@Controller
public class DemoController {

    @RequestMapping("/")
    public String index() {
        return "index"; // Returns the name of the HTML template (index.html)
    }

    // JAVA LOGIC SHOULD BE IMPLEMENTED IN THIS FUNCTION
    @PostMapping("/process")
    public String processForm(@RequestParam("emailBody") String emailBody, Model model) {
        // call the start function of MailManagementService to implement the logic
        MailManagementService managementService = new MailManagementService();
        // Perform logic to extract metadata from emailBody
        MailManagementService.Mail mail = managementService.processMail("", emailBody);

        String messageToUser = managementService.scheduleDeletion();

        // Add the extracted data to the model
        model.addAttribute("extractedData", mail.getDeadline());
        model.addAttribute("category", mail.getCategory());
        model.addAttribute("expirationDate", mail.getExpirationDate());
        model.addAttribute("messageToUser", messageToUser);

        return "result"; // Returns the name of the HTML template (result.html)
    }
}