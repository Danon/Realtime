package app;

import debug.DefaultLoginGameClient;
import ui.gfx.resources.Resources;
import util.option.Options;

import static javax.swing.JOptionPane.*;

public class Application {
    public final static String VERSION = "0.02";
    public final static Options RunOptions = new Options();

    public static void main(String[] args) {
        Resources.load();

        RunOptions.setOptions(args);
        System.out.println(RunOptions);

        if (RunOptions.isUsed("-DebugX")) {
            new GameServer(false);
            new GameClient().start();
            new GameClient().start();
        } else if (RunOptions.isUsed("-Debug")) {
            new GameServer(true);
            new DefaultLoginGameClient("Test", "test").start();
        } else {
            int option = showOptionDialog(null,
                    "Would you like to run the server?", "Are you server?",
                    YES_NO_OPTION, QUESTION_MESSAGE, null,
                    new Object[]{"Server", "Client"}, "Server");

            switch (option) {
                case YES_OPTION:
                    new GameServer(true);
                    break;

                case NO_OPTION:
                    Resources.load();
                    new GameClient().start();
                    break;
            }
        }
    }
}
