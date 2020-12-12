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
public class chat {

	public JFrame frame;
	public JList  conectedUsers;
	protected JTextArea history;
	protected JTextField message;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					chat window = new chat();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public chat() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GREEN);
		panel.setBounds(6, 6, 184, 460);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		String[] data = {"David", "Mathieu", "Théo"};
		
		JList<String> conectedUsers = new JList<String>(data);
		conectedUsers.setBounds(6, 20, 172, 393);
		panel.add(conectedUsers);
		
		JButton startChat = new JButton("Start Chatting");
		startChat.setBounds(31, 425, 117, 29);
		panel.add(startChat);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(202, 6, 492, 460);
		frame.getContentPane().add(tabbedPane);
		
		startChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String selecteduser = conectedUsers.getSelectedValue();
            	JComponent panel1 = chatPanel(selecteduser);
            	tabbedPane.addTab(selecteduser, panel1);
                
            }
        });
		
		
	}
	
   
    
    protected JComponent chatPanel(String user ) {
        JPanel panel = new JPanel(false);

		panel.setLayout(null);
		
		JLabel otherUser = new JLabel(user);
		otherUser.setBounds(32, 17, 61, 16);
		panel.add(otherUser);

		
		history = new JTextArea();
		history.setBounds(6, 45, 438, 211);
		panel.add(history);		
		JLabel messageLabel = new JLabel("Message");
		messageLabel.setBounds(6, 273, 61, 16);
		panel.add(messageLabel);
		
		JTextField message = new JTextField();
		message.setBounds(79, 268, 247, 26);
		panel.add(message);
		message.setColumns(10);
		
		JButton send = new JButton("Send");
		send.setBounds(327, 268, 117, 29);
		panel.add(send);
		send.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sendMessage(message.getText());
            }
        });
		
        return panel;
	}
    
    public void sendMessage(String content) {
        System.out.println("test");
    }
    
    
    
}
