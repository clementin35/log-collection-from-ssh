package application.view;

import java.io.File;

import application.Main;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 * 
 * @author Nuri Selcuk
 */
public class RootLayoutController implements Runnable{

	// Reference to the main application
	private Main mainApp;
	
	private LogOverviewController controller;
	
	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}
	
	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param controller
	 */
	public void setControllerApp(LogOverviewController controller){
		this.controller = controller;
	}

	@FXML
	private void handleClear() throws Exception {
		/** Clean cache of app tiers, method acts as duster */
		controller.clearCache();
	}

	/**
	 * Creates an table view with no instances
	 */
	@FXML
	private void handleNew() {
		mainApp.getServerData().clear();
		mainApp.setInstancesFilePath(null);
	}

	/**
	 * Opens a FileChooser to let the user select an address book to load.
	 */

	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			mainApp.loadInstanceDataFromFile(file);
		}
	}

	/**
	 * Saves the file to the person file that is currently open. If there is no
	 * open file, the "save as" dialog is shown.
	 */
	@FXML
	public void handleSave() {

		File personFile = mainApp.getXmlFile();
		if (personFile != null) {
			mainApp.saveInstanceDataToFile(personFile);
		} else {
			handleSaveAs();
		}
	}

	/**
	 * Opens a FileChooser to let the user select a file to save to.
	 */
	@FXML
	public void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

		if (file != null) {
			// Make sure it has the correct extension
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			mainApp.saveInstanceDataToFile(file);
		}
	}

	/**
	 * Opens an about dialog.
	 */
	@FXML
	private void handleAbout() {
		mainApp.showAboutDialog();

	}

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}