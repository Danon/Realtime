package util.save;

import app.UserNotFoundException;
import gameplay.GameMap;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileNotFoundException;

final public class SaveManager {
    private static final CustomSaveManager manager = new CustomSaveManager();

    public static boolean save(Savable object, String folderName, String filename) {
        return manager.saveObject(folderName, filename, object);
    }

    @SneakyThrows
    public static void load(String folderName, String fileName, Savable object) {
        manager.loadObject(folderName, fileName, object);
    }

    public static boolean exists(String folderName, String filename) {
        return new File(folderName, filename.toLowerCase()).exists();
    }

    public static class Map {
        final static String pathName = "maps";

        public static String[] list() {
            return new File(pathName).list();
        }

        @SneakyThrows
        public static GameMap load(String mapName) {
            GameMap map = new GameMap(mapName, -1, -1);
            manager.loadObject(pathName, mapName, map);
            return map;
        }
    }

    public static class Accounts {
        final static String pathName = "users";

        public static boolean exists(String username) {
            return new File(pathName, username.toLowerCase()).exists();
        }

        public static String[] list() {
            return new File(pathName).list();
        }

        public static boolean save(network.UserAccount account) {
            return manager.saveObject(pathName, account.getUsername(), account);
        }

        public static network.UserAccount load(String username) {
            network.UserAccount account = new network.UserAccount();
            try {
                manager.loadObject(pathName, username, account);
                return account;
            } catch (FileNotFoundException e) {
                throw new UserNotFoundException(username);
            }
        }
    }
}
