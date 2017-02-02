package ui;

import javax.swing.JOptionPane;

public class MessageBox {
    public static void show(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

    static void show(String text, String title) {
        JOptionPane.showMessageDialog(null, title, title, Type.Plain.getValue());
    }

    static void show(String text, Type messageType) {
        JOptionPane.showMessageDialog(null, text, "", messageType.getValue());
    }

    static void show(String text, String title, Type messageType) {
        JOptionPane.showConfirmDialog(null, text, title, messageType.getValue());
    }


    public static void showFormat(String text, Object... args) {
        JOptionPane.showMessageDialog(null, String.format(text, args));
    }

    static void showFormat(String text, Type messageType, Object... args) {
        JOptionPane.showMessageDialog(null, String.format(text, args), "", messageType.getValue());
    }

    static void showFormat(String text, String title, Type messageType, Object... args) {
        JOptionPane.showConfirmDialog(null, String.format(text, args), title, messageType.getValue());
    }

    public enum Type {
        Error(0), Information(1), Warning(2), Question(3), Plain(4);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
