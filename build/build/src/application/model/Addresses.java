package application.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Addresses {

	private final StringProperty instanceName;
	private final StringProperty IpAddress;
	private final StringProperty password;
	private final StringProperty userName;
	private final IntegerProperty portNumber;

	public Addresses() {
		// TODO Auto-generated constructor stub
		this(null, null,null);
	}

	/**
	 * Constructor with some initial data.
	 * 
	 * @param IpAddress
	 * @param password
	 */
	public Addresses(String instanceName ,String IpAddress, String password) {
		// TODO Auto-generated constructor stub
		this.IpAddress = new SimpleStringProperty(IpAddress);
		this.password = new SimpleStringProperty(password);

		// default values
		this.instanceName = new SimpleStringProperty(instanceName);
		this.userName = new SimpleStringProperty("root");
		this.portNumber = new SimpleIntegerProperty(22);
	}
	
	private String maskPass(String password) {
        String output = "";
        for(int i = 0; i < password.length(); i++) {
            output += "\u2022";
        }
        return output;
    }

	// getters and setters
	
	public String getUserName() {
		return userName.get();
	}
	
	public void setUserName(String userName) {
		this.userName.set(userName);
	}
	
	public StringProperty userNameProperty() {
		return userName;
	}

	public String getInstanceName() {
		return instanceName.get();
	}

	public void setInstanceName(String instanceName) {
		this.instanceName.set(instanceName);
	}

	public StringProperty instanceNameProperty() {
		return instanceName;
	}

	public String getIpAddress() {
		return IpAddress.get();
	}

	public void setIpAddress(String IpAdress) {
		this.IpAddress.set(IpAdress);
	}

	public StringProperty IpAddressProperty() {
		return IpAddress;
	}

	public String getPassword() {
		return password.get();
	}

	public void setPassword(String Password) {
		this.password.set(Password);
		
	}

	public StringProperty PasswordProperty() {
		return password;
	}

	public int getPortNumber() {
		return portNumber.get();
	}

	public void setPortNumber(int portNumber) {
		this.portNumber.set(portNumber);
	}

	public IntegerProperty PortNumberProperty() {
		return portNumber;
	}

}
