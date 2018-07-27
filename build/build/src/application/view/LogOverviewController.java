package application.view;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleButton;

import application.Main;
import application.model.Addresses;
import application.model.SSHCommandExecutor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;

public class LogOverviewController implements Runnable {

	boolean isDeleted = false;
	OutputStream outputDebugging;
	PrintStream printstreamDebugging;

	DirectoryChooser directoryChooser = new DirectoryChooser();

	@FXML
	private TableView<Addresses> ipTable;
	@FXML
	private TableColumn<Addresses, String> instanceNameColumn;
	@FXML
	private TableColumn<Addresses, String> ipAddressColumn;
	@FXML
	private TableColumn<Addresses, String> passwordColumn;

	@FXML
	private JFXButton startButton;
	@FXML
	private JFXButton stopButton;
	@FXML
	private JFXButton downloadButton;

	@FXML
	private Label instanceNameLabel;
	@FXML
	private Label instanceLocationLabel;
	@FXML
	private Label isDoneLabel;

	@FXML
	private JFXCheckBox openAdminLogsCheckBox;
	@FXML
	private JFXCheckBox openAppLogsCheckBox;
	@FXML
	private JFXCheckBox openTurnLogsCheckBox;

	@FXML
	private TextArea logScreenArea;

	@FXML
	private ProgressIndicator actionProgressIndicator;

	@FXML
	private ProgressBar actionProgressBar;

	@FXML
	private JFXToggleButton enableAppDebugLevelTButton;

	@FXML
	private JFXToggleButton enableBrokerDebugLevelTButton;

	@FXML
	private JFXToggleButton enableAdmDebugLevelTButton;

	private SSHCommandExecutor ssh;

	public String logName = "default";

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public Button getStartButton() {
		return startButton;
	}

	@SuppressWarnings("rawtypes")
	private Task copyWorkerApp;

	@SuppressWarnings("rawtypes")
	private Task copyWorkerAdm;

	@SuppressWarnings("rawtypes")
	private Task copyWorkerTurn;

	@SuppressWarnings("rawtypes")
	private Task copyWorkerDownload;

	public File selectedDirectory;

	public TextArea getLogScreenArea() {
		return logScreenArea;
	}

	public void setLogScreenArea(String string) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				logScreenArea.appendText(string);
			}
		});
	}

	public TableColumn<Addresses, String> getInstanceNameColumn() {
		return instanceNameColumn;
	}

	public void setInstanceNameColumn(TableColumn<Addresses, String> instanceNameColumn) {
		this.instanceNameColumn = instanceNameColumn;
	}

	public TableColumn<Addresses, String> getIpAddressColumn() {
		return ipAddressColumn;
	}

	public void setIpAddressColumn(TableColumn<Addresses, String> ipAddressColumn) {
		this.ipAddressColumn = ipAddressColumn;
	}

	public TableColumn<Addresses, String> getPasswordColumn() {
		return passwordColumn;
	}

	public void setPasswordColumn(TableColumn<Addresses, String> passwordColumn) {
		this.passwordColumn = passwordColumn;
	}

	public TableView<Addresses> getIpTable() {
		return ipTable;
	}

	public void setIpTable(TableView<Addresses> ipTable) {
		this.ipTable = ipTable;
	}

	// reference to the main application
	private Main mainApp;
	private boolean th1Status, th2Status, th3Status, th4Status, th5Status, th6Status, th7Status, th8Status,
			th9Status = false;

	private boolean downloadStatus = false;
	private String destPath;

	public String getDestPath() {
		return destPath;
	}

	public String host, user, password;

	public LogOverviewController() {
		// TODO Auto-generated constructor stub
	}

	RootLayoutController layoutController = new RootLayoutController();

	@FXML
	private void initialize() {

		instanceNameColumn.setCellValueFactory(cellData -> cellData.getValue().instanceNameProperty());
		/* Initialize servers table with 3 columns */
		// instanceNameColumn.setCellValueFactory(cellData ->
		// cellData.getValue().instanceNameProperty());
		ipAddressColumn.setCellValueFactory(cellData -> cellData.getValue().IpAddressProperty());
		passwordColumn.setCellValueFactory(cellData -> cellData.getValue().PasswordProperty());

		/* buraya edit screen in kod bloğu eklenecektir... */
		showServerDetails(null);

		// selection listener
		ipTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showServerDetails(newValue));

		ssh = new SSHCommandExecutor(this);

		// log durdurma
		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ssh.stop();
			}
		});

		logScreenArea.setWrapText(true);

	}

	@FXML
	public void clearCache() {

		Addresses selectedPerson = ipTable.getSelectionModel().getSelectedItem();

		if (selectedPerson != null) {

			/** Listener take data from table */
			host = ipAddressColumn.getCellData(selectedPerson);
			user = "root";
			password = passwordColumn.getCellData(selectedPerson);

			Thread debugThreadClearCache = new Thread(new Runnable() {
				public void run() {
					try {

						ssh.openConnectionForAdminTiers(host, user, password);
						isDeleted = ssh.clearCache();

						if (isDeleted) {
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									System.out.println("INFO: Cache has been cleared: " + isDeleted);
									Alert alert = new Alert(AlertType.CONFIRMATION);
									alert.initOwner(mainApp.getPrimaryStage());
									alert.setTitle("Cache Cleaning");
									alert.setHeaderText("Cache has been cleared");

									alert.showAndWait();
								}
							});

						}

						else {

							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub

									System.out.println("INFO: Cache has not been cleared: " + isDeleted);
									Alert alert = new Alert(AlertType.ERROR);
									alert.initOwner(mainApp.getPrimaryStage());
									alert.setTitle("Cache Cleaning");
									alert.setHeaderText("Cache has not been cleared");

									alert.showAndWait();

								}
							});

						}

					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			debugThreadClearCache.start();
			debugThreadClearCache.setName("Caching Debug Leverage");
		}

		else {

			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Cache Cleaning");
			alert.setHeaderText("Cache has not been cleared from dead logs");
			alert.setContentText("NOTICE: Due to connection error, logs couldnt cleared");

			alert.showAndWait();
		}
	}

	private void showServerDetails(Addresses address) {
		if (address != null) {
			// fill tables with address info
			instanceNameLabel.setText(address.getInstanceName());
			/* server location ının alıp buraya yazdıralım */
		}

		else {
			// Person null, clear everything.
			instanceNameLabel.setText("");
		}

	}

	/**
	 * Called when the user clicks the new button. Opens a dialog to edit
	 * details for a new person.
	 */

	@FXML
	private void handleAppDebugLevel() {

		Addresses selectedPerson = ipTable.getSelectionModel().getSelectedItem();

		if (selectedPerson != null) {

			/** Listener take data from table */
			host = ipAddressColumn.getCellData(selectedPerson);
			user = "root";
			password = passwordColumn.getCellData(selectedPerson);

			Thread debugThread1 = new Thread(new Runnable() {
				public void run() {
					if (enableAppDebugLevelTButton.isSelected()) {
						try {
							ssh.openAppDebug(host, user, password);
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (enableAppDebugLevelTButton.isSelected() == false) {
						try {
							ssh.closeAppDebug(host, user, password);
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});

			debugThread1.start();
			debugThread1.setName("App Level Debug Threading");
		}

		else {

			enableAppDebugLevelTButton.setSelected(false);

			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Side Selected");
			alert.setContentText("Please select a side in the table.");

			alert.showAndWait();
		}
	}

	@FXML
	private void handleBrokerDebugLevel() {

		Addresses selectedPerson = ipTable.getSelectionModel().getSelectedItem();

		if (selectedPerson != null) {

			/** Listener take data from table */
			host = ipAddressColumn.getCellData(selectedPerson);
			user = "root";
			password = passwordColumn.getCellData(selectedPerson);

			Thread debugThread2 = new Thread(new Runnable() {
				public void run() {
					if (enableBrokerDebugLevelTButton.isSelected()) {
						try {
							ssh.openBrokerDebug(host, user, password);
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (enableBrokerDebugLevelTButton.isSelected() == false) {
						try {
							ssh.closeBrokerDebug(host, user, password);
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});

			debugThread2.start();
			debugThread2.setName("Broker Level Debug Threading");
		}

		else {

			enableBrokerDebugLevelTButton.setSelected(false);

			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Side Selected");
			alert.setContentText("Please select a side in the table.");

			alert.showAndWait();
		}
	}
	

	@SuppressWarnings("deprecation")
	@FXML
	private void handleAdminDebugLevel() throws Throwable {

		Addresses selectedPerson = ipTable.getSelectionModel().getSelectedItem();

		if (selectedPerson != null) {

			/** Listener take data from table */
			host = ipAddressColumn.getCellData(selectedPerson);
			user = "root";
			password = passwordColumn.getCellData(selectedPerson);
			
			Channel debugChannel = ssh.debugSession(host, user, password);

		
			
			Thread adminDebugThread = new Thread(new Runnable() {
				
				public void run() {
					
					if (enableAdmDebugLevelTButton.isSelected()) {
						try {
							
							/* Output and Command list definition */
							outputDebugging = debugChannel.getOutputStream();
							printstreamDebugging = new PrintStream(outputDebugging, true);

							/* Start to command burning */
							debugChannel.connect();

							InputStream inputDebugging = debugChannel.getInputStream();

							// command lists
							printstreamDebugging.println(
									"runon --adm \"./logcap.sh -d all\""); /*
																			 * activate debug level
																			 * for all tiers
																			 */

							setLogScreenArea("\n[INFORMATION]: Admin Tier Debug Level has been opened, activation may take a few seconds");


							/* Killing threads */
							
							
							ssh.printResult(inputDebugging, debugChannel);

							printstreamDebugging.close();
							debugChannel.disconnect();
							
							//ssh.openAdmDebug(host, user, password);
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
					
					if (enableAdmDebugLevelTButton.isSelected() == false) {
						try {
														
							/* Output and Command list definition */
							outputDebugging = debugChannel.getOutputStream();
							printstreamDebugging = new PrintStream(outputDebugging, true);

							/* Start to command burning */
							debugChannel.connect();

							InputStream inputDebugging = debugChannel.getInputStream();

							// command lists
							// command lists
							printstreamDebugging.println(
									"runon --app \"./logcap.sh -u all\""); /*
																			 * activate debug level
																			 * for all tiers
																			 */
							setLogScreenArea("\n[INFORMATION]: Application Tier Debug Level has been closed");


							/* Killing threads */
							
							ssh.printResult(inputDebugging, debugChannel);

							printstreamDebugging.close();
							
							//ssh.closeAdmDebug(host, user, password);
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				
			});

			if(adminDebugThread.isAlive() == true){
				adminDebugThread.interrupt();
			}
			
			adminDebugThread.start();
			adminDebugThread.setName("Adm Level Debug Threading");
			
		}

		else {

			enableAdmDebugLevelTButton.setSelected(false);

			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Side Selected");
			alert.setContentText("Please select a side in the table.");

			alert.showAndWait();
		}
	}


	@FXML
	private void handleNewPerson() {
		Addresses tempPerson = new Addresses();
		boolean okClicked = mainApp.showServerEditDialog(tempPerson);
		if (okClicked) {
			mainApp.getServerData().add(tempPerson);
		}
	}

	@FXML
	private void handleDeletePerson() {

		int selectedIndex = ipTable.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {
			ipTable.getItems().remove(selectedIndex);
		}

		else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Side Selected");
			alert.setContentText("Please select a side in the table.");

			alert.showAndWait();
		}
	}

	/**
	 * Called when the user clicks the edit button. Opens a dialog to edit
	 * details for the selected person.
	 */
	@FXML
	private void handleEditPerson() {
		Addresses selectedPerson = ipTable.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			boolean okClicked = mainApp.showServerEditDialog(selectedPerson);
			if (okClicked) {
				showPersonDetails(selectedPerson);
			}

		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No Selection");
			alert.setHeaderText("No Person Selected");
			alert.setContentText("Please select a person in the table.");

			alert.showAndWait();
		}
	}

	/**
	 * 
	 * This is for start session button
	 * 
	 * @throws Exception
	 */

	public void assignFromList(Addresses selectedPerson) {

		/** Button disable for aware of repeated actions */
		startButton.setDisable(true);

		/** Listener take data from table */
		host = ipAddressColumn.getCellData(selectedPerson);
		user = "root";
		password = passwordColumn.getCellData(selectedPerson);

	}

	@SuppressWarnings("unchecked")
	@FXML
	private void handleStartSession() throws Exception {
		Addresses selectedPerson = ipTable.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {

			assignFromList(selectedPerson);

			/** Getting log file name from a new window */
			TextInputDialog dialog = new TextInputDialog();
			dialog.setResizable(false);
			dialog.setTitle("Information");
			dialog.setHeaderText("Please, type a name for your logs");
			dialog.setContentText("Name:");
			
			Optional<String> result = (Optional<String>) dialog.showAndWait();
			
			
			

			if (result.isPresent()) { // The result.isPresent() will return
										// false if the user cancelled the
										// dialog.
				logName = result.get();

				/*
				 * Controlling the checkboxes, If selected collect traces from
				 * each
				 */
				if (openAdminLogsCheckBox.isSelected()) {

					/* Initially connection establish */
					ssh.openConnectionForAdminTiers(host, user, password);

					/** Starting progress indicator for existing action */
					copyWorkerAdm = createWorkerAdm();
					actionProgressIndicator.progressProperty().unbind();
					actionProgressIndicator.progressProperty().bind(copyWorkerAdm.progressProperty());

					copyWorkerAdm.messageProperty().addListener(new ChangeListener<String>() {
						public void changed(ObservableValue<? extends String> observable, String oldValue,
								String newValue) {
							System.out.println(newValue);
						}
					});

					/* Starting thread of collecting traces */
					new Thread(copyWorkerAdm).start();

				}

				if (openAppLogsCheckBox.isSelected()) {
					// collect logs from host3 and host4

					/* Initially connection establish */

					ssh.openConnectionForApplicationTiers(host, user, password);

					if (ssh.isConnectedToAppTier) {
						/** Starting progress indicator for existing action */
						copyWorkerApp = createWorkerApp();

						actionProgressIndicator.progressProperty().unbind();
						actionProgressIndicator.progressProperty().bind(copyWorkerApp.progressProperty());

						copyWorkerApp.messageProperty().addListener(new ChangeListener<String>() {
							public void changed(ObservableValue<? extends String> observable, String oldValue,
									String newValue) {
								System.out.println(newValue);
							}
						});

						/* Starting thread of collecting traces */
						new Thread(copyWorkerApp).start();
					}

				}

				if (openTurnLogsCheckBox.isSelected()) {

					/* collect logs from host5,host6,host7 and host8 */
					/* checkbox for Broker and Turn logs */

					/* Initially connection establish */
					ssh.openConnectionForTurnTiers(host, user, password);

					/** Starting progress indicator for existing action */
					copyWorkerTurn = createWorkerTurn();

					actionProgressIndicator.progressProperty().unbind();
					actionProgressIndicator.progressProperty().bind(copyWorkerTurn.progressProperty());

					copyWorkerTurn.messageProperty().addListener(new ChangeListener<String>() {
						public void changed(ObservableValue<? extends String> observable, String oldValue,
								String newValue) {
							System.out.println(newValue);
						}
					});

					/* Starting thread of collecting traces */
					new Thread(copyWorkerTurn).start();

				}

				/**
				 * If none of the check box selected and would push to start
				 * button, an alert will raise
				 */
				if (openTurnLogsCheckBox.isSelected() == false && openAppLogsCheckBox.isSelected() == false
						&& openAdminLogsCheckBox.isSelected() == false) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainApp.getPrimaryStage());

					/* Alert information */
					alert.setTitle("Error");
					alert.setHeaderText("No Host Selected");
					alert.setContentText("Please check less one tier.");

					alert.showAndWait();
					startButton.setDisable(false);
				}

			} else {
				// Activate start button again
				startButton.setDisable(false);
			}
		}

		/**
		 * If user do not select any of instance from table view, an alert will
		 * raise
		 */
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainApp.getPrimaryStage());

			/* Alert information */
			alert.setTitle("Error");
			alert.setHeaderText("No Instance Selected");
			alert.setContentText("Please select an instance from the list.");

			alert.showAndWait();

			startButton.setDisable(false);
		}
	}

	/** Handling method for download action */
	@FXML
	private void handleDownloadButton() throws JSchException, Exception {

		selectedDirectory = directoryChooser.showDialog(mainApp.getPrimaryStage());

		destPath = selectedDirectory.toString();

		if (selectedDirectory.isDirectory()) {

			/* Download button should disable for aware of race conditions */
			downloadButton.setDisable(true);

			/* Starting progress bar for existing action */
			copyWorkerDownload = createWorkerDownload();

			actionProgressBar.progressProperty().unbind();
			actionProgressBar.progressProperty().bind(copyWorkerDownload.progressProperty());

			copyWorkerDownload.messageProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					System.out.println(newValue);
				}
			});

			new Thread(copyWorkerDownload).start();

		}
	}

	@FXML
	private void handleClearButton() throws Exception {

		/**
		 * Method acts as cleaner of /root/spidrlogs directory from app tiers
		 */

		ssh.clearCache();

	}

	@FXML
	private void handleCancelDownloadButton() {

		/* This method will be handled cancellation of download progress */

	}

	/** Outbound Task method has been using for implementation of ProgressBar */
	@SuppressWarnings("rawtypes")
	private Task createWorkerDownload() throws Exception {
		// TODO Auto-generated method stub
		return new Task() {
			@Override
			protected Object call() throws Exception {
				for (int i = 0; i < 1; i++) {

					/* Create a new Thread for downloading action */
					Thread downloadThread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							/******
							 * In order to directory selection, code has been
							 * opened a new window to prevent null-pointers this
							 * action has been run in UI thread
							 */

							/* Start the download progress */
							try {
								ssh.download(host, user, password);

							} catch (JSchException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							/*
							 * Set download status to true for next level
							 * actions
							 */
							downloadStatus = true;
							stopDownload();

							/* When download finishes, kill thread */
							// stopDownload();

							/** Error modifier will write here */
							/*
							 * If program catch an error or exception, we havent
							 * shown yet. So, error codes will add here in next
							 * version
							 */
						}
					});

					/* Set name to Thread and start action */

					// Platform.setImplicitExit(false);
					downloadThread.setName("Download Thread");
					downloadThread.start();

				}

				return true;
			}
		};

	}

	@SuppressWarnings("rawtypes")
	private Task createWorkerTurn() {
		// TODO Auto-generated method stub

		actionProgressIndicator.setVisible(true);
		isDoneLabel.setVisible(false);

		return new Task() {
			@Override
			protected Object call() throws Exception {
				for (int i = 0; i < 1; i++) {

					/** Create threads for every host */

					Thread host5Thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							try {

								/* Execute log collection for each host */
								ssh.executeCommandsForHost5Tier(logName);
								th5Status = true;
								stopProgress();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

					Thread host6Thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							try {

								/* Execute log collection for each host */
								ssh.executeCommandsForHost6Tier(logName);
								th6Status = true;
								stopProgress();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

					Thread host7Thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							try {

								/* Execute log collection for each host */
								ssh.executeCommandsForHost7Tier(logName);
								th7Status = true;
								stopProgress();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

					Thread host8Thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							try {

								/* Execute log collection for each host */
								ssh.executeCommandsForHost8Tier(logName);
								th8Status = true;
								stopProgress();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

					Thread host9Thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							try {

								/* Execute log collection for each host */
								ssh.executeCommandsForHost9Tier(logName);
								th9Status = true;
								stopProgress();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

					/** Threads starting **/
					host5Thread.start();
					host5Thread.setName("Thread of Host5");

					host6Thread.start();
					host6Thread.setName("Thread of Host6");

					host7Thread.start();
					host7Thread.setName("Thread of Host7");

					host8Thread.start();
					host8Thread.setName("Thread of Host8");

					host9Thread.start();
					host9Thread.setName("Thread of Host9");

				}

				return true;
			}
		};
	}

	@SuppressWarnings("rawtypes")
	private Task createWorkerAdm() {
		// TODO Auto-generated method stub

		actionProgressIndicator.setVisible(true);
		isDoneLabel.setVisible(false);
		return new Task() {
			@Override
			protected Object call() throws Exception {
				for (int i = 0; i < 1; i++) {

					Thread host1Thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							try {

								/* Execute commands for each hosts */
								ssh.executeCommandsForHost1Tier(logName);
								th1Status = true;
								stopProgress();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

					Thread host2Thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							try {

								/* Execute commands for each hosts */
								ssh.executeCommandsForHost2Tier(logName);
								th2Status = true;
								stopProgress();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

					host1Thread.start();
					host1Thread.setName("Thread for Host1");

					host2Thread.start();
					host2Thread.setName("Thread for Host2");

				}

				return true;
			}
		};
	}

	@SuppressWarnings("rawtypes")
	private Task createWorkerApp() {
		// TODO Auto-generated method stub

		actionProgressIndicator.setVisible(true);
		isDoneLabel.setVisible(false);

		return new Task() {
			@Override
			protected Object call() throws Exception {
				for (int i = 0; i < 1; i++) {

					Thread host3Thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							try {

								/* Execute commands for each hosts */
								ssh.executeCommandsForHost3Tier(logName);
								th3Status = true;
								stopProgress();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					host3Thread.setName("host3Thread");

					Thread host4Thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							try {

								/* Execute commands for each hosts */
								ssh.executeCommandsForHost4Tier(logName);
								th4Status = true;
								stopProgress();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

					host3Thread.start();
					host3Thread.setName("Thread of Host3");

					host4Thread.start();
					host4Thread.setName("Thread of Host4");

				}

				return true;
			}
		};
	}

	private synchronized void stopProgress() {

		boolean admin = false;
		boolean application = false;
		boolean broker = false;

		/* If both thread status would have closed, stop progress indicator */
		if (checkAdminTier()) {

			if (th1Status && th2Status) {
				admin = true;
			}
		}

		if (checkApplicationTier()) {

			if (th3Status && th4Status) {
				application = true;
			}
		}

		if (checkRestTier()) {
			if (th5Status && th6Status && th7Status && th8Status && th9Status) {
				broker = true;
			}

		}

		if (application || broker || admin) {

			/** Connect to UI thread */
			Platform.runLater(new Runnable() {

				@Override
				public void run() {

					isDoneLabel.setVisible(true);
					actionProgressIndicator.setVisible(false);
					isDoneLabel.setText("Done...");
					mainApp.getPrimaryStage().setTitle("SPiDR Log Collection Tool");

					/* Activate start button again */
					startButton.setDisable(false);

				}
			});
		}

	}

	private synchronized void stopDownload() {

		/* Method for stopping progress bar */
		if (downloadStatus == true) {

			actionProgressBar.setVisible(false);

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					isDoneLabel.setText("Downloaded");
				}
			});

			downloadButton.setDisable(false);

		}
	}

	private void showPersonDetails(Addresses selectedPerson) {
		// TODO Auto-generated method stub

	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table
		ipTable.setItems(mainApp.getServerData());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public boolean checkAdminTier() {
		if (openAdminLogsCheckBox.isSelected()) {
			return true;
		} else
			return false;
	}

	public boolean checkApplicationTier() {
		if (openAppLogsCheckBox.isSelected()) {
			return true;
		} else
			return false;
	}

	public boolean checkRestTier() {
		if (openTurnLogsCheckBox.isSelected()) {
			return true;
		} else
			return false;
	}

}


