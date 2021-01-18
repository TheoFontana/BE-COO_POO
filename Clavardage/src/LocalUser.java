import java.lang.System;
// Represents the local user of the chat system
class LocalUser extends User {
    
    // Reference to the chat master
    private ChatMaster chat;
    // To store and load messages
    private History history;
    
    // Constructor
    public LocalUser(int id, String pseudo, ChatMaster chat) {
        super(id, pseudo);
        this.chat = chat;
        this.history = new History();
        this.history.load();
    }

    public History getHistory() {
        return this.history;
    }
}
