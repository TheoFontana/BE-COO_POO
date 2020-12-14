import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.System;
import java.time.LocalDateTime;

// https://www.guru99.com/java-swing-gui.html
public class GraphicalInterface {

    // Swing objects
    private JFrame frame;
    private JPanel panel;
    public JPanel userspanel;
    private JLabel label;
    private JTextField message;
    private JButton send;
    private JButton reset;
    private JTextArea messageHistory;

    // Reference to the chat object that does all network communications
    // ie send/receive messages
    private ChatMaster chat;

    // constructor
    public GraphicalInterface(ChatMaster chat) {

        this.chat = chat;

        // Creating the Frame
        frame = new JFrame("CHAT app");
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

		
		// TODO manage the connected users list
		
		
        String[] data = { "David", "Mathieu", "Théo" };
        JList<String> conectedUsers = new JList<String>(data);
        conectedUsers.setBounds(6, 20, 172, 393);
        userspanel.add(conectedUsers);
        
		JButton refreshUsers = new JButton("Refresh");
		refreshUsers.setBounds(31, 6, 117, 29);
		userspanel.add(refreshUsers);

        JButton startChat = new JButton("Start Chatting");
        startChat.setBounds(31, 425, 117, 29);
        userspanel.add(startChat);
        
		startChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String selecteduser = conectedUsers.getSelectedValue();
            	JComponent panel1 = chatPanel(selecteduser);
            	tabbedPane.addTab(selecteduser, panel1);
                
            }
        });	
		
		
		
        // Creating the panel at bottom and adding components
        panel = new JPanel(); // the panel is not visible in output
        label = new JLabel("Message : ");
        message = new JTextField(30); // accepts upto 30 characters
        send = new JButton("Send");
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(message.getText());
            }
        });
        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetMessage(message.getText());
            }
        });

        // Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("Chat");
        mb.add(m1);
        JMenuItem m11 = new JMenuItem("New Chat");
        m1.add(m11);
        m11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openDialogNewConn();
            }
        });
        JMenuItem m12 = new JMenuItem("Scan for hosts");
        m12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scanHosts();
            }
        });
        m1.add(m12);

        panel.add(label); // Components Added using Flow Layout
        panel.add(message);
        panel.add(send);
        panel.add(reset);

        // Text Area at the Center
        messageHistory = new JTextArea();
        messageHistory.setEditable(false);

        // Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, messageHistory);
        frame.setVisible(true);

        this.openDialogAskPseudo();
    }
    
    protected JComponent chatPanel(String user ) {
        JPanel panel = new JPanel(false);

		panel.setLayout(null);
		
		JLabel otherUser = new JLabel(user);
		otherUser.setBounds(32, 17, 61, 16);
		panel.add(otherUser);

		
		messageHistory = new JTextArea();
		messageHistory.setBounds(6, 45, 438, 211);
		panel.add(messageHistory);		
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

    

    private class DiscussionPanel extends JPanel {
        private JTabbedPane pane;

        public DiscussionPanel(String title, JTabbedPane pane) {
            this.pane = pane;
            setOpaque(false);
            JLabel label = new JLabel(title);
            this.add(label);
        }

        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(this);
            if (i != -1)
                pane.remove(i);
        }

    }

    // Method executed when the user click on send
    public void sendMessage(String content) {
        this.message.setText("");
        this.messageHistory.append("[" + this.chat.getLocalUser().getPseudo() + " at " + LocalDateTime.now().withNano(0)
                + "]> " + content + "\n");
    }

    // Method executed when the user click on reset
    public void resetMessage(String content) {
        this.message.setText("");
    }

    // Opens a dialog window to connect to another user to begin chatting
    public void openDialogNewConn() {
        JDialog d = new JDialog(this.frame, "New Connection");
        d.setSize(400, 100);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("IP : ");
        JTextField tf = new JTextField(20); // accepts upto 20 characters
        JButton send = new JButton("Connect");
        send.addActionListener(new ActionListener() {

            private JPanel newpanel;

            public void actionPerformed(ActionEvent e) {
                newpanel = new JPanel();
                openConnection(message.getText());
            }
        });
        panel.add(label);
        panel.add(message);
        panel.add(send);
        d.add(panel);
        d.setVisible(true);
    }

    // Opens a dialog window to Enter Account Name and temporary pseudo
    // Account Name is used to compute a unique id for the communications
    public void openDialogAskPseudo() {
        JDialog d = new JDialog(this.frame, "Enter Pseudo");
        // Checks that the user entered his informations before exiting this window
        d.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (chat.getLocalUser() == null) {
                    System.out.println("[ERROR] Tried entering without entering pseudo or account");
                    System.exit(-2);
                }
            }
        });
        d.setSize(300, 200);
        JPanel panel = new JPanel();
        JLabel labelAccount = new JLabel("Account Name : ");
        JTextField tfAccount = new JTextField(20); // accepts upto 20 characters
        JLabel labelPseudo = new JLabel("Pseudo : ");
        JTextField tfPseudo = new JTextField(20); // accepts upto 20 characters

        JButton enter = new JButton("Enter Chat");
        // Enter chat if pseudo and account info have been filled
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!tfAccount.getText().equals("") && !tfPseudo.getText().equals("")) {
                    enterChat(tfAccount.getText(), tfPseudo.getText());
                    d.setVisible(false);
                    d.dispose();
                }
            }
        });
        panel.add(labelAccount);
        panel.add(tfAccount);
        panel.add(labelPseudo);
        panel.add(tfPseudo);
        panel.add(enter);
        d.add(panel);
        d.setVisible(true);
    }

    // Send a request to open connection with a new user
    // to the ChatMaster
    public void openConnection(String IPAddress) {
        System.out.println("[LOG] Opening connection with " + IPAddress);
        ChatClient client = this.chat.openConnection(IPAddress);
        client.start();
    }

    // Create a new user based on information entered in the first
    // dialog box
    public void enterChat(String account, String pseudo) {
        int id = account.hashCode();
        System.out.println("[LOG] Entered with account : " + account + " and pseudo : " + pseudo);
        System.out.println("[LOG] ID = " + id);
        this.chat.setLocalUser(new User(id, pseudo, this.chat));
    }

    // Function called to discover new available hosts for communication
    // on the network
    public void scanHosts() {
        // TODO
    }
}
