package ui;

import java.util.ArrayList;

public class Chat {
    private static boolean fieldOpen;
    private static boolean historyOpen;
    private static int historyOpenFor;
    private static String textTyped = "";
    private static final ArrayList<String> messages = new ArrayList<>();
    public static String[] preparedMessages = new String[0];

    static void addMessage(String text) {
        messages.add(text);
        showHistory();
    }

    private static String[] getLastMessages(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be positive number");
        }

        return messages.subList(
                Math.max(0, messages.size() - count), 0
        ).toArray(new String[Math.min(count, messages.size())]);
    }

    static void setLast5ForEasyRetrieval() {
        preparedMessages = getLastMessages(5);
    }

    private static void showTextField() {
        fieldOpen = true;
        historyOpen = true;
        historyOpenFor = 0;
    }

    static void hideTextField() {
        fieldOpen = false;
    }

    public static boolean textFieldShown() {
        return fieldOpen;
    }

    static boolean toggleTextField() {
        if (fieldOpen) {
            hideTextField();
        } else {
            showTextField();
        }
        return textFieldShown();
    }

    public static boolean historyShown() {
        return historyOpen;
    }

    static void showHistory() {
        historyOpen = true;
        historyOpenFor = 0;
    }

    public static String getText() {
        return textTyped;
    }

    static boolean textEmpty() {
        return textTyped.equals("");
    }

    static void clearText() {
        textTyped = "";
    }

    static void keyPressed(char key) {
        textTyped += key;
    }
}
