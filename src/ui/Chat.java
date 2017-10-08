package ui;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class Chat {
    private static volatile boolean fieldOpen;
    private static volatile boolean historyOpen;
    private static volatile String textTyped = "";

    private static final List<String> messages = new ArrayList<>();
    private static volatile String[] preparedMessages = new String[0];

    static void addMessage(String text) {
        synchronized (messages) {
            messages.add(text);
        }
        showHistory();
    }

    private static String[] getLastMessages(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be positive number");
        }

        synchronized (messages) {
            if (messages.isEmpty()) {
                return new String[0];
            }

            return messages
                    .subList(
                            max(0, messages.size() - count - 1),
                            max(0, messages.size() - 1)
                    )
                    .toArray(new String[Math.min(count, messages.size())]);
        }
    }

    static void setLast5ForEasyRetrieval() {
        preparedMessages = getLastMessages(5);
    }

    public static String[] getPreparedMessages() {
        return preparedMessages;
    }

    private static void showTextField() {
        fieldOpen = true;
        historyOpen = true;
    }

    static void hideTextField() {
        fieldOpen = false;
    }

    public static boolean isTextFieldShown() {
        return fieldOpen;
    }

    static boolean toggleTextField() {
        if (fieldOpen) {
            hideTextField();
        } else {
            showTextField();
        }
        return isTextFieldShown();
    }

    public static boolean historyShown() {
        return historyOpen;
    }

    static void showHistory() {
        historyOpen = true;
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
