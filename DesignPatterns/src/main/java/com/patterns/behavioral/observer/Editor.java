package com.patterns.behavioral.observer;
import java.io.File;
import java.util.HashMap;

public class Editor {
    public EventManager eventManager;
    private File file;

    public Editor() {
        this.eventManager = new EventManager("open", "save");
    }

    public void openFile(String pathFile) {
        this.file = new File(pathFile);
        eventManager.notify("open", file);
    }

    public void saveFile() throws Exception {
        if (this.file != null) {
            eventManager.notify("save", file);
        } else {
            throw new Exception("Please open a file first.");
        }
    }
}
