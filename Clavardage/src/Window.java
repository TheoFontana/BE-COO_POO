import java.net.*;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JDialog;
import java.lang.System;
import java.time.LocalDateTime;


public class Window extends JFrame {
    private JPanel chat_panel;
    private JButton connection_button;
    private JPanel connection_panel;
    private JButton deconnection_button;
    private JButton connect_to_server_button;
    private JButton change_pseudo_button;
    private JLabel local_user;
    private JPanel local_user_panel;
    private JTextArea message_area;
    private JPanel message_send_panel;
    JTextPane messages_pane;
    private JTextField nickname_field;
    JList foreignUsers_jlist;
    DefaultListModel<ForeignUser> foreignUsers_list;
    private JButton send_button;
    private JLabel welcome_message;

    // Reference to the ChatMaster
    private ChatMaster chat;

    public Window(ChatMaster cm) {
        this.chat = cm;
        initComponents();
    }

    private void initComponents() {
        setTitle("ChatSystem");
        this.foreignUsers_list = new DefaultListModel<>();
        this.foreignUsers_jlist = new JList(this.foreignUsers_list);
        this.foreignUsers_jlist.setCellRenderer(new CellRenderer());
        this.foreignUsers_jlist.setSelectionMode(0);
        this.foreignUsers_jlist.setBackground(new Color(44, 62, 80));
        this.foreignUsers_jlist.setOpaque(true);
        this.connection_panel = new JPanel(new GridBagLayout());
        GridBagConstraints c_connection_panel = new GridBagConstraints();
        c_connection_panel.insets = new Insets(20, 20, 5, 20);
        this.welcome_message = new JLabel("Welcome !");
        this.nickname_field = new JTextField(30);
        this.connection_button = new JButton("Login");
        this.connection_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!"".equals(Window.this.nickname_field.getText())) {
                    String pseudo = Window.this.nickname_field.getText();
                    if (Window.this.chat.isPseudoTaken(pseudo)) {
                        System.out.println("[ERROR] This pseudo is not available");
                        Window.this.welcome_message.setText("Pseudo not available");
                        return;
                    }
                    Window.this.welcome_message.setText("Welcome !");
                    Window.this.local_user.setText(Window.this.nickname_field.getText());
                    Window.this.getContentPane().remove(Window.this.connection_panel);
                    Window.this.getContentPane().add(Window.this.chat_panel);
                    Window.this.pack();
                    Window.this.setExtendedState(6);
                    // Defines user id with the hash of the ipAddress
                    try {
                        Window.this.chat.setLocalUser(new LocalUser(0, pseudo, Window.this.chat));
                    } catch (Exception ex) {
                        System.out.println("[ERROR] in Window : " + ex.toString());
                        System.exit(-1);
                    }
                    Window.this.chat.discover();
                    if (Window.this.chat.pmsinterface != null) {
                        Window.this.chat.pmsinterface.register(pseudo);
                    }
                    Window.this.nickname_field.setText("");
                    Window.this.messages_pane.setText(Window.this.chat.getLocalUser().getHistory().parseHistory());
                }
            }
        });
        c_connection_panel.gridx = 0;
        c_connection_panel.gridy = 0;
        c_connection_panel.weightx = 0.0d;
        c_connection_panel.weighty = 0.0d;
        c_connection_panel.gridheight = 1;
        this.connection_panel.add(this.welcome_message, c_connection_panel);
        c_connection_panel.gridx = 0;
        c_connection_panel.gridy = 1;
        c_connection_panel.weightx = 0.0d;
        c_connection_panel.weighty = 0.0d;
        c_connection_panel.gridheight = 1;
        this.connection_panel.add(this.nickname_field, c_connection_panel);
        c_connection_panel.gridx = 0;
        c_connection_panel.gridy = 2;
        c_connection_panel.weightx = 0.0d;
        c_connection_panel.weighty = 0.0d;
        c_connection_panel.gridheight = 1;
        this.connection_panel.add(this.connection_button, c_connection_panel);
        add(this.connection_panel);
        this.chat_panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = 1;
        this.local_user = new JLabel("Anonymous");
        this.local_user.setOpaque(true);
        this.deconnection_button = new JButton("Logout");
        this.deconnection_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Window.this.getContentPane().remove(Window.this.chat_panel);
                Window.this.messages_pane.setText("");
                Window.this.foreignUsers_list.clear();
                Window.this.getContentPane().add(Window.this.connection_panel);
                Window.this.pack();
                Window.this.chat.logout();
            }
        });
        this.connect_to_server_button = new JButton("Connect to server");
        this.connect_to_server_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog d = new JDialog(Window.this, "Connect to server");
                d.setSize(400, 100);
                JPanel panel = new JPanel();
                JLabel label = new JLabel("IP : ");
                JTextField tf = new JTextField(20); // accepts upto 20 characters
                JButton send = new JButton("Connect");
                send.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (Window.this.chat.connectToPresenceManagementServer(tf.getText())) {
                            d.dispose();
                        }
                    }
                });
                panel.add(label);
                panel.add(tf);
                panel.add(send);
                d.add(panel);
                d.setVisible(true);
            }
        });
        this.change_pseudo_button = new JButton("Change pseudo");
        this.change_pseudo_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog d = new JDialog(Window.this, "Change pseudo");
                d.setSize(400, 100);
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Pseudo : ");
                JTextField tf = new JTextField(20); // accepts upto 20 characters
                JButton send = new JButton("Change pseudo");
                send.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (Window.this.chat.changeLocalUserPseudo(tf.getText())) {
                            d.dispose();
                        } else {
                            label.setText("Taken !");
                        }
                    }
                });
                panel.add(label);
                panel.add(tf);
                panel.add(send);
                d.add(panel);
                d.setVisible(true);
            }
        });
        this.local_user_panel = new JPanel();
        this.local_user_panel.setLayout(new GridBagLayout());
        GridBagConstraints c_local_user_panel = new GridBagConstraints();
        c_local_user_panel.insets = new Insets(0, 0, 0, 0);
        c_local_user_panel.fill = 1;
        c_local_user_panel.gridx = 0;
        c_local_user_panel.gridy = 0;
        c_local_user_panel.weightx = 1.0d;
        c_local_user_panel.weighty = 1.0d;
        c_local_user_panel.gridheight = 1;
        c_local_user_panel.gridwidth = 1;
        this.local_user_panel.add(this.deconnection_button, c_local_user_panel);
        c_local_user_panel.gridx = 0;
        c_local_user_panel.gridy = 1;
        this.local_user_panel.add(this.connect_to_server_button, c_local_user_panel);
        c_local_user_panel.gridx = 1;
        c_local_user_panel.gridy = 1;
        this.local_user_panel.add(this.change_pseudo_button, c_local_user_panel);
        this.message_area = new JTextArea(3, 30);
        this.send_button = new JButton("Send");
        this.send_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Window.this.foreignUsers_jlist.isSelectionEmpty() && !"".equals(Window.this.message_area.getText())) {
                    String to_display = "->[" + LocalDateTime.now().withNano(0) + "@" + Window.this.foreignUsers_jlist.getSelectedValue().toString() + " (me)] : " + Window.this.message_area.getText();
                    if ("".equals(Window.this.messages_pane.getText())) {
                        Window.this.messages_pane.setText(to_display);
                    } else {
                        Window.this.messages_pane.setText(Window.this.messages_pane.getText() + "\n" + to_display);
                    }
                    Window.this.chat.sendMessage(Window.this.message_area.getText().replaceAll(System.lineSeparator(), ""), (ForeignUser) Window.this.foreignUsers_jlist.getSelectedValue());
                    Window.this.message_area.setText("");
                }
            }
        });
        this.message_send_panel = new JPanel();
        this.message_send_panel.setLayout(new GridBagLayout());
        GridBagConstraints c_message_send_panel = new GridBagConstraints();
        c_message_send_panel.insets = new Insets(0, 0, 0, 0);
        c_message_send_panel.fill = 1;
        c_message_send_panel.gridx = 0;
        c_message_send_panel.gridy = 0;
        c_message_send_panel.weightx = 1.0d;
        c_message_send_panel.weighty = 1.0d;
        c_message_send_panel.gridheight = 1;
        c_message_send_panel.gridwidth = 1;
        this.message_send_panel.add(this.message_area, c_message_send_panel);
        c_message_send_panel.gridx = 1;
        c_message_send_panel.gridy = 0;
        c_message_send_panel.weightx = 0.0d;
        c_message_send_panel.weighty = 1.0d;
        c_message_send_panel.gridheight = 1;
        this.message_send_panel.add(this.send_button, c_message_send_panel);
        c_message_send_panel.gridx = 2;
        c_message_send_panel.gridy = 0;
        c_message_send_panel.weightx = 0.0d;
        c_message_send_panel.weighty = 1.0d;
        c_message_send_panel.gridheight = 1;
        this.messages_pane = new JTextPane();
        this.messages_pane.setEditable(false);
        this.messages_pane.setBackground(new Color(236, 240, 241));
        this.messages_pane.setOpaque(true);
        JScrollPane scroll_pane_messages_list = new JScrollPane();
        scroll_pane_messages_list.setViewportView(this.messages_pane);
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.0d;
        c.weighty = 0.0d;
        c.gridheight = 1;
        this.chat_panel.add(this.local_user_panel, c);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1d;
        c.weighty = 1.0d;
        c.gridheight = 2;
        this.chat_panel.add(this.foreignUsers_jlist, c);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0d;
        c.weighty = 1.0d;
        c.gridheight = 2;
        this.chat_panel.add(scroll_pane_messages_list, c);
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1.0d;
        c.weighty = 0.0d;
        c.gridheight = 1;
        this.chat_panel.add(this.message_send_panel, c);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
        pack();
    }
}
