package util;

import ui.window.LobbyForm;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.swing.UIManager.getInstalledLookAndFeels;

public class LookAndFeel {
    public static void setLookAndFeel() {
        try {
            for (LookAndFeelInfo info : getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            getLogger(LobbyForm.class.getName()).log(SEVERE, null, ex);
        }
    }
}
