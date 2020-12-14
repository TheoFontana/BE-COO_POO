import java.awt.EventQueue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.System;
import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class connection {

	public JFrame frame;
	private JTextField textFieldUser;
	private JTextField textFieldPseudo;
	private ChatMaster chat;

	// Constructor
	public connection( ChatMaster chat1) {
		this.chat=chat1;
		frame = new JFrame("Connection");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
				
		
		JLabel username = new JLabel("User Name");
		username.setBounds(29, 56, 68, 16);
		frame.getContentPane().add(username);
		

		textFieldUser = new JTextField();
		textFieldUser.setBounds(109, 51, 161, 26);
		frame.getContentPane().add(textFieldUser);
		textFieldUser.setColumns(10);

		
		JLabel pseudo = new JLabel("Pseudo");
		pseudo.setBounds(29, 94, 45, 16);
		frame.getContentPane().add(pseudo);
		
		textFieldPseudo = new JTextField();
		textFieldPseudo.setBounds(109, 89, 161, 21);
		frame.getContentPane().add(textFieldPseudo);
		
		JButton enterChat = new JButton("Login");
		enterChat.setBounds(109, 145, 79, 29);
		frame.getContentPane().add(enterChat);
		
		enterChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (!textFieldUser.getText().equals("") && !textFieldPseudo.getText().equals("")) {
                    enterChat(textFieldUser.getText(), textFieldPseudo.getText());
                    GraphicalInterface1 window = new GraphicalInterface1(chat)  ; 
    				window.frame.setVisible(true);
                	frame.dispose();

            	}        
            }
        });
			
	}
	
    public void enterChat(String account, String pseudo) {
        int id = account.hashCode();
        System.out.println("[LOG] Entered with account : " + account + " and pseudo : " + pseudo);
        System.out.println("[LOG] ID = " + id);
    }
}
