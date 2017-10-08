package ui;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Chat {
    private boolean fieldOpen;
    private boolean historyOpen;
    private String textTyped = "";

    private final List<String> messages = new CopyOnWriteArrayList<>();
    private volatile String[] preparedMessages = new String[0];

    void addMessage(String text) {
        messages.add(text);
        showHistory();
    }

    private String[] getLastMessages(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be positive number");
        }

        if (messages.size() < count) {
            return messages.toArray(new String[messages.size()]);
        }

        return messages
                .subList(
                        max(0, messages.size() - count - 1),
                        max(1, messages.size())
                )
                .toArray(new String[min(count, messages.size())]);
    }

    void setLast5ForEasyRetrieval() {
        preparedMessages = getLastMessages(5);
    }

    public String[] getPreparedMessages() {
        return preparedMessages;
    }

    private void showTextField() {
        fieldOpen = true;
        historyOpen = true;
    }

    void hideTextField() {
        fieldOpen = false;
    }

    public boolean isTextFieldShown() {
        return fieldOpen;
    }

    boolean toggleTextField() {
        if (fieldOpen) {
            hideTextField();
        } else {
            showTextField();
        }
        return isTextFieldShown();
    }

    public boolean isHistoryShown() {
        return historyOpen;
    }

    void showHistory() {
        historyOpen = true;
    }

    public String getText() {
        return textTyped;
    }

    boolean isTextEmpty() {
        return textTyped.equals("");
    }

    void clearText() {
        textTyped = "";
    }

    void keyPressed(char key) {
        textTyped += key;
    }
}
