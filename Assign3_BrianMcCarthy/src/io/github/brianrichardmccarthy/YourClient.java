/**
 * 
 * @author Brian McCarthy
 * @version 1.0
 * @date 14/12/2018
 * 
 */
package io.github.brianrichardmccarthy;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class YourClient extends JFrame {

	/**
	 * 
	 * 
	 *
	 */
	private class ClientModel {
		private String equation, number;
		private Double ans;
		
		public ClientModel() {
			equation = "";
			number = "";
		}
		
		public String getEquation() {
			return equation;
		}
		
		public String getNumber() {
			return number;
		}
		
		public void setEquation(String equation) {
			this.equation = equation;
		}
		
		public void setSignedNumber() {
			number = (number.startsWith("-")) ? number.substring(1) : "-" + number;
		}
		
		public void setNumber(String number) {
			this.number = number;
		}
		
		public void setAnswer(Double answer) {
			ans = answer;
		}
		
		@Override
		public String toString() {
			String s = "" + equation + number;
			if (ans != null) s += (" = " + ans.toString());
			return s;
		}
		
	}
	
	private class ClientController {
		private ClientModel model;
		private YourIF calculatorSever;
		private YourClient view;
		
		public ClientController(YourClient view) {
			this.view = view;
			model = new ClientModel();
			try {
				calculatorSever = (YourIF)Naming.lookup("//localhost/CalculatorServer");
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		
		/*
		 * */
		
		/**
		 * 
		 * returns if the open bracket is the same type as the closing one.
		 * 
		 * @param open
		 *            (char)
		 * @param close
		 *            (char)
		 * @return (boolean)
		 */
		private boolean isMatch(char open, char close) {
			return (open == '(' && close == ')') || (open == '{' && close == '}') || (open == '[' && close == ']');
		}

		/**
		 * Checks if all the brackets match.
		 * 
		 * @param line
		 *            (String)
		 * @return boolean
		 */
		private boolean bracesMatch(String line) {
			boolean isBalanced = true;
			ArrayList<Character> balance = new ArrayList<>();

			/*
			 * loop through all the characters of the string if it's an open bracket
			 * push it onto the stack else if it's a closing bracket check if the
			 * brackets match
			 */
			for (int x = 0; x < line.length() && isBalanced; x++) {
				switch (line.charAt(x)) {
				case '(':
				case '{':
				case '[':
					balance.add(line.charAt(x));
					break;
				case ']':
				case '}':
				case ')':
					isBalanced = isMatch(balance.remove(balance.size()-1), line.charAt(x));
					break;
				default:
					continue;
				}
			}

			// if there is brackets left on the stack
			return balance.isEmpty();
		}

		/**
		 * 
		 * @param text
		 */
		public void buttonCLicked(String text) {
			if (text.equals("+ / -"))  {
				model.setSignedNumber();
			} else if (text.equals("sin") || text.equals("cos") || text.equals("tan") || text.equals("sqrt")) {
				model.setEquation(model.getEquation() + text + " ( ");
			} else if ((Character.isDigit(text.charAt(0)) || text.length() > 1 && Character.isDigit(text.charAt(1)))) {
				model.setNumber(model.getNumber() + text);
			} else if (text.equals(".")) {
				if (model.getNumber().length() == 0 || model.getNumber().length() == 1 && !Character.isDigit(model.getNumber().charAt(0))) {
					model.setNumber(model.getNumber() + "0.");
				} else {
					model.setNumber(model.getNumber() + text);
				}
			} else if (text.startsWith("C")) {
				model.setEquation("");
				model.setNumber("");
			} else if (text.startsWith("=")) {
				model.setEquation(model.getEquation() + model.getNumber() + " " + text + " ");
				model.setNumber("");
				solve();
			} else if (text.equals("(")) {
				model.setEquation(model.getEquation() + " " + text + " ");
			} else if (text.equals(")")) {
				model.setEquation(model.getEquation() + model.getNumber() + " " + text);
				model.setNumber("");
			} else {
				model.setEquation(model.getEquation() + model.getNumber() + " " + text + " ");
				model.setNumber("");
			}
			
		}
		
		/**
		 * Checks which of the two given operators has precedence
		 * 
		 * @param topOfStackCharacter
		 *            (char)
		 * @param nextCharacter
		 *            (char)
		 * @return (boolean)
		 */
		private boolean hasPrecedence(String topOfStackCharacter, String nextCharacter) {
			return getPrecedenced(topOfStackCharacter) >= getPrecedenced(nextCharacter);
		}

		/**
		 * 
		 * Returns a number based on the operator in a given string
		 * 
		 * @param operator
		 *            (String)
		 * @return (int)
		 */
		private int getPrecedenced(String operator) {
			if (operator == null) {
				return 0;
			}

			switch (operator) {
				case "(":
				case ")":
					return 5;
				case "sin":
				case "cos":
				case "tan":
				case "sqrt":
					return 4;
				case "^":
					return 3;
				case "*":
				case "/":
					return 2;
				default:
					return 1;
			}
		}
		
		/**
		 * Converts a infix string to postfix e.g input -> 2 + 2 output 2 2 +
		 * @return (String)
		 */
		private String shuntingYard() {
			// check that the brackets match
			if (!bracesMatch(model.getEquation())) return "";
			
			String[] equation = model.getEquation().split("\\s+");
			ArrayList<String> stack =  new ArrayList<String>();
			String postfix = "";
			
			// loop throught the array
			for (String s : equation) {
					// check if the string is positive or negative string
					if (Character.isDigit(s.charAt(0)) || (s.length() > 1 && Character.isDigit(s.charAt(1)))) {
						postfix += (s + " ");
					} else {
						// else the string is a operator
						switch (s) {
						
							// if it's a power or open bracket
							case "^":
							case "(":
								// append to the stack
								stack.add(s);
								break;
							case ")":
								// if it's a closing bracket
								// loop thought stack of operands until it's empty or the top of the stack is an open bracket
								while (!stack.isEmpty() && !stack.get(stack.size()-1).equals("(")) postfix += stack.remove(stack.size()-1) + " "; // add the operand to the string, and remove it from the stack
								
								// check if the stack is not empty and an open bracket is at the top of the stack and remove it
								if (!stack.isEmpty() && stack.get(stack.size() - 1).equals("(")) stack.remove(stack.size()-1);
								break;
							case "+":
							case "-":
							case "*":
							case "/":
							case "sin":
							case "cos":
							case "tan":
							case "sqrt":
								while (!stack.isEmpty() && hasPrecedence(stack.get(stack.size()-1), s)) {
									
									if (stack.get(stack.size()-1).equals("(")) {
										stack.remove(stack.size() - 1);
										break;
									} 
									
									postfix += stack.remove(stack.size()-1) + " ";
								}
								stack.add(s);
								break;
						}
					}
				}
				
			
			while (!stack.isEmpty()) postfix += (stack.remove(stack.size()-1) + " ");
			return postfix;
		}
		
		/**
		 * Uses the postfix string from shunting yard method to calculate a result of the given expression
		 */
		public void solve() {
			ArrayList<Double> operands = new ArrayList<>();
			
			// get the postfix string
			String postfix = shuntingYard();
			
			// check the postfix length, if it's zero then the open brackets didn't have a closing bracket
			if (postfix.length() == 0) {
				view.resultArea.setText(null);
				view.resultArea.setText("Brackets didn't match");
				return;
			}
			
			// split the equation by space
			String[] equation = postfix.split("\\s+");
			
			// loop throught the array
			for (String s : equation) {
				// check if the string is a positive or negative number
				if (Character.isDigit(s.charAt(0)) || (s.length() > 1 && Character.isDigit(s.charAt(1))) ) {
					operands.add(Double.parseDouble(s));
				} else {
					// else the string is an operator
					// so call the correct method
					switch(s) {
						case "+":
							try {
								operands.add(calculatorSever.add(operands.remove(operands.size()-1), operands.remove(operands.size()-1)));
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							break;
						case "-":
							try {
								operands.add(calculatorSever.subtract(operands.remove(operands.size()-1), operands.remove(operands.size()-1)));
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							break;
						case "*":
							try {
								operands.add(calculatorSever.mulitply(operands.remove(operands.size()-1), operands.remove(operands.size()-1)));
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							break;
						case "/":
							try {
								double one = operands.remove(operands.size()-1);
								double two = operands.remove(operands.size()-1);
								operands.add(calculatorSever.divide(two, one));
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							break;
						case "sin":
							try {
								operands.add(calculatorSever.sin(operands.remove(operands.size()-1)));
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							break;
						case "sqrt":
							try {
								operands.add(calculatorSever.sqrt(operands.remove(operands.size()-1)));
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							break;
						case "tan":
							try {
								operands.add(calculatorSever.tan(operands.remove(operands.size()-1)));
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							break;
						case "^":
							try {
								operands.add(calculatorSever.mulitply(operands.remove(operands.size()-1), operands.remove(operands.size()-1)));
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							break;
						case "cos":
							try {
								operands.add(calculatorSever.cos(operands.remove(operands.size()-1)));
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							break;
						default:
							System.out.println("Error" + s);
							break;
							
					}
				}
			}
			
			model.setAnswer(operands.get(0));
			
		}
			
		@Override
		public String toString() {
			return model.toString();
		}
		
	}
	
	// class variables
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ClientController controller;
	private JTextArea resultArea;
	
	/**
	 * Default Constructor, sets up the GUI
	 */
	public YourClient() {
		controller = new ClientController(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 547, 402);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPane.rowHeights =   new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
		contentPane.setLayout(gbl_contentPane);
		
		resultArea = new JTextArea();
		resultArea.setEnabled(false);
		GridBagConstraints gbc_resultArea = new GridBagConstraints();
		gbc_resultArea.gridwidth = 4;
		gbc_resultArea.insets = new Insets(0, 0, 5, 6);
		gbc_resultArea.fill = GridBagConstraints.BOTH;
		gbc_resultArea.gridx = 0;
		gbc_resultArea.gridy = 0;
		contentPane.add(resultArea, gbc_resultArea);
		
		JButton btnSeven = new JButton("7");
		btnSeven.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("7");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnSeven = new GridBagConstraints();
		gbc_btnSeven.fill = GridBagConstraints.BOTH;
		gbc_btnSeven.insets = new Insets(0, 0, 5, 5);
		gbc_btnSeven.gridx = 0;
		gbc_btnSeven.gridy = 1;
		contentPane.add(btnSeven, gbc_btnSeven);
		
		JButton btnEight = new JButton("8");
		btnEight.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("8");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnEight = new GridBagConstraints();
		gbc_btnEight.fill = GridBagConstraints.BOTH;
		gbc_btnEight.insets = new Insets(0, 0, 5, 5);
		gbc_btnEight.gridx = 1;
		gbc_btnEight.gridy = 1;
		contentPane.add(btnEight, gbc_btnEight);
		
		JButton btnNine = new JButton("9");
		btnNine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("9");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnNine = new GridBagConstraints();
		gbc_btnNine.fill = GridBagConstraints.BOTH;
		gbc_btnNine.insets = new Insets(0, 0, 5, 5);
		gbc_btnNine.gridx = 2;
		gbc_btnNine.gridy = 1;
		contentPane.add(btnNine, gbc_btnNine);
		
		JButton btnMulitply = new JButton("*");
		btnMulitply.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("*");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnMulitply = new GridBagConstraints();
		gbc_btnMulitply.fill = GridBagConstraints.BOTH;
		gbc_btnMulitply.insets = new Insets(0, 0, 5, 0);
		gbc_btnMulitply.gridx = 3;
		gbc_btnMulitply.gridy = 1;
		contentPane.add(btnMulitply, gbc_btnMulitply);
		
		JButton btnFour = new JButton("4");
		btnFour.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("4");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnFour = new GridBagConstraints();
		gbc_btnFour.fill = GridBagConstraints.BOTH;
		gbc_btnFour.insets = new Insets(0, 0, 5, 5);
		gbc_btnFour.gridx = 0;
		gbc_btnFour.gridy = 2;
		contentPane.add(btnFour, gbc_btnFour);
		
		JButton btnFive = new JButton("5");
		btnFive.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("5");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnFive = new GridBagConstraints();
		gbc_btnFive.fill = GridBagConstraints.BOTH;
		gbc_btnFive.insets = new Insets(0, 0, 5, 5);
		gbc_btnFive.gridx = 1;
		gbc_btnFive.gridy = 2;
		contentPane.add(btnFive, gbc_btnFive);
		
		JButton btnSix = new JButton("6");
		btnSix.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("6");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnSix = new GridBagConstraints();
		gbc_btnSix.fill = GridBagConstraints.BOTH;
		gbc_btnSix.insets = new Insets(0, 0, 5, 5);
		gbc_btnSix.gridx = 2;
		gbc_btnSix.gridy = 2;
		contentPane.add(btnSix, gbc_btnSix);
		
		JButton btnDivide = new JButton("/");
		btnDivide.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("/");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnDivide = new GridBagConstraints();
		gbc_btnDivide.fill = GridBagConstraints.BOTH;
		gbc_btnDivide.insets = new Insets(0, 0, 5, 0);
		gbc_btnDivide.gridx = 3;
		gbc_btnDivide.gridy = 2;
		contentPane.add(btnDivide, gbc_btnDivide);
		
		JButton btnOne = new JButton("1");
		btnOne.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("1");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnOne = new GridBagConstraints();
		gbc_btnOne.fill = GridBagConstraints.BOTH;
		gbc_btnOne.insets = new Insets(0, 0, 5, 5);
		gbc_btnOne.gridx = 0;
		gbc_btnOne.gridy = 3;
		contentPane.add(btnOne, gbc_btnOne);
		
		JButton btnTwo = new JButton("2");
		btnTwo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("2");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnTwo = new GridBagConstraints();
		gbc_btnTwo.fill = GridBagConstraints.BOTH;
		gbc_btnTwo.insets = new Insets(0, 0, 5, 5);
		gbc_btnTwo.gridx = 1;
		gbc_btnTwo.gridy = 3;
		contentPane.add(btnTwo, gbc_btnTwo);
		
		JButton btnThree = new JButton("3");
		btnThree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("3");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnThree = new GridBagConstraints();
		gbc_btnThree.fill = GridBagConstraints.BOTH;
		gbc_btnThree.insets = new Insets(0, 0, 5, 5);
		gbc_btnThree.gridx = 2;
		gbc_btnThree.gridy = 3;
		contentPane.add(btnThree, gbc_btnThree);
		
		JButton btnMinus = new JButton("-");
		btnMinus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("-");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnMinus = new GridBagConstraints();
		gbc_btnMinus.fill = GridBagConstraints.BOTH;
		gbc_btnMinus.insets = new Insets(0, 0, 5, 0);
		gbc_btnMinus.gridx = 3;
		gbc_btnMinus.gridy = 3;
		contentPane.add(btnMinus, gbc_btnMinus);
		
		JButton btnZero = new JButton("0");
		btnZero.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("0");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnZero = new GridBagConstraints();
		gbc_btnZero.fill = GridBagConstraints.BOTH;
		gbc_btnZero.insets = new Insets(0, 0, 5, 5);
		gbc_btnZero.gridx = 0;
		gbc_btnZero.gridy = 4;
		contentPane.add(btnZero, gbc_btnZero);
		
		JButton btnClear = new JButton("C");
		btnClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("C");
				resultArea.setText(null);
			}
		});
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.fill = GridBagConstraints.BOTH;
		gbc_btnClear.insets = new Insets(0, 0, 5, 5);
		gbc_btnClear.gridx = 1;
		gbc_btnClear.gridy = 4;
		contentPane.add(btnClear, gbc_btnClear);
		
		JButton btnEquals = new JButton("=");
		btnEquals.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("=");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnEquals = new GridBagConstraints();
		gbc_btnEquals.fill = GridBagConstraints.BOTH;
		gbc_btnEquals.insets = new Insets(0, 0, 5, 5);
		gbc_btnEquals.gridx = 2;
		gbc_btnEquals.gridy = 4;
		contentPane.add(btnEquals, gbc_btnEquals);
		
		JButton btnAdd = new JButton("+");
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("+");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.fill = GridBagConstraints.BOTH;
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridx = 3;
		gbc_btnAdd.gridy = 4;
		contentPane.add(btnAdd, gbc_btnAdd);
		
		JButton btnSin = new JButton("Sin");
		btnSin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("sin");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnSin = new GridBagConstraints();
		gbc_btnSin.fill = GridBagConstraints.BOTH;
		gbc_btnSin.insets = new Insets(0, 0, 5, 5);
		gbc_btnSin.gridx = 0;
		gbc_btnSin.gridy = 5;
		contentPane.add(btnSin, gbc_btnSin);
		
		JButton btnTan = new JButton("Tan");
		btnTan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("tan");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnTan = new GridBagConstraints();
		gbc_btnTan.fill = GridBagConstraints.BOTH;
		gbc_btnTan.insets = new Insets(0, 0, 5, 5);
		gbc_btnTan.gridx = 1;
		gbc_btnTan.gridy = 5;
		contentPane.add(btnTan, gbc_btnTan);
		
		JButton btnCos = new JButton("Cos");
		btnCos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("cos");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnCos = new GridBagConstraints();
		gbc_btnCos.fill = GridBagConstraints.BOTH;
		gbc_btnCos.insets = new Insets(0, 0, 5, 5);
		gbc_btnCos.gridx = 2;
		gbc_btnCos.gridy = 5;
		contentPane.add(btnCos, gbc_btnCos);
		
		JButton btnSqrt = new JButton("Sqrt");
		btnSqrt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("sqrt");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnSqrt = new GridBagConstraints();
		gbc_btnSqrt.fill = GridBagConstraints.BOTH;
		gbc_btnSqrt.insets = new Insets(0, 0, 5, 0);
		gbc_btnSqrt.gridx = 3;
		gbc_btnSqrt.gridy = 5;
		contentPane.add(btnSqrt, gbc_btnSqrt);
		
		JButton btnDecimal = new JButton(".");
		btnDecimal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked(".");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		
		JButton btnCloseBracket = new JButton(")");
		btnCloseBracket.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked(")");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		
		JButton btnOpenBracket = new JButton("(");
		btnOpenBracket.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("^");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnOpenBracket = new GridBagConstraints();
		gbc_btnOpenBracket.fill = GridBagConstraints.BOTH;
		gbc_btnOpenBracket.insets = new Insets(0, 0, 5, 5);
		gbc_btnOpenBracket.gridx = 0;
		gbc_btnOpenBracket.gridy = 6;
		contentPane.add(btnOpenBracket, gbc_btnOpenBracket);
		GridBagConstraints gbc_btnCloseBracket = new GridBagConstraints();
		gbc_btnCloseBracket.fill = GridBagConstraints.BOTH;
		gbc_btnCloseBracket.insets = new Insets(0, 0, 5, 5);
		gbc_btnCloseBracket.gridx = 1;
		gbc_btnCloseBracket.gridy = 6;
		contentPane.add(btnCloseBracket, gbc_btnCloseBracket);
		GridBagConstraints gbc_btnDecimal = new GridBagConstraints();
		gbc_btnDecimal.fill = GridBagConstraints.BOTH;
		gbc_btnDecimal.insets = new Insets(0, 0, 5, 5);
		gbc_btnDecimal.gridx = 2;
		gbc_btnDecimal.gridy = 6;
		contentPane.add(btnDecimal, gbc_btnDecimal);
		
		JButton btnPower = new JButton("^");
		btnPower.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("^");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_btnPower = new GridBagConstraints();
		gbc_btnPower.fill = GridBagConstraints.BOTH;
		gbc_btnPower.insets = new Insets(0, 0, 5, 0);
		gbc_btnPower.gridx = 3;
		gbc_btnPower.gridy = 6;
		contentPane.add(btnPower, gbc_btnPower);
		
		JButton btnSigned = new JButton("+ / -");
		btnSigned.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buttonCLicked("+ / -");
				resultArea.setText(null);
				resultArea.setText(controller.toString());
			}
		});
		GridBagConstraints gbc_buttonSigned = new GridBagConstraints();
		gbc_buttonSigned.fill = GridBagConstraints.BOTH;
		gbc_buttonSigned.insets = new Insets(0, 0, 0, 5);
		gbc_buttonSigned.gridx = 0;
		gbc_buttonSigned.gridy = 7;
		contentPane.add(btnSigned, gbc_buttonSigned);
		
		setVisible(true);
	}
	

	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		new YourClient();
	}

}
