public class Main {
    public static void main(String args[]) {
        ChatMaster chat = new ChatMaster(1337);
       // chat.run();
        connection gui = new connection(chat);
        gui.frame.setVisible(true);
    }
}