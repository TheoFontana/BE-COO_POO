public class Main {
    public static void main(String args[]) {
        ChatMaster chat = new ChatMaster(1337, 1338);
        chat.start();
        GraphicalInterface gui = new GraphicalInterface(chat);
    }
}