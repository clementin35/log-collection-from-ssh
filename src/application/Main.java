package application;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

import application.model.Addresses;
import application.model.InstanceListWrapper;
import application.view.AboutController;
import application.view.LogOverviewController;
import application.view.RootLayoutController;
import application.view.ServerEditDialogController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application implements Runnable {

	ClassLoader classLoader = getClass().getClassLoader();

	private Stage primaryStage;
	private AnchorPane logOverview;
	private BorderPane rootLayout;
	private AnchorPane serverEditDialog;
	private AnchorPane aboutDialog;

	//File xmlFile = new File(classLoader.getResource("data/instances.xml").getFile());

	public File xmlFile = new File("resources/data/instances.xml");

	private ObservableList<Addresses> ipData = FXCollections.observableArrayList();

	public ObservableList<Addresses> getIpData() {
		return ipData;
	}

	public File getXmlFile() {
		return xmlFile;
	}

	public Main() throws SAXException, IOException {
		// TODO Auto-generated constructor stub
		// ipData.add(new Addresses("Kandy China", "54.223.173.74",
		// "KandyCN123$"));

		InstanceDataSetup(xmlFile);

	}

	// getter method for person data

	public ObservableList<Addresses> getServerData() {

		return ipData;

	}

	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Genband Gramber");

		this.primaryStage.setWidth(950);
		this.primaryStage.setHeight(600);

		this.primaryStage.setResizable(true);

		initRootLayout();
		showLogOverview();
				
		primaryStage.getIcons().add(new Image("file:resources/images/1465305729_Christmas-19_32.png"));
		primaryStage.getIcons().add(new Image("file:resources/images/1465305729_Christmas-19_16.png"));
		primaryStage.getIcons().add(new Image("file:resources/images/1465305729_Christmas-19_48.png"));
		primaryStage.getIcons().add(new Image("file:resources/images/1465305729_Christmas-19_256.bmp"));


		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				Platform.exit();
				System.exit(0);

			}
		});

	}

	private void showLogOverview() {
		// TODO Auto-generated method stub
		try {

			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/LogOverview.fxml"));
			logOverview = loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(logOverview);

			LogOverviewController controller = loader.getController();
			controller.setMainApp(this);
			controller.getLogScreenArea().appendText("Welcome to SpiDR Log Collection");
			controller.getLogScreenArea().autosize();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void initRootLayout() {
		// TODO Auto-generated method stub
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			// Give the controller access to the main app.
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Try to load last opened person file.
		File file = getInstancesFilePath();

		/*
		 * if (file != null) { loadInstanceDataFromFile(file); }
		 */

		// * The lines that give the controller access to the main app and the
		// last three lines to load the last opened person file.

	}

	/**
	 * Opens a dialog to edit details for the specified person. If the user
	 * clicks OK, the changes are saved into the provided person object and true
	 * is returned.
	 * 
	 * @param person
	 *            the person object to be edited
	 * @return true if the user clicked OK, false otherwise.
	 */

	public boolean showServerEditDialog(Addresses address) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ServerEditDialog.fxml"));
			serverEditDialog = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Server");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(serverEditDialog);
			dialogStage.setScene(scene);

			dialogStage.setResizable(false);

			// Set the person into the controller.
			ServerEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setAddress(address);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOKClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showAboutDialog() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/About.fxml"));
			aboutDialog = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("About");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(aboutDialog);
			dialogStage.setScene(scene);

			dialogStage.setResizable(false);

			AboutController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns the person file preference, i.e. the file that was last opened.
	 * The preference is read from the OS specific registry. If no such
	 * preference can be found, null is returned.
	 * 
	 * @return
	 */
	public File getInstancesFilePath() {

		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/**
	 * Sets the file path of the currently loaded file. The path is persisted in
	 * the OS specific registry.
	 * 
	 * @param file
	 *            the file or null to remove the path
	 */
	public void setInstancesFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());

			// Update the stage title.
			primaryStage.setTitle("SPiDR Log Collection Tool - " + file.getName());
		} else {
			prefs.remove("filePath");

			// Update the stage title.
			primaryStage.setTitle("SPiDR Log Collection Tool");
		}
	}

	/**
	 * Loads person data from the specified file. The current person data will
	 * be replaced.
	 * 
	 * @param file
	 */

	public void InstanceDataSetup(File file) {

		try {

			JAXBContext context = JAXBContext.newInstance(InstanceListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			InstanceListWrapper wrapper = (InstanceListWrapper) um.unmarshal(file);

			ipData.clear();
			if (wrapper.getInstances() == null) {
			} else
				ipData.addAll(wrapper.getInstances());

			// Save the file path to the registry.

			// setInstancesFilePath(file);

		} catch (JAXBException e) { /** If catches any exception */
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}

	}

	public void loadInstanceDataFromFile(File file) {

		try {

			JAXBContext context = JAXBContext.newInstance(InstanceListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			InstanceListWrapper wrapper = (InstanceListWrapper) um.unmarshal(file);

			ipData.clear();
			ipData.addAll(wrapper.getInstances());

			// Save the file path to the registry.

			setInstancesFilePath(file);

		} catch (JAXBException e) { /** If catches any exception */
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}

	}

	/**
	 * Saves the current person data to the specified file.
	 * 
	 * @param file
	 */
	public void saveInstanceDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(InstanceListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			InstanceListWrapper wrapper = new InstanceListWrapper();
			wrapper.setInstances(ipData);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, file);

			// Save the file path to the registry.
			setInstancesFilePath(file);

		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

}
