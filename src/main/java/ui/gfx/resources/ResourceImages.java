package ui.gfx.resources;

import lombok.SneakyThrows;
import ui.gfx.resources.duality.FileHandler;
import ui.gfx.resources.duality.ResourceIterator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static javax.imageio.ImageIO.read;

public class ResourceImages {
    private final Map<String, BufferedImage> images = new HashMap<>();

    void load() {
        new ResourceIterator("images").iterate(name -> images.put(new File(name).getName(), loadImage(name)));
    }

    @SneakyThrows
    private BufferedImage loadImage(String name) {
        return read(new FileHandler(name).getInputStream());
    }

    BufferedImage getImageByName(String imageName) {
        if (images.containsKey(imageName)) {
            return images.get(imageName);
        }
        throw new RuntimeException(String.format("No such file '%s' found", imageName));
    }
}
