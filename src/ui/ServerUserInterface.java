package ui;

import app.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ServerUserInterface extends CustomUserInterface {
    private final JFrame frame = new JFrame();

    public ServerUserInterface() {
        frame.setTitle("Server running v" + Application.VERSION);
        frame.setAlwaysOnTop(true);
        frame.setSize(300, 75);
        frame.setLocationRelativeTo(null);

        JButton closeButton = new JButton("Server running: Close");
        closeButton.addActionListener(this::closeApplicationActionPerformed);

        frame.add(closeButton);
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    private void closeApplicationActionPerformed(ActionEvent e) {
        System.exit(-1);
    }
}
