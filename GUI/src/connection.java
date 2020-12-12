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

	private JFrame frame;
	private JTextField textFieldUser;
	private JTextField textFieldPseudo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					connection window = new connection();
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
	public connection() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
            	// TO DO :
            	//	Check pseudo 
            	// 	Launch chat
            	
            	
            	
            	
            	chat window = new chat()  ; 
				window.frame.setVisible(true);

            	frame.dispose();
                
            }
        });
		
		
		
	}
}
