import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.System;

// https://www.guru99.com/java-swing-gui.html
public class GraphicalInterface {
    
    // Swing objects
    private JFrame frame;
    private JPanel panel; 
    private JLabel label;
    private JTextField tf; 
    private JButton send;
    private JButton reset;
    private JTextArea ta;

    // Reference to the chat object that does all network communications
    // ie send/receive messages
    private Chat chat;

    // Reference to the user 
    private User user;

    public GraphicalInterface(User user) {
        
        this.user = user;

        //Creating the Frame
        frame = new JFrame("Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        
        //Creating the panel at bottom and adding components
        panel = new JPanel(); // the panel is not visible in output
        label = new JLabel("Enter Text");
        tf = new JTextField(50); // accepts upto 50 characters
        send = new JButton("Send");
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(tf.getText());
            }
        });
        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetMessage(tf.getText());
            }
        });

        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(send);
        panel.add(reset);

        // Text Area at the Center
        ta = new JTextArea();

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
    }

    // Method executed when the user click on send
    public void sendMessage(String content) {
        // TODO
        this.tf.setText("");
        this.ta.append("[ME]> " + content + "\n");
    }

    // Method executed when the user click on reset
    public void sendMessage(String content) {
        this.tf.setText("");
    }

}
