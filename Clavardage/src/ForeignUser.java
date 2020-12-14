// Represents a User that can be contacted with the
// tool 
public class ForeignUser {
    public String IP;
    public int port;
    public String Pseudo;
    public int id;
    


	//Constructor 
    public ForeignUser(String IP, int port, String pseudo, int id) {
    	this.IP=IP;
    	this.port=port;
    	this.Pseudo=pseudo;
    	this.id=id;
    	
    }
    
	public String getPseudo() {
		return this.Pseudo;
	}
	public String getIP() {
		return this.IP;
	}
	public int getId() {
		return this.id;
	}
}
