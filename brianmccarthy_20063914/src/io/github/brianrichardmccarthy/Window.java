package io.github.brianrichardmccarthy;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Window extends JFrame {
	/**
	 * Class variables
	 */
	private static final long serialVersionUID = 1L;
	private JTextField genderTxt;
	private JTextField salaryTxt;
	private JTextField addressTxt;
	private JTextField nameTxt;
	private JTextField dobTxt;
	private JTextField ssnTxt;
	private Database database;

	/**
	 * Generates a constraint for the grid bag layout
	 * 
	 * @param insets    (padding)
	 * @param x         (grid x)
	 * @param y         (grid y)
	 * @param gridWidth (number of cells)
	 * @param fill      (used when component display area is larger than the default
	 *                  size)
	 * @return
	 */
	private GridBagConstraints constraints(Insets insets, int x, int y, int gridWidth, int fill) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = insets;
		constraints.gridx = x;
		constraints.gridy = y;

		if (gridWidth > 0)
			constraints.gridwidth = gridWidth;
		if (fill > 0)
			constraints.fill = fill;

		return constraints;
	}

	/**
	 * Create the frame.
	 */
	public Window() {
		database = new Database();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 770, 350);
		setMinimumSize(new Dimension(670, 300));
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 157, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		// add the labels to the panel
		contentPane.add(new JLabel("Employee Details"), constraints(new Insets(0, 0, 5, 5), 3, 0, -1, -1));
		contentPane.add(new JLabel("Ssn"), constraints(new Insets(0, 0, 5, 5), 1, 1, -1, -1));
		contentPane.add(new JLabel("DOB (yyyy-mm-dd)"), constraints(new Insets(0, 0, 5, 5), 1, 2, -1, -1));
		contentPane.add(new JLabel("Address"), constraints(new Insets(0, 0, 5, 5), 1, 5, -1, -1));
		contentPane.add(new JLabel("Gender"), constraints(new Insets(0, 0, 5, 5), 1, 8, -1, -1));
		contentPane.add(new JLabel("Name"), constraints(new Insets(0, 0, 5, 5), 1, 4, -1, -1));
		contentPane.add(new JLabel("Salary"), constraints(new Insets(0, 0, 5, 5), 1, 7, -1, -1));

		// add the text fields to the panel
		ssnTxt = new JTextField();
		ssnTxt.setEditable(false);
		contentPane.add(ssnTxt, constraints(new Insets(0, 0, 5, 5), 3, 1, 3, GridBagConstraints.HORIZONTAL));
		ssnTxt.setColumns(10);

		dobTxt = new JTextField();
		contentPane.add(dobTxt, constraints(new Insets(0, 0, 5, 5), 3, 2, 3, GridBagConstraints.HORIZONTAL));
		dobTxt.setColumns(10);

		nameTxt = new JTextField();
		nameTxt.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				// if the text in the text box does not match the regex for lower and upper and spaces only
				// clear the text box
				if (!nameTxt.getText().matches("^[a-zA-Z ]+$")) nameTxt.setText("");
			}
		});
		contentPane.add(nameTxt, constraints(new Insets(0, 0, 5, 5), 3, 4, 3, GridBagConstraints.HORIZONTAL));
		nameTxt.setColumns(10);

		addressTxt = new JTextField();
		addressTxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				// if the text in the text box does not match the regex for lower and upper letters and numbers and spaces only
				// clear the text box
				if (!addressTxt.getText().matches("^[a-zA-Z0-9 ]+$")) addressTxt.setText("");
			}
		});
		contentPane.add(addressTxt, constraints(new Insets(0, 0, 5, 5), 3, 5, 3, GridBagConstraints.HORIZONTAL));
		addressTxt.setColumns(10);

		salaryTxt = new JTextField();
		salaryTxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				// if the text in the text box does not match the regex for dot, and numbers only
				// clear the text box
				if (!salaryTxt.getText().matches("^[.0-9]+$")) salaryTxt.setText("");
			}
		});
		contentPane.add(salaryTxt, constraints(new Insets(0, 0, 5, 5), 3, 7, 3, GridBagConstraints.HORIZONTAL));
		salaryTxt.setColumns(10);

		genderTxt = new JTextField();
		genderTxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				// if the text in the text box does not match the regex for lower and upper letters and spaces only
				// clear the text box
				if (!genderTxt.getText().matches("^[a-zA-Z ]+$")) genderTxt.setText("");
			}
		});
		contentPane.add(genderTxt, constraints(new Insets(0, 0, 5, 5), 3, 8, 3, GridBagConstraints.HORIZONTAL));
		genderTxt.setColumns(10);

		// add the buttons to the panel
		JButton preBtn = new JButton("Previous");
		// show the previous employee when this button is pressed
		preBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				setTableFields(database.getPrevEmployee());
			}
		});
		contentPane.add(preBtn, constraints(new Insets(0, 0, 5, 5), 7, 3, -1, -1));

		JButton NextBtn = new JButton("Next");
		// show the next employee when this button is pressed
		NextBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				setTableFields(database.getNextEmployee());
			}
		});
		contentPane.add(NextBtn, constraints(new Insets(0, 0, 5, 5), 7, 5, -1, -1));


		// Clear all the text fields
		JButton clearBtn = new JButton("Clear");
		clearBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				genderTxt.setText("");
				nameTxt.setText("");
				addressTxt.setText("");
				dobTxt.setText("");
				ssnTxt.setText("");
				salaryTxt.setText("");
			}
		});
		contentPane.add(clearBtn, constraints(new Insets(0, 0, 5, 5), 7, 7, -1, -1));


		// Adds the current employee details as a new employee if the date is in the correct format, and none of the fields are blank
		JButton addBtn = new JButton("Add");
		addBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!dobTxt.getText().matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))"))
					dobTxt.setText("");
				if (genderTxt.getText().length() == 0 || salaryTxt.getText().length() == 0
						|| addressTxt.getText().length() == 0 || dobTxt.getText().length() == 0
						|| nameTxt.getText().length() == 0)
					return;

				database.insert(
						(nameTxt.getText().length() > 90) ? nameTxt.getText().substring(0, 90) : nameTxt.getText(),
						dobTxt.getText(),
						(addressTxt.getText().length() > 90) ? addressTxt.getText().substring(0, 90)
								: addressTxt.getText(),
						Double.parseDouble(salaryTxt.getText()),
						(nameTxt.getText().length() > 90) ? genderTxt.getText().substring(0, 90) : genderTxt.getText());
			}
		});
		contentPane.add(addBtn, constraints(new Insets(0, 0, 0, 5), 3, 10, -1, -1));

		// deletes the current employee record and shows the previous employee record (if the ssn text box has a value)
		JButton deleteBtn = new JButton("Delete");
		deleteBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!ssnTxt.getText().isEmpty()) {
					database.delete(Integer.parseInt(ssnTxt.getText()));
					setTableFields(database.getPrevEmployee());
				}
			}
		});
		contentPane.add(deleteBtn, constraints(new Insets(0, 0, 0, 5), 5, 10, -1, -1));

		// updates the current employee details if the date is in the correct format, and none of the fields are blank
		JButton updateBtn = new JButton("Update");
		updateBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!dobTxt.getText().matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))"))
					dobTxt.setText("");
				if (genderTxt.getText().length() == 0 || salaryTxt.getText().length() == 0
						|| addressTxt.getText().length() == 0 || dobTxt.getText().length() == 0
						|| nameTxt.getText().length() == 0)
					return;
				database.update(Integer.parseInt(ssnTxt.getText()),
						(nameTxt.getText().length() > 90) ? nameTxt.getText().substring(0, 90) : nameTxt.getText(),
						dobTxt.getText(),
						(addressTxt.getText().length() > 90) ? addressTxt.getText().substring(0, 90)
								: addressTxt.getText(),
						Double.parseDouble(salaryTxt.getText()),
						(nameTxt.getText().length() > 90) ? genderTxt.getText().substring(0, 90) : genderTxt.getText());
			}
		});
		setTableFields(database.getCurrentEmployee());

		contentPane.add(updateBtn, constraints(new Insets(0, 0, 0, 5), 7, 10, -1, -1));
	}

	/**
	* Updates the text fields to show the given employee details
	*/	
	private void setTableFields(Employee e) {
		ssnTxt.setText(Integer.toString(e.getSsn()));
		nameTxt.setText(e.getName());
		dobTxt.setText(e.getDob());
		addressTxt.setText(e.getAddress());
		salaryTxt.setText(Double.toString(e.getSalary()));
		genderTxt.setText(e.getGender());
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
