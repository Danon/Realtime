package util.save;

import gameplay.GameMap;
import ui.gfx.SkeletonAnimation;

import java.io.File;

final public class SaveManager {
    private static final CustomSaveManager manager = new CustomSaveManager();

    public static boolean save(Savable object, String folderName, String fileName) {
        return manager.saveObject(folderName, fileName, object);
    }

    public static boolean load(String folderName, String fileName, Savable object) {
        return manager.loadObject(folderName, fileName, object);
    }

    public static boolean exists(String folderName, String mapName) {
        return new File(folderName, mapName.toLowerCase()).exists();
    }

    public static class Map {
        final static String pathName = "maps";

        public static boolean exists(String mapName) {
            return new File(pathName, mapName.toLowerCase()).exists();
        }

        public static String[] list() {
            return new File(pathName).list();
        }

        public static void save(GameMap map) {
            manager.saveObject(pathName, map.getName(), map);
        }

        public static GameMap load(String mapName) {
            GameMap map = new GameMap(mapName, -1, -1);
            manager.loadObject(pathName, mapName, map);
            return map;
        }
    }

    public static class Animation {
        final static String pathName = "animations";

        public static boolean exists(String animationName) {
            return new File(pathName, animationName.toLowerCase()).exists();
        }

        public static String[] list() {
            return new File(pathName).list();
        }

        public static void save(SkeletonAnimation animation) {
            manager.saveObject(pathName, animation.getName(), animation);
        }

        public static SkeletonAnimation load(String animationName) {
            SkeletonAnimation skeleton = new SkeletonAnimation(animationName);
            manager.loadObject(pathName, animationName, skeleton);
            return skeleton;
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
            if (manager.loadObject(pathName, username, account)) {
                return account;
            }
            return null;
        }
    }
}