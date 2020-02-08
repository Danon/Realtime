package util.save;

import app.UserNotFoundException;
import gameplay.GameMap;
import lombok.SneakyThrows;
import network.UserAccount;

import java.io.File;
import java.io.FileNotFoundException;

final public class SaveManager {
    private static final CustomSaveManager manager = new CustomSaveManager();

    public static boolean save(Saveable object, String folderName, String filename) {
        return manager.saveObject(folderName, filename, object);
    }

    public static class Map {
        final static String pathName = "maps";

        public static String[] list() {
            return new File(pathName).list();
        }

        @SneakyThrows
        public static GameMap load(String mapName) {
            return manager.loadObject(pathName, mapName, GameMap.factory());
        }
    }

    public static class UserAutoIncrement {
        public static int autoInc() {
            int load = load(10);
            PrimitiveReader reader = new PrimitiveReader();
            reader.setValue(load + 1);
            SaveManager.save(reader, "settings", "userId");
            return reader.getValue();
        }

        private static int load(int startingValue) {
            try {
                return manager.loadObject("settings", "userId", new PrimitiveReader()).getValue();
            } catch (FileNotFoundException e) {
                return startingValue;
            }
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
            try {
                return manager.loadObject(pathName, username, UserAccount.factory());
            } catch (FileNotFoundException e) {
                throw new UserNotFoundException(username);
            }
        }
    }
}
