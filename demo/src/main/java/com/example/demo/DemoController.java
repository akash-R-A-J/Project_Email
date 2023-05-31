package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemoController {

    @RequestMapping("/")
    public String index() {
        return "index"; // Returns the name of the HTML template (index.html)
    }

    // JAVA LOGIC SHOULD BE IMPLEMENTED IN THIS FUNCTION
    @PostMapping("/process")
    public String processMail(@RequestParam("emailHeader") String emailHeader,
            @RequestParam("emailBody") String emailBody, Model model) {

        // allocation of MailManagementService object and others too
        MailManagementService managementService = new MailManagementService();

        // Perform logic to extract metadata from emailBody
        MailManagementService.Mail mail = managementService.processMail(emailHeader,
                emailBody);

        String messageToUser = managementService.scheduleDeletion();

        // Add the extracted data to the model
        model.addAttribute("category", mail.getCategory());

        if (mail.getArrivalDate() != null) {
            model.addAttribute("arrivalDate", mail.getArrivalDate());
        } else {
            model.addAttribute("arrivalDate", "Not Found");
        }

        if (mail.getMetadata() != null) {
            model.addAttribute("extractedData", mail.getMetadata());
        } else {
            model.addAttribute("arrivalDate", "Not Found");
        }

        model.addAttribute("expirationDate", mail.getExpirationDate());
        model.addAttribute("messageToUser", messageToUser);

        return "result"; // Returns the name of the HTML template (result.html)
    }

    // ERROR CONTROLLER
    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ModelAndView handleException(Exception ex) {
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("errorMessage", "An unexpected error occurred.");
            return modelAndView;
        }
    }
}