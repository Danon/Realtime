package ui;

public interface ClientChatListener {
    void receiveTextMessage(int userId, String text);
}
