package ui.gfx.resources;

import ui.gfx.resources.duality.FileHandler;
import ui.gfx.resources.duality.FileIterator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResourceImages {
    private final FileIterator iterator = new FileIterator("res/images");
    private final Map<String, BufferedImage> images = new HashMap<>();

    void load() {
        iterator.iterate(name -> images.put(new File(name).getName(), loadImage(name)));
    }

    private BufferedImage loadImage(String name) {
        try {
            return ImageIO.read(new FileHandler(name).getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    BufferedImage getImageByName(String imageName) {
        if (images.containsKey(imageName)) {
            return images.get(imageName);
        }
        throw new RuntimeException(String.format("No such file '%s' found", imageName));
    }
}
