import javax.swing.JPanel;
import javax.swing.JInternalFrame;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;

public class chatPanel extends JPanel {
	private JTextField message;

	/**
	 * Create the panel.
	 */
	public chatPanel() {
		setLayout(null);
		
		JLabel messageLabel = new JLabel("Message");
		messageLabel.setBounds(6, 273, 61, 16);
		add(messageLabel);
		
		message = new JTextField();
		message.setBounds(79, 268, 247, 26);
		add(message);
		message.setColumns(10);
		
		JButton sendButton = new JButton("Send");
		sendButton.setBounds(327, 268, 117, 29);
		add(sendButton);
		
		JLabel otherUser = new JLabel("User");
		otherUser.setBounds(32, 17, 61, 16);
		add(otherUser);
		
		JList history = new JList();
		history.setBounds(6, 45, 438, 211);
		add(history);

	}
}
