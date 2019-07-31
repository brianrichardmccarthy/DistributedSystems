package io.github.brianrichardmccarthy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

public class Database {

	/**
	 * Constants used for the connection string
	 */
	private final String password = "";
	private final String username = "root";
	private final String tablename = "assignment1";
	private final String servername = "localhost";
	private final String portnumber = "3306";
	
	/**
	 * Instance variables
	 */
	private ArrayList<Employee> employees;
	private int index;
	private PreparedStatement preStmt = null;
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	/**
	 * Constructor
	 */
	public Database() {	
		con = getConnection();
		employees = new ArrayList<>();
		index = 0;
		updateResult();
	}
	
	/**
	 * Gets the current employee
	 * @return Employee
	 */
	public Employee getCurrentEmployee() {
		return employees.get(index);
	}
	
	/**
	 * Gets the next employee
	 * @return Employee
	 */
	public Employee getNextEmployee() {
		index = (index == employees.size()-1) ? 0 : index + 1;
		return getCurrentEmployee();
	}
	
	/**
	 * Gets the previous employee
	 * @return Employee
	 */
	public Employee getPrevEmployee() {
		index = (index == 0) ? employees.size()-1 : index - 1;
		return getCurrentEmployee();
	}
	
	/**
	 * Once a delete, insert or update has happened, update the arraylist with these changes
	 */
	private void updateResult() {
		try {
			employees = new ArrayList<>();
			index = 0;
			stmt = con.createStatement();
			rs = stmt.executeQuery("Select * From employee");
			while (rs.next()) employees.add(new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6), rs.getDouble(5)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a connection to mysql
	 * @return Connection
	 */
	private Connection getConnection() {
		Connection con;
		
		Properties pro = new Properties();
		pro.put("user", username);
		pro.put("password", password);
		String s = "jdbc:mysql://" + servername + ":" + portnumber + "/" + tablename + "?user=" + username + "&password=" + password + "&serverTimezone=Europe/Dublin&useSSL=false";  //127.0.0.1:3306/?user=root";
		try {
			con = DriverManager.getConnection(s, pro);
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Deletes an employee record with the given id
	 * @param ssn (Int)
	 * @return Int number of rows effected (max 1)
	 */
	public int delete(int ssn) {
		int result = -1;
		try {
			preStmt = con.prepareStatement("DELETE FROM `assignment1`.`employee` WHERE `ssn` = " + ssn);
			result = preStmt.executeUpdate();
			if (result == 1) updateResult();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	/**
	 * Inserts a new employee record with the given parameters
	 * @param name (String)
	 * @param dob (String)
	 * @param address (String)
	 * @param salary (double)
	 * @param gender (String)
	 * @return Int number of rows effected (max 1)
	 */
	public int insert(String name, String dob, String address, double salary, String gender) {
		int result = -1;
		try {
			preStmt = con.prepareStatement("INSERT INTO `assignment1`.`employee`" + "(`name`," + "`dob`," + "`address`," + "`Salary`," + "`Gender`)" + " VALUES('" + name + "', '" + dob+ "', '" + address+ "', '" + salary+ "', '" + gender+ "')");
			result = preStmt.executeUpdate();
			if (result == 1) updateResult();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	/**
	 * Updates an employee record with the given id
	 * @param ssn (Int)
	 * @param name (String)
	 * @param dob (String)
	 * @param address (String)
	 * @param salary (double)
	 * @param gender (String)
	 * @return Int number of rows effected (max 1)
	 */
	public int update(int ssn, String name, String dob, String address, double salary, String gender) {
		int result = -1;
		try {
			preStmt = con.prepareStatement("UPDATE `assignment1`.`employee` SET `name` = '" + name +"', `dob` = '" + dob + "', `address` = '" + address + "', `Salary` = '" + salary + "', `Gender` = '" + gender + "' WHERE `ssn` = " + ssn);
			result = preStmt.executeUpdate();
			if (result == 1) updateResult();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
}
