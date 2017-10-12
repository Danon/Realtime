package ui.gfx.resources;

import ui.gfx.Spritesheet;

import java.awt.image.BufferedImage;

public class Resources {
    private final static ResourceImages images = new ResourceImages();
    private final static ResourceSounds sounds = new ResourceSounds();
    public final static Spritesheet spritesheet = new CharacterSpritesheet().spritesheet;

    public static void load() {
        images.load();
    }

    public static BufferedImage getImageByName(String imageName) {
        return images.getImageByName(imageName);
    }

    public static void play(final String fileName) {
        sounds.play(fileName);
    }
}
