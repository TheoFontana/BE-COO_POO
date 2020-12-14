import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.System;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.System;
import java.time.LocalDateTime;

public class GraphicalInterface1 {

	public JFrame frame;
	private JList<ForeignUser>  conectedUsers;
	protected JPanel userspanel;
	
    // Reference to the chat object that does all network communications
    // ie send/receive messages
    private ChatMaster chat;
    
    // Constructor
    public GraphicalInterface1(ChatMaster chat2) {
    	
        this.chat = chat2;

    	// Set the window frame
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
        // Right panel with connected users list
        JPanel userspanel = new JPanel();
        userspanel.setBackground(Color.GREEN);
        userspanel.setBounds(30, 30, 184, 460);
        frame.getContentPane().add(userspanel);
        userspanel.setLayout(null);
        
        // Left tabbedPanel with the conversation
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(202, 6, 492, 460);
		frame.getContentPane().add(tabbedPane);

		
		ForeignUser[] fu = {new ForeignUser("124",1025,"toto",123)};
		// List of the connected user
		conectedUsers = new JList<ForeignUser>(fu);
		conectedUsers.setBounds(6, 36, 172, 377);
		userspanel.add(conectedUsers);
		
		JButton startChat = new JButton("Start Chatting");
		startChat.setBounds(31, 425, 117, 29);
		userspanel.add(startChat);
		
		JButton refreshUsers = new JButton("Refresh");
		refreshUsers.setBounds(31, 6, 117, 29);
		userspanel.add(refreshUsers);
		refreshUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// TODO   refresh connectedUser base on the UDP scan  
            }
        });	
		
		startChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	ForeignUser selecteduser = conectedUsers.getSelectedValue();
            	JComponent panel1 = createChatPanel(selecteduser);
            	tabbedPane.addTab(selecteduser.getPseudo(), panel1); 
            }
        });	

    }
	
    protected JComponent createChatPanel(ForeignUser user ) {
    	
        //ChatClient client = this.chat.openConnection(user.getIP());
       // client.start();    
        
        JPanel panel = new JPanel(false);
		panel.setLayout(null);
		
		JLabel otherUser = new JLabel(user.getPseudo());
		otherUser.setBounds(32, 17, 61, 16);
		panel.add(otherUser);
		
		JButton refreshHistory = new JButton("Refresh");
		refreshHistory.setBounds(316, 12, 117, 29);
		panel.add(refreshHistory);
		
		List<Message> conv = chat.getLocalUser().getHistory().getConv(user.getId());	
		JList<Message> convhistory = new JList<Message>((ListModel<Message>) conv);
		convhistory.setBounds(6, 45, 438, 211);
		panel.add(convhistory);	
		
		JLabel messageLabel = new JLabel("Message");
		messageLabel.setBounds(6, 273, 61, 16);
		panel.add(messageLabel);
		
		JTextField messageContent = new JTextField();
		messageContent.setBounds(79, 268, 247, 26);
		panel.add(messageContent);
		messageContent.setColumns(10);
		
		
		JButton send = new JButton("Send");
		send.setBounds(327, 268, 117, 29);
		panel.add(send);
		
		send.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Message message = new Message( chat.getLocalUser().getId(), user.getId(), messageContent.getText()) ;
            	chat.getLocalUser().getHistory().addMessage(message);
            	// TODO send message via TCP to user
            }
        });
        
        return panel;
	}
    
}
