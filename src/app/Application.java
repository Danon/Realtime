package app;

import ui.gfx.Resources;
import util.option.Options;

import javax.swing.*;
import java.io.IOException;

public class Application {
    public final static String VERSION = "0.019.0";
    public final static Options RunOptions = new Options();

    public static void main(String[] args) throws IOException {
        RunOptions.setOptions(args);
        System.out.println(RunOptions);

        if (RunOptions.isUsed("-Debug")) {
            Resources.load();
            new GameServer();
            new GameClient().openUserInterface();
        } else {
            switch (JOptionPane.showConfirmDialog(null,
                    "Would you like to run the server? \n Yes - Server \n No - Client", "Are you server?", JOptionPane.YES_NO_OPTION)) {
                case JOptionPane.YES_OPTION:
                    new GameServer();
                    break;

                case JOptionPane.NO_OPTION:
                    Resources.load();
                    new GameClient().openUserInterface();
                    break;
            }
        }
    }
}
