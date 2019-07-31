/**
 * 
 * @author Brian McCarthy
 * @version 1.0
 * @date 14/12/2018
 * 
 */
package io.github.brianrichardmccarthy;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 */
public interface YourIF extends Remote {

	/**
	 * 
	 * Adds the two given integers
	 * @param x
	 * @param y
	 * @return (int) x + y 
	 * @throws RemoteException
	 */
	int add(int x, int y) throws RemoteException;
	
	/**
	 * Subtracts the two given integers
	 * @param x
	 * @param y
	 * @return (int) x - y
	 * @throws RemoteException
	 */
	int subtract(int x, int y) throws RemoteException;
	
	/**
	 * Divides the two integers
	 * @param x
	 * @param y
	 * @return returns x/y
	 * @throws RemoteException
	 */
	int divide(int x, int y) throws RemoteException;
	
	/**
	 * Multiples the two given ints
	 * @param x
	 * @param y
	 * @return x*y
	 * @throws RemoteException
	 */
	int mulitply(int x, int y) throws RemoteException;
	
	/**
	 * 
	 * Adds the two given doubles
	 * @param x
	 * @param y
	 * @return (doubles) x + y 
	 * @throws RemoteException
	 */
	double add(double x, double y) throws RemoteException;
	
	/**
	 * 
	 * Subtracts the two given doubles
	 * @param x
	 * @param y
	 * @return (doubles) x - y 
	 * @throws RemoteException
	 */
	double subtract(double x, double y) throws RemoteException;
	
	/**
	 * 
	 * Divides the two given doubles
	 * @param x
	 * @param y
	 * @return (doubles) x / y 
	 * @throws RemoteException
	 */
	double divide(double x, double y) throws RemoteException;
	
	/**
	 * 
	 * Multiples the two given doubles
	 * @param x
	 * @param y
	 * @return (doubles) x * y 
	 * @throws RemoteException
	 */
	double mulitply(double x, double y) throws RemoteException;
	
	/**
	 * Calculates the tan of the given parameter
	 * @param x
	 * @return tan(x)
	 * @throws RemoteException
	 */
	double tan(double x) throws RemoteException;
	
	/**
	 * Calculates the sin of the given parameter
	 * @param x
	 * @return sin(x)
	 * @throws RemoteException
	 */
	double sin(double x) throws RemoteException;
	
	/**
	 * Calculates the cos of the given parameter
	 * @param x
	 * @return cos(x)
	 * @throws RemoteException
	 */
	double cos(double x) throws RemoteException;
	
	/**
	 * Calculates the square root of the given parameter
	 * @param x
	 * @return sqrt(x)
	 * @throws RemoteException
	 */
	double sqrt(double x) throws RemoteException;
	
	/**
	 * Calculates the base to the exponent
	 * @param base
	 * @param epo
	 * @return
	 * @throws RemoteException
	 */
	double power(double base, double exponent) throws RemoteException;
	
}
