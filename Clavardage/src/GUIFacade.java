import java.time.LocalDateTime;

public class GUIFacade {
    // Instance of the Window (visible element of the gui)
    private Window w;

    // Reference to the ChatMaster
    private ChatMaster chat;

    // Constructor
    public GUIFacade(ChatMaster chat) {
        this.chat = chat;
        this.w = new Window(this.chat);
    }

    
    public void foreignUserConnection(ForeignUser foreignUser) {
        this.w.foreignUsers_list.addElement(foreignUser);
        String to_display = "[" + LocalDateTime.now().withNano(0) + "] : " + foreignUser.toString() + " logged in";
        if (this.w.messages_pane.getText().equals("")) {
            this.w.messages_pane.setText(this.w.messages_pane.getText() + to_display);
        } else {
            this.w.messages_pane.setText(this.w.messages_pane.getText() + "\n" + to_display);
        }
    }

    public void foreignUserDeconnection(ForeignUser foreignUser) {
        this.w.messages_pane.setText(this.w.messages_pane.getText() + "\n" + ("[" + LocalDateTime.now().withNano(0) + "] : " + foreignUser.toString() + " logged out"));
        this.w.foreignUsers_list.removeElement(foreignUser);
    }

    public void messageReception(ForeignUser foreignUser, String text_message) {
        this.w.messages_pane.setText(this.w.messages_pane.getText() + "\n" + ("<-[" + LocalDateTime.now().withNano(0) + "@" + foreignUser.toString() + "] : " + text_message));
    }

}
