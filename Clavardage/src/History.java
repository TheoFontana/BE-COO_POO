import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.System;

public class History {
    // Indicates where the history file is saved
    private String savePath = "./saved_messages.csv";
    // Indicates where the associations ID <-> Username are stored
    private String savePathUsernames = "./saved_usernames.csv";
    
    private List<Message> messages = new ArrayList<Message>(); 

    private Map<Integer, String> idUsernameDict = new HashMap<Integer, String>();

    // Loads the history file saved in csv format into 
    // the messages List
    public void load() {
        String line = "";
        String splitBy = ",";
        BufferedReader br = null;
        BufferedReader br1 = null;
        try {
            System.out.println("[LOG] Loading history...");
            br = new BufferedReader(new FileReader(savePath));   
            br1 = new BufferedReader(new FileReader(savePathUsernames));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                messages.add(new Message(line.split(splitBy)));
            }
            while ((line = br1.readLine()) != null) {
                System.out.println(line);
                idUsernameDict.put(Integer.parseInt(line.split(",")[0]), line.split(",")[1]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("[WARNING] No history on this system");
        } catch (IOException e) {
            System.out.println("[ERROR] IOException in reading history");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (br1 != null) {
                    br1.close();
                }
            } catch (IOException e) {
                System.out.println("[ERROR] IOException in reading history");
            }
        }
    } 


    // Save messages history to a file in csv format from 
    // the messages List
    public void save() {
       BufferedWriter bw = null;
       BufferedWriter bw1 = null;
        try {
            System.out.println("[LOG] Saving history...");
            bw = new BufferedWriter(new FileWriter(savePath));
            bw1 = new BufferedWriter(new FileWriter(savePathUsernames));
            
            for (int i = 0; i < messages.size(); i++) {
                bw.write(messages.get(i).serialize());
                bw.newLine();
            }
            for (Map.Entry entry : this.idUsernameDict.entrySet()) {
                System.out.println(entry.getKey() + "," + entry.getValue());
                bw1.write(entry.getKey() + "," + entry.getValue());
                bw1.newLine();
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Could not save history");
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (bw1 != null) {
                    bw1.close();
                }
            } catch (IOException e) {
                System.out.println("[ERROR] IOException in saving history");
            }
        }
    } 
    
    // Returns all the messages exchanged with another user
    // given its id
    public List<Message> getConv(int id) {
        List<Message> conv = new ArrayList<Message>();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getDest() == id) {
                conv.add(messages.get(i));
            }
        }
        return conv;
    }
    
    // Add a message to the list of messages
    public void addMessage(Message m) {
        this.messages.add(m);
    }

    // Returns the history in the printable form to show it on gui
    public String parseHistory() {
        String toRet = "";
        for (int i = 0; i < messages.size(); i++) {
            String toShow = "";
            Message m = messages.get(i);
            if (m.getSrc() == 0) {
                toRet+="->";
                toShow = this.idUsernameDict.get(m.getDest());
            } else {
                toRet+="<-";
                toShow = this.idUsernameDict.get(m.getSrc());
            }
            toRet+=("["+m.getTimeStamp()+"@"+toShow+"] : " + m.getContent());
            toRet+=("\n");
        }
        return toRet;
    }

    public void clearHistory() {
        this.messages.clear();
    }

    public void setAssociation(int id, String pseudo) {
        idUsernameDict.put(id, pseudo);
    }

    // Getters + Setters
    public void setSavePath(String path) {
        this.savePath = path;
    }
}
