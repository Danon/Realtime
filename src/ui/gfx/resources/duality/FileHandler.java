package ui.gfx.resources.duality;

import lombok.RequiredArgsConstructor;
import ui.gfx.resources.ResourceImages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class FileHandler {
    private final String filename;

    public InputStream getInputStream() {
        try {
            return tryLoadImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream tryLoadImage() throws IOException {
        InputStream inputStream = ResourceImages.class.getResourceAsStream("/" + filename);

        if (inputStream != null) {
            return inputStream;
        }

        return new FileInputStream(new File(filename).getAbsoluteFile());
    }

    public boolean exists() {
        InputStream inputStream = ResourceImages.class.getResourceAsStream("/" + filename);

        if (inputStream != null) {
            return true;
        }

        return new File(filename).exists();
    }
}
