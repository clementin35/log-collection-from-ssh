package application.view;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {

	private Main mainApp;
	private Stage dialogStage;

	@FXML
	private Button okButton;

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Creates an table view with no instances
	 */
	@FXML
	private void handleClose() {
		if (dialogStage != null) {
			dialogStage.close();
		}
	}
}
