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
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class MultiThreadedServerA2 extends JFrame {

	/**
	 * 
	 * @author Brian McCarthy
	 * Client class
	 */
	private class Client extends Thread {

		// fields from the database
		private String sid;
		private String studId;
		private String fName;
		private String lName;
		
		// socket, input/ output and INet address fields
		private Socket socket;
		private InetAddress address;
		private DataInputStream inputFromClient;
		private DataOutputStream outputToClient;

		/**
		 * Constructor
		 * @param socket (Socket) socket the client is connected on
		 * @param sid (String) student id (primary key)
		 * @param studId (String) student number/ id
		 * @param fname (String) student first name
		 * @param lname (String) student last name
		 */
		public Client(Socket socket, String sid, String studId, String fName, String lName) {
			try {
				// initalise variables
				this.socket = socket;
				this.sid = sid;
				this.studId = studId;
				this.fName = fName;
				this.lName = lName;
				
				// pull input, output and INet from the socket
				address = this.socket.getInetAddress();
				inputFromClient = new DataInputStream(socket.getInputStream());
				outputToClient = new DataOutputStream(socket.getOutputStream());
			} catch (Exception e) {
				// catch any error and display it to the message log
				messageLog.append("Client " + studId + e.getMessage() + "\n");
			}
		}
		
		/**
		 * Gets the students' full name (first name & second name)
		 * @return (String)
		 */
		public String getFullName() {
			return fName + " " + lName;
		}
		
		/**
		 * Sends the parameter to the client
		 * @param message (String)
		 */
		public void send(String message) {
			try {
				// write the message to the data out put stream and flush the buffer
				outputToClient.writeUTF(message);
				outputToClient.flush();
			} catch (IOException e) {
				// write any error to the message log
				messageLog.append("Client " + studId + e.getMessage() + "\n");
			}			
		}
		
		/**
		 * Method to listen for messages from the client
		 */
		public void run() {
			// loop forever
			while (true) {
				try {
					// when a message from the client is received write it to the message log
					messageLog.append("Client " + studId + ": " +  inputFromClient.readUTF() + "\n");
				} catch (Exception e) {
					// write any error to the message log
					messageLog.append("Client " + studId + e.getMessage() + "\n");
				}
			}
		}
		
		@Override
		/**
		 * @return String toString representation of this class
		 */
		public String toString() {
			return "IP address = " + address.getHostAddress() + "\nStudent Id = " + studId + "\nSID = " + sid + "\nName = " + getName()  + "\n";
		}
		
	}


	private static final long serialVersionUID = 1L;

	// Server socket
	private ServerSocket serverSocket;
	
	// swing/ GUI fields
	private JPanel contentPane;
	private JTextField newMessage;
	private JTextArea messageLog;
	
	// list of clients
	private ArrayList<Client> clients;
	
	// MySQL variables
	private Connection conn;
	private final String mysqlPassword = "";
	private final String mysqlUsername = "root";
	private final String serverName = "localhost";
	private final String portNumber = "3306";
	private final String dbName = "Assign2";
	
	public static void main(String[] args) {
		MultiThreadedServerA2 frame = new MultiThreadedServerA2();
		frame.run();
	}

	/**
	 * Create the frame.
	 */
	public MultiThreadedServerA2() {
		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		messageLog = new JTextArea();
		messageLog.setEditable(false);
		GridBagConstraints gbc_messageLog = new GridBagConstraints();
		gbc_messageLog.gridwidth = 13;
		gbc_messageLog.gridheight = 5;
		gbc_messageLog.insets = new Insets(0, 0, 5, 5);
		gbc_messageLog.fill = GridBagConstraints.BOTH;
		gbc_messageLog.gridx = 0;
		gbc_messageLog.gridy = 0;
		contentPane.add(messageLog, gbc_messageLog);

		newMessage = new JTextField();
		newMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					send();
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
		sendBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				send();
			}
		});
		GridBagConstraints gbc_sendBtn = new GridBagConstraints();
		gbc_sendBtn.gridx = 12;
		gbc_sendBtn.gridy = 7;
		contentPane.add(sendBtn, gbc_sendBtn);
		setVisible(true);
		clients = new ArrayList<Client>();
		try {
			serverSocket = new ServerSocket(1090);
		} catch (IOException e) {
			messageLog.append(e.getMessage() + "\n");
		}
		
		try {
			Properties prop = new Properties();
			prop.put("user", mysqlUsername);
			prop.put("password", mysqlPassword);
			conn = DriverManager.getConnection("jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName, prop);
		} catch (SQLException e) {
			messageLog.append(e.getMessage() + "\n");
		}
		
	}

	/**
	 * Sends the text in the text field to each of the clients
	 */
	private void send() {
		// loop through all clients and send them the message in the text field
		for (Client c : clients) c.send(newMessage.getText());
		// write the message to the server
		messageLog.append(newMessage.getText() + "\n");
	}
	
	/**
	 * Lets new clients connected, prompt them for a student number and check the database for the student an shows a message to the client whether their student number exists or not
	 */
	public void run() {
		
		try {
			while (true) {
				// Accept a connection request
				Socket socket = serverSocket.accept();
				// setup a output stream to prompt the user for their student number
				DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataOutputStream.writeUTF("Please enter your student number");
				dataOutputStream.flush();
				
				// listen for the new user input
				DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
				
				// sql result
				ResultSet resultSet = null;
				
				// string for the user input
				String studId = "";
				
				try {
					// get student number
					studId = dataInputStream.readUTF();
					
					// check if the given student number exists in the database
					PreparedStatement stmt = conn.prepareStatement("select * from myStudents where STUD_ID = " + Integer.parseInt(studId) + ";");
					resultSet = stmt.executeQuery();
				} catch (SQLException e) {
					// catch any error
					messageLog.append(e.getMessage() + "\n");
				}
				
				// if a user is found
				if (resultSet.next()) {
					// create a new client
					Client client = new Client(socket, Integer.toString(resultSet.getInt(1)), Integer.toString(resultSet.getInt(2)), resultSet.getString(3), resultSet.getString(4));
					// start their thread
					client.start();
					// add to the list of clients
					clients.add(client);
					// send an message welcoming the connected user
					clients.get(clients.size()-1).send("Client " + "" + ": Welcome " + client.getFullName() + ", you are now connected.");
				} else {
					// user does not exist
					// write a message to the client telling them they were unsuccessful
					dataOutputStream.writeUTF("Client " + studId + ": Sorry " + studId + ", you are not a registered student.");
					dataOutputStream.flush();
					// close the output stream and the socket
					dataOutputStream.close();
					socket.close();
				}
			}
		} catch (Exception e) {
			// catch any errors and write them to the message log
			messageLog.append("Client " + e.getMessage() + "\n");
		}
	}

}