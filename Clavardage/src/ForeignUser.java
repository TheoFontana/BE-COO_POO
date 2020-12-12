// Represents a User that can be contacted with the
// tool 
public class ForeignUser {
    public String ip;
    public int port;
    public String pseudo;
    public int id;

    // Constructor
    public ForeignUser(String ip, int port, String pseudo, int id) {
        this.ip = ip;
        this.port = port;
        this.pseudo = pseudo;
        this.id = id;
    }

    // Create a new ForeignUser from a String with the following format
    // ip:port:pseudo:id
    public static ForeignUser fromString(String serialized) {
        String [] splitted = serialized.split(":");
        return new ForeignUser(splitted[0], Integer.parseInt(splitted[1]), splitted[2], Integer.parseInt(splitted[3]));
    }

    // Override equals method to allow calling contains() on an ArrayList of these objects
    @Override
    public boolean equals(Object other) {
        // If compared with itself, equals returns true
        if (other == this) {return true;}
        // If compared with an object that is not a ForeignUser
        // equals returns false
        if (!(other instanceof ForeignUser)) { 
            return false; 
        } 
        ForeignUser otherUser = (ForeignUser)other;

        return this.ip.equals(otherUser.ip) && this.id == otherUser.id && this.pseudo.equals(otherUser.pseudo) && this.port == otherUser.port;
    }

}
