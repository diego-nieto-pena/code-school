package com.patterns.structural.facade;
import java.io.File;

public class Demo {
    public static void main(String[] args) {
        VideoConversionFacade converter = new VideoConversionFacade();
        final File mp4Video = converter.convertVideo("youtubevideo.ogg", "mp4");

    }
}
