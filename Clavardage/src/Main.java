public class Main {
    public static void main(String args[]) {
        Chat chat = new Chat();
        User user = new User(1, "pseudo_test", chat);
        GraphicalInterface gui = new GraphicalInterface(user);
    }
}