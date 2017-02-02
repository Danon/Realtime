package ui;

import app.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerUserInterface extends CustomUserInterface implements ActionListener {
    private final JFrame frame = new JFrame();

    public ServerUserInterface() {
        frame.setTitle("Server running v" + Application.VERSION);
        frame.setLocationRelativeTo(null);

        JButton closeButton = new JButton("Server running: Close");
        closeButton.addActionListener(this);

        frame.add(closeButton);
        frame.pack();
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(-1);
    }
}
