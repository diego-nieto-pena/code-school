package com.patterns.behavioral.observer;
import java.io.File;

public class LogOpenListener implements EventListener {

    private File log;

    public LogOpenListener(String fileName) {
        this.log = new File(fileName);
    }

    @Override
    public void update(String eventType, File file) {
        System.out.println("Save log to file: " + file.getName());
    }
}
