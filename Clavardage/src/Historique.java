import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Historique {
    // Indicates where the history file is saved
    private String savePath = "./saved_messages.csv";
    
    private List<Message> messages = new ArrayList<Message>(); 

    // Loads the history file saved in csv format into 
    // the messages List
    public void load() {
        String line = "";
        String splitBy = ",";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(savePath));   
            while ((line = br.readLine()) != null) {
                messages.add(new Message(line.split(splitBy)));
            }
        } catch (FileNotFoundException e) {
            System.out.println("No history on this system");
        } catch (IOException e) {
            System.out.println("IOException in reading history");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("IOException in reading history");
            }
        }
    } 


    // Save messages history to a file in csv format from 
    // the messages List
    public void save() {
       BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(savePath, true));
            
            for (int i = 0; i < messages.size(); i++) {
                bw.write(messages.get(i).serialize());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save history");
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                System.out.println("IOException in saving history");
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
    

    // Getters + Setters
    public void setSavePath(String path) {
        this.savePath = path;
    }

}
