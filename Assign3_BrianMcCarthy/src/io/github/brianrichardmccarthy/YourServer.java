/**
 * 
 * @author Brian McCarthy
 * @version 1.0
 * @date 14/12/2018
 * 
 */
package io.github.brianrichardmccarthy;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * 
 * RMI Calculator class implements the Calculator interface.
 *
 */
public class YourServer extends UnicastRemoteObject implements YourIF {


	/**
	 * Class variables 
	 */
	private static final long serialVersionUID = 1L;
	private ServerGUI view;
	
	/**
	 * 
	 * GUI for the server
	 * 
	 */
	private class ServerGUI extends JFrame {
		private static final long serialVersionUID = 1L;
		private JTextArea messageLog;
		
		/**
		 * Default Constructor
		 */
		public ServerGUI() {
			// Create a basic GUI with a text area
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 450, 300);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			GridBagLayout gbl_contentPane = new GridBagLayout();
			gbl_contentPane.columnWidths = new int[] { 0, 0 };
			gbl_contentPane.rowHeights = new int[] { 0, 0 };
			gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gbl_contentPane.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
			contentPane.setLayout(gbl_contentPane);

			messageLog = new JTextArea();
			messageLog.setEditable(false);
			GridBagConstraints gbc_messageLog = new GridBagConstraints();
			gbc_messageLog.fill = GridBagConstraints.BOTH;
			gbc_messageLog.gridx = 0;
			gbc_messageLog.gridy = 0;
			contentPane.add(messageLog, gbc_messageLog);
			setTitle("Calculator Server");
			setVisible(true);
		}
		
		/**
		 * 
		 * @param text (String) text to be appended to the text area
		 */
		public void append(String text) {
			messageLog.append(text);
		}
		
	}

	/**
	 * Default Constructor
	 * @throws RemoteException
	 */
	public YourServer() throws RemoteException {
		super();
		view = new ServerGUI();
	}

	@Override
	public int add(int x, int y) throws RemoteException {
		try {
			view.append("Request from " + getClientHost() + "\nOpnd 1: " + x + "\nOpnd 2: " + y + "\nOprtr: +\nData to Client: " + (x+y));
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return x + y;
	}

	@Override
	public int subtract(int x, int y) throws RemoteException {
		try {
			view.append("Request from " + getClientHost() + "\nOpnd 1: " + x + "\nOpnd 2: " + y + "\nOprtr: -\nData to Client: " + (x-y));
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return x - y;
	}

	@Override
	public int divide(int x, int y) throws RemoteException {
		try {
			view.append("Request from " + getClientHost() + "\nOpnd 1: " + x + "\nOpnd 2: " + y + "\nOprtr: /\nData to Client: " + (x/y));
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return x / y;
	}

	@Override
	public int mulitply(int x, int y) throws RemoteException {
		try {
			view.append("Request from " + getClientHost() + "\nOpnd 1: " + x + "\nOpnd 2: " + y + "\nOprtr: *\nData to Client: " + (x*y));
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return x * y;
	}

	@Override
	public double add(double x, double y) throws RemoteException {
		try {
			view.append("Request from " + getClientHost() + "\nOpnd 1: " + x + "\nOpnd 2: " + y + "\nOprtr: +\nData to Client: " + (x+y));
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return x + y;
	}

	@Override
	public double subtract(double x, double y) throws RemoteException {		
		try {
			view.append("Request from " + getClientHost() + "\nOpnd 1: " + x + "\nOpnd 2: " + y + "\nOprtr: -\nData to Client: " + (x-y));
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return x - y;
	}

	@Override
	public double divide(double x, double y) throws RemoteException {
		try {
			view.append("Request from " + getClientHost() + "\nOpnd 1: " + x + "\nOpnd 2: " + y + "\nOprtr: /\nData to Client: " + (x/y));
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return x / y;
	}

	@Override
	public double mulitply(double x, double y) throws RemoteException {
		try {
			view.append("Request from " + getClientHost() + "\nOpnd 1: " + x + "\nOpnd 2: " + y + "\nOprtr: *\nData to Client: " + (x*y));
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return x * y;
	}
	
	@Override
	public double tan(double x) throws RemoteException {
		try {
			view.append("Request from " + getClientHost() + "tan(" + x + ")");
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return Math.tan(x);
	}
	
	@Override
	public double cos(double x) throws RemoteException {
		try {
			view.append("Request from " + getClientHost() + "cos(" + x + ")");
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return Math.cos(x);
	}
	
	@Override
	public double sqrt(double x) throws RemoteException {
		try {
			view.append("Request from " + getClientHost() + "sqrt(" + x + ")");
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return Math.sqrt(x);
	}
	
	@Override
	public double sin(double x) throws RemoteException {
		try {
			view.append("Request from " + getClientHost() + "sin(" + x + ")");
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return Math.sin(x);
	}
	
	@Override
	public double power(double base, double exponent) throws RemoteException {
		try {
			view.append("Request from " + getClientHost() + base + "^" + exponent);
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return Math.pow(base, exponent);
	}
	
	/**
	 * Main method for the server
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			YourServer obj = new YourServer();
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("CalculatorServer", obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}