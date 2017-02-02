package ui.gfx;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class Resources {
    private final static HashMap<String, BufferedImage> images = new HashMap<>();

    public static void load() {
        File[] listOfFiles = new File("res/images").listFiles();

        for (File imageFile : listOfFiles) {
            if (imageFile.isFile()) {
                try {
                    images.put(imageFile.getName(), ImageIO.read(imageFile.getAbsoluteFile()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static BufferedImage getImageByName(String imageName) {
        if (images.containsKey(imageName)) {
            return images.get(imageName);
        }
        throw new IllegalAccessError(String.format("No such file %s found", imageName));
    }

    public static void play(final String fileName) {
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

    static Spritesheet spritesheet = new Spritesheet("sprites.png",
            new FrameAnimation("materialization", FrameAnimation.FrameAnimationSpeed.Materialization,
                    new Frame(0, 6, 5, 9, 48, 4),
                    new Frame(1, 23, 24, 22, 29, 11),
                    new Frame(2, 51, 11, 30, 42, 15),
                    new Frame(3, 88, 14, 30, 39, 15),
                    new Frame(4, 124, 17, 30, 36, 15),
                    new Frame(5, 160, 19, 30, 34, 15),
                    new Frame(6, 197, 21, 30, 32, 15)),
            new FrameAnimation("idle", FrameAnimation.FrameAnimationSpeed.Idle,
                    new Frame(0, 233, 19, 30, 34, 15),
                    new Frame(1, 269, 19, 30, 34, 15),
                    new Frame(2, 305, 19, 30, 34, 15)),
            new FrameAnimation("midair", FrameAnimation.FrameAnimationSpeed.MidAir,
                    new Frame(0, 175, 71, 24, 37, 12),
                    new Frame(1, 207, 67, 17, 41, 11),
                    new Frame(2, 234, 62, 19, 46, 12),
                    new Frame(3, 260, 67, 23, 41, 14),
                    new Frame(4, 290, 66, 27, 42, 18),
                    new Frame(5, 326, 70, 24, 38, 12),
                    new Frame(6, 355, 76, 30, 32, 17)),
            new FrameAnimation("midair-gun", FrameAnimation.FrameAnimationSpeed.MidAirGun,
                    new Frame(0, 399, 73, 34, 37, 12),
                    new Frame(1, 438, 69, 31, 41, 11),
                    new Frame(2, 475, 64, 32, 46, 14),
                    new Frame(3, 511, 69, 36, 41, 14),
                    new Frame(4, 551, 68, 36, 42, 14),
                    new Frame(6, 594, 72, 35, 38, 12),
                    new Frame(7, 634, 78, 41, 32, 16)),
            new FrameAnimation("run-start", FrameAnimation.FrameAnimationSpeed.RunStart,
                    new Frame(0, 10, 118, 30, 34, 15)),
            new FrameAnimation("run", FrameAnimation.FrameAnimationSpeed.Run,
                    new Frame(0, 45, 118, 20, 34, 9),
                    new Frame(1, 69, 117, 23, 35, 12),
                    new Frame(2, 95, 118, 32, 34, 17),
                    new Frame(3, 131, 119, 34, 33, 18),
                    new Frame(4, 170, 119, 26, 33, 14),
                    new Frame(5, 202, 118, 22, 34, 11),
                    new Frame(6, 229, 117, 25, 35, 12),
                    new Frame(7, 259, 118, 30, 34, 14),
                    new Frame(8, 292, 119, 34, 33, 15),
                    new Frame(9, 330, 119, 29, 33, 13)),
            new FrameAnimation("run-gun", FrameAnimation.FrameAnimationSpeed.RunGun,
                    new Frame(0, 10, 167, 34, 34, 9),
                    new Frame(1, 48, 166, 37, 35, 12),
                    new Frame(2, 89, 167, 40, 34, 17),
                    new Frame(3, 132, 168, 43, 33, 18),
                    new Frame(4, 180, 168, 39, 33, 14),
                    new Frame(5, 223, 167, 36, 34, 11),
                    new Frame(6, 263, 166, 38, 35, 12),
                    new Frame(7, 305, 167, 40, 34, 14),
                    new Frame(8, 349, 168, 42, 33, 15),
                    new Frame(9, 395, 168, 40, 33, 13)),
            new FrameAnimation("pushed", FrameAnimation.FrameAnimationSpeed.Pushed,
                    new Frame(0, 456, 167, 22, 36, 6),
                    new Frame(1, 482, 166, 25, 36, 8)),
            new FrameAnimation("shooting", FrameAnimation.FrameAnimationSpeed.Shooting,
                    new Frame(0, 5, 217, 32, 36, 19),
                    new Frame(1, 43, 217, 31, 36, 19),
                    new Frame(2, 79, 221, 36, 32, 16),
                    new Frame(3, 119, 221, 41, 32, 16),
                    new Frame(4, 164, 219, 28, 34, 16),
                    new Frame(5, 199, 221, 32, 32, 16),
                    new Frame(6, 234, 221, 29, 32, 16),
                    new Frame(7, 268, 221, 37, 32, 18),
                    new Frame(8, 308, 221, 43, 32, 18)),
            new FrameAnimation("basic", FrameAnimation.FrameAnimationSpeed.Basic,
                    new Frame(0, 4, 291, 30, 34, 18),
                    new Frame(1, 40, 292, 31, 33, 19),
                    new Frame(2, 76, 292, 31, 33, 19),
                    new Frame(3, 110, 287, 39, 38, 27),
                    new Frame(4, 151, 290, 38, 35, 26),
                    new Frame(5, 193, 270, 42, 55, 25),
                    new Frame(6, 239, 272, 63, 53, 18),
                    new Frame(7, 308, 292, 47, 33, 19),
                    new Frame(8, 357, 293, 47, 32, 19),
                    new Frame(9, 407, 293, 32, 32, 19),
                    new Frame(10, 443, 292, 32, 33, 19)),
            new FrameAnimation("basic-air", FrameAnimation.FrameAnimationSpeed.BasicAir,
                    new Frame(0, 5, 349, 28, 44, 17, 9),
                    new Frame(1, 39, 350, 25, 39, 13, 13),
                    new Frame(2, 69, 350, 30, 44, 19, 8),
                    new Frame(3, 107, 349, 37, 49, 26, 4),
                    new Frame(4, 150, 354, 37, 43, 28, 5),
                    new Frame(5, 193, 340, 41, 59, 26, 3),
                    new Frame(6, 250, 337, 51, 64, 7, 1),
                    new Frame(7, 311, 358, 37, 44, 9, 0),
                    new Frame(8, 356, 358, 36, 42, 10, 2),
                    new Frame(9, 397, 358, 21, 42, 9, 2),
                    new Frame(10, 427, 358, 22, 44, 9, 0)),
            new FrameAnimation("goal", FrameAnimation.FrameAnimationSpeed.Goal,
                    new Frame(0, 501, 279, 28, 45, 16),
                    new Frame(1, 537, 279, 29, 45, 16),
                    new Frame(2, 573, 276, 34, 48, 16),
                    new Frame(3, 612, 278, 29, 45, 16),
                    new Frame(4, 648, 278, 31, 45, 16)),
            new FrameAnimation("climbing-start", FrameAnimation.FrameAnimationSpeed.ClimbingStart,
                    new Frame(0, 468, 359, 21, 36)),
            new FrameAnimation("climbing-end", FrameAnimation.FrameAnimationSpeed.ClimbingEnd,
                    new Frame(1, 616, 357, 21, 32),
                    new Frame(2, 645, 350, 18, 34)),
            new FrameAnimation("climbig", FrameAnimation.FrameAnimationSpeed.Climbig,
                    new Frame(0, 499, 352, 19, 49, 9),
                    new Frame(1, 528, 357, 20, 40, 10),
                    new Frame(2, 557, 357, 20, 40, 11),
                    new Frame(3, 587, 352, 19, 49, 10),
                    new Frame(4, 557, 357, 20, 40, 11),
                    new Frame(5, 528, 357, 20, 40, 10)
            )
    );
}