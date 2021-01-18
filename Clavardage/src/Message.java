import java.time.LocalDateTime;

public class Message {
    // ID of the sender
    private int idSender;
    // ID of the receiver
    private int idReceiver;
    // Time at which the message was sent
    private LocalDateTime sendTime;
    // Content of the message
    private String content;

    // Constructor 
    public Message(int idSrc, int idDest, String content) {
        this.idSender = idSrc;
        this.idReceiver = idDest;
        this.content = content;
        this.sendTime = LocalDateTime.now().withNano(0);
    }

    // Constructor overload to create a message from a csv
    // entry
    public Message(String [] csvEntry) {
        this.idSender = Integer.parseInt(csvEntry[0]);
        this.idReceiver = Integer.parseInt(csvEntry[1]);
        this.sendTime = LocalDateTime.parse(csvEntry[2]);
        this.content = csvEntry[3];
    }

    // Returns the message in csv format :
    // idSender, idReceiver, date, content
    public String serialize() {
        return Integer.toString(idSender) + "," 
            + Integer.toString(idReceiver) + "," 
            + sendTime.toString() + "," 
            + content.replaceAll("\n", "");
    }

    // Add a message to an history
    public void updateHistory(History h) {
        h.addMessage(this);       
    }

    // Getters + Setters
    public int getSrc() {
        return this.idSender;
    }

    public int getDest() {
        return this.idReceiver;
    }

    public LocalDateTime getTimeStamp(){
        return this.sendTime;
    }

    public String getContent() {
        return this.content;
    }
}
