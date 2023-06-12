package com.patterns.behavioral.observer;
public class Demo {
    public static void main(String[] args) {
        Editor editor = new Editor();

        editor.eventManager.subscribe("open", new EmailNotificationListener("dhyego.nieto@gmail.com"));
        editor.eventManager.subscribe("save", new LogOpenListener("./log.txt"));

        try {
            editor.openFile("test.txt");
            editor.saveFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
