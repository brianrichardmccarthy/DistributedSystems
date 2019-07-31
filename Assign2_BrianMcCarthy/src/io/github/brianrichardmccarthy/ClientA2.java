package io.github.brianrichardmccarthy;
/**
 * @author Brian McCarthy
 */

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientA2 extends JFrame {

	private static final long serialVersionUID = 1L;

	// text field for any message that the client wants to send to the server
	private JTextField newMessage;
	
	// text area for the previous messages either from the server or client
	private JTextArea messageLog;
	
	// socket for the connection to the server
	private Socket socket;
	
	// data output stream to send messages to the server
	private DataOutputStream toServer;
	
	// data input stream to get messages from the server
	private DataInputStream fromServer;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ClientA2 frame = new ClientA2();
		frame.run();
	}

	/**
	 * Create the frame.
	 */
	public ClientA2() {
		
		// setup the GUI
		setTitle("Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		// Layout the components with a grid bag layout
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		messageLog = new JTextArea();
		
		// prevent the user from typing in messages into the text area
		messageLog.setEditable(false);
		GridBagConstraints gbc_messageLog = new GridBagConstraints();
		gbc_messageLog.gridheight = 7;
		gbc_messageLog.gridwidth = 13;
		gbc_messageLog.insets = new Insets(0, 0, 5, 0);
		gbc_messageLog.fill = GridBagConstraints.BOTH;
		gbc_messageLog.gridx = 0;
		gbc_messageLog.gridy = 0;
		contentPane.add(messageLog, gbc_messageLog);
		
		newMessage = new JTextField();
		
		// in the text field listen for key events
		newMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// if the user presses the enter key, call the send method
				if (e.getKeyChar() == KeyEvent.VK_ENTER) send();
			}
		});
		GridBagConstraints gbc_newMessage = new GridBagConstraints();
		gbc_newMessage.gridwidth = 12;
		gbc_newMessage.insets = new Insets(0, 0, 0, 5);
		gbc_newMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_newMessage.gridx = 0;
		gbc_newMessage.gridy = 7;
		contentPane.add(newMessage, gbc_newMessage);
		newMessage.setColumns(10);
		
		JButton sendBtn = new JButton("Send");
		// listen for send button clicks
		sendBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// when clicked, call the send method
				send();
			}
		});
		GridBagConstraints gbc_sendBtn = new GridBagConstraints();
		gbc_sendBtn.gridx = 12;
		gbc_sendBtn.gridy = 7;
		contentPane.add(sendBtn, gbc_sendBtn);
		setVisible(true);
		try {
			// initialise the socket and the input/ output streams
			socket = new Socket("localhost", 1090);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			// if there is any error write to the message log
			messageLog.append(e.getMessage() + "\n");
		}
		
	}
	
	private void run() {
		try {
			while (true) {
				// when the client receives a message from the server, write it to the message log
				messageLog.append("Server: " + fromServer.readUTF() + "\n");
			}
		} catch (Exception e) {
			// if there is any error write to the message log
			messageLog.append(e.getMessage() + "\n");
		}
	}
	
	private void send() {
		try {
			// write the message to the text area
			messageLog.append(newMessage.getText() + "\n");
			
			// write the message to the server and flush the buffer
			toServer.writeUTF(newMessage.getText());
			toServer.flush();
		} catch (Exception e) {
			// if there is any error write to the message log
			messageLog.append(e.getMessage() + "\n");
		}
	}

}
