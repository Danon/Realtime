package ui;

import javax.swing.JOptionPane;

import util.Validate;

public class CustomUserInterface {
    public void showInfo(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

    public String input(String message, String title, String defValue) {
        while (true) {
            String input = (String) JOptionPane.showInputDialog(
                    null, message, title, JOptionPane.QUESTION_MESSAGE, null, null, defValue);

            input = input.trim();

            if (input.length() != 0) {
                return input;
            }
        }
    }

    public int input(String message, String title, int defValue) {
        while (true) {
            String input = (String) JOptionPane.showInputDialog(
                    null, message, title, JOptionPane.QUESTION_MESSAGE, null, null, defValue);

            input = input.trim();

            if (input.length() != 0 && Validate.isInt(input)) {
                return Integer.parseInt(input);
            }
        }
    }
}
