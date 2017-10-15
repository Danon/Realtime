package ui;

import app.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static util.LookAndFeel.setLookAndFeel;

public class ServerUserInterface extends CustomUserInterface {
    private final JFrame frame = new JFrame();

    public ServerUserInterface() {
        setLookAndFeel();

        frame.setTitle("Realtime | Server v" + Application.VERSION);
        frame.setSize(350, 75);
        frame.setLocationRelativeTo(null);

        JButton closeButton = new JButton("Server running: Close");
        closeButton.addActionListener(this::closeApplicationActionPerformed);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(new JLabel("Server control panel"));
        frame.add(closeButton);
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    private void closeApplicationActionPerformed(ActionEvent e) {
        System.exit(-1);
    }
}
