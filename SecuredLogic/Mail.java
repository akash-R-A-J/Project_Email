package com.example.demo;

import java.time.LocalDate;

public class Mail {
    private String category;
    private LocalDate deadline;
    private LocalDate expirationDate;

    public Mail(String category, LocalDate deadline) {
        this.category = category;
        this.deadline = deadline;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}
