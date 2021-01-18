import java.lang.System;

public class User {
    // UserID
    protected int id;
    // Pseudo of the user
    protected String pseudo;

    // constructor
    public User(int id, String pseudo) {
        this.id = id;
        this.pseudo = pseudo;
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
