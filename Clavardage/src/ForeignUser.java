import java.net.*;

// Represents a User that can be contacted with the tool 
public class ForeignUser extends User {
    public String ip;
    // If this socket is not null, it means that the local client 
    // is connected to this ForeignUser
    private Socket sock = null;
    
    // Is this user outside of the LAN ? Default : false
    private boolean remote = false;

    // Constructor
    public ForeignUser(int id, String pseudo, String ip) {
        super(id, pseudo);
        this.ip = ip;
    }

    // Override toString method
    public String toString() {
        return this.pseudo + ":" + this.ip;
    }

    // Create a new ForeignUser from a String with the following format
    // ip:pseudo:id
    public static ForeignUser fromString(String serialized) {
        String [] splitted = serialized.split(":");
        if (splitted.length != 3) {return null;}
        return new ForeignUser(Integer.parseInt(splitted[2]), splitted[1], splitted[0]);
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

        return this.ip.equals(otherUser.ip) && this.id == otherUser.id && this.pseudo.equals(otherUser.pseudo);
    }

    public Socket getSock() {
        return this.sock;
    }

    public void setSock(Socket sock) {
        this.sock = sock;
    }

    public String getIP() {
        return this.ip;
    }

    public void setRemote() {
        this.remote = true;
    }

    public boolean isRemote() {
        return this.remote;
    }

}
