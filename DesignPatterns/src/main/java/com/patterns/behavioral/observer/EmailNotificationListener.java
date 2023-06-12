package com.patterns.behavioral.observer;
import java.io.File;

public class EmailNotificationListener implements EventListener {

    private String email;

    public EmailNotificationListener(String email) {
        this.email = email;
    }
    @Override
    public void update(String eventType, File file) {
        System.out.println("Sending email to: " + email);
    }
}
