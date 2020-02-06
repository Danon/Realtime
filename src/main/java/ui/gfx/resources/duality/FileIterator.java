package ui.gfx.resources.duality;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@RequiredArgsConstructor
public class FileIterator {
    private final String path;

    public void iterate(Consumer<String> consumer) {
        File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        if (file.isFile()) {
            iterateJarFile(file, consumer);
        } else {
            iterateAbsoluteFile(consumer);
        }
    }

    private void iterateAbsoluteFile(Consumer<String> consumer) {
        File[] files = new File(path).listFiles();

        if (files == null) {
            throw new RuntimeException("Cannot list files in /" + path);
        }

        for (File file : files) {
            if (file.isFile()) {
                consumer.accept(file.getPath());
            }
        }
    }

    private void iterateJarFile(File file, Consumer<String> consumer) {
        try (JarFile jar = new JarFile(file)) {
            tryIterateJarFile(jar, consumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void tryIterateJarFile(JarFile jar, Consumer<String> consumer) {
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.startsWith(path + "/")) {
                consumer.accept(name);
            }
        }
    }
}
