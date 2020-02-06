package ui.gfx.resources;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class ResourceSounds {
    public void play(final String fileName) {
        String path = "res/sounds/";
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path + fileName));
            clip.open(inputStream);
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            System.out.println("play sound error: " + e.getMessage() + " for " + fileName);
        }
    }
}
