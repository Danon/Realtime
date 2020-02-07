package ui;

import javax.swing.*;

public class MessageBox {
    public static void show(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

    public static void showFormat(String text, Object... args) {
        JOptionPane.showMessageDialog(null, String.format(text, args));
    }
}
