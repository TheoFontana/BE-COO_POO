import java.net.*;

// Represents a user managed by the presence management server
public class ManagedUser {
    // Ip Address of the user
    private String IPAddress;
    // Stores the status of the user 
    private Status status;
    private String pseudo;

    // Constructor
    public ManagedUser(String ip, String pseudo) {
        this.IPAddress = ip;
        this.pseudo = pseudo;
        this.setActive();
    }
    
    // Status setters
    public void setActive() {
        this.status = Status.ACTIVE;
    }
    public void setAbsent() {
        this.status = Status.ABSENT;
    }

    public String toString() {
        return this.pseudo + ":" + this.IPAddress;
    }
    
    // Override equals method to allow calling contains() on an ArrayList of these objects
    @Override
    public boolean equals(Object other) {
        // If compared with itself, equals returns true
        if (other == this) {return true;}
        // If compared with an object that is not a ManagedUser
        // equals returns false
        if (!(other instanceof ManagedUser)) { 
            return false; 
        } 

        ManagedUser otherUser = (ManagedUser)other;

        return this.IPAddress.equals(otherUser.IPAddress) && this.pseudo.equals(otherUser.pseudo);
    }

    public String getIP() {
        return this.IPAddress;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getStatus() {
        switch (this.status) {
            case ABSENT:
                return "ABSENT";
            case ACTIVE:
                return "ACTIVE";
            default:
                return "UNKOWN";
        }
    }

    // Represents the possible status for a ManagedUser
    private enum Status {
        ACTIVE, ABSENT
    }
}
