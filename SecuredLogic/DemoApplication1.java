package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication1 {
    public static void main(String[] args) {
        try {
            String mailSubject = "";
            String mailBody = "Please fill out the form by the deadline: 2023-06-30. Don't forget! The meeting is scheduled for 23-May, 2023.";

            MailManagementService service = new MailManagementService();
            service.processMail(mailSubject, mailBody);
            System.out.println(service.scheduleDeletion());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
