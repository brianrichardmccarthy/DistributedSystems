package io.github.brianrichardmccarthy;

public class Employee {
	private int ssn;
	private String name;
	private String dob;
	private String address;
	private String gender;
	private double salary;
	
	public Employee(int ssn, String name, String dob, String address, String gender, double salary) {
		this.ssn = ssn;
		this.name = name;
		this.dob = dob;
		this.address = address;
		this.gender = gender;
		this.salary = salary;
	}
	public int getSsn() {
		return ssn;
	}
	public void setSsn(int ssn) {
		this.ssn = ssn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	@Override
	public String toString() {
		return "Employee [ssn=" + ssn + ", name=" + name + ", dob=" + dob + ", address=" + address + ", gender="
				+ gender + ", salary=" + salary + "]";
	}
	
}