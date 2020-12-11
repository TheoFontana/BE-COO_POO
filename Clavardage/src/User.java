import java.lang.System;

public class User {
    // UserID
    private int id;
    // Pseudo of the user
    private String pseudo;
    // History to store sent and received messages
    private History history;
    // Reference to the chat class used to do all networking
    // ie send/receive messages
    private ChatMaster chat;

    // constructor
    public User(int id, String pseudo, ChatMaster chat) {
        this.id = id;
        this.pseudo = pseudo;
        this.history = new History();
        this.history.load();
        this.chat = chat;
    }

    // Send a message to the user with the id destId
    public void sendMessage(int destId, String content) {
        Message mess = new Message(this.id, destId, content);
        System.out.println("[LOG] Sending " + content + " to " + destId);
    }

    // Getters + Setters
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
