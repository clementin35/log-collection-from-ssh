package application.view;

import application.Main;
import application.model.Addresses;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerEditDialogController {

	// This is for controlling the extra opening dialogs
	@FXML
	private TextField serverNameField;
	@FXML
	private TextField ipAddressField;
	@FXML
	private TextField passwordField;
	@FXML
	private TextField usernameField;

	private Main mainApp;
	private Stage dialogStage;
	private Addresses address;
	private boolean okClicked = false;
	
	RootLayoutController controller = new RootLayoutController();

	public ServerEditDialogController() {
		// TODO Auto-generated constructor stub
		
		
	}

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	
	// To set addresses of Kandy Servers
	public void setAddress(Addresses address) {

		this.address = address;

		serverNameField.setText(address.getInstanceName());
		ipAddressField.setText(address.getIpAddress());
		passwordField.setText(address.getPassword());
		usernameField.setText(address.getUserName());
	
	}
	
	public boolean isOKClicked() {
		return okClicked;
	}
	
	/**
     * Called when the user clicks ok.
     */
	
    @FXML
	private void handleOK() {
		if(isInputValid()){
			address.setInstanceName(serverNameField.getText());
			address.setIpAddress(ipAddressField.getText());
			address.setPassword(passwordField.getText());
			address.setUserName(usernameField.getText());
			
			okClicked = true;
			dialogStage.close();
			
		}
	}
	
	/**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

	private boolean isInputValid() {
		// TODO Auto-generated method stub
		
		String errorMessage = "";
		
		if (serverNameField.getText() == null || serverNameField.getText().length() == 0) {
			errorMessage += "Please enter a server name!\n";
		}
		
		// ip address check etme gelicek yakında
		if (ipAddressField.getText() == null || ipAddressField.getText().length() == 0) {
			errorMessage += "No valid IP address!\n";
		}
		
		if (passwordField.getText() == null || passwordField.getText().length() == 0) {
			errorMessage += "Please enter a password!";
		}
		
		if (errorMessage.length() == 0) {
			return true;	
		} else {
			//show the alert
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);
			
			alert.showAndWait();
			return false;

		} 
		
	}
	
}
