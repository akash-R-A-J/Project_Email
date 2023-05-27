package com.example.demo;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;

import java.io.IOException;
import java.util.List;

public class GmailFormSubmissionDateExtractor {

    public static String extractSubmissionDate(String messageId, Gmail service) throws IOException {
        Message message = service.users().messages().get("me", messageId).execute();

        List<MessagePartHeader> headers = message.getPayload().getHeaders();
        for (MessagePartHeader header : headers) {
            if (header.getName().equalsIgnoreCase("Date")) {
                String submissionDate = header.getValue();
                System.out.println("Submission Date: " + submissionDate);
                break;
            }
        }

        return submissionDate;
    }

    public static void main(String[] args) throws IOException {
        // Initialize the Gmail service
        Gmail service = // Initialize and authenticate the Gmail service

        // Replace messageId with the ID of the email containing the form submission
        String messageId = "replace-with-message-id";

        extractSubmissionDate(messageId, service);
    }
}
