package application.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import application.utility.ConnectionCheck;
import application.utility.Stop;
import application.view.LogOverviewController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SSHCommandExecutor {

	private Session host1, host2, host3, host4, host5, host6, host7, host8, host9; // For
																					// hosts
	private Session debugging; // For activate debugging
	LogOverviewController controller;

	public SSHCommandExecutor(LogOverviewController controller) {
		this.controller = controller;

	}

	public boolean isConnectedToAppTier = false;
	public boolean isConnectedToAdmTier = false;

	ConnectionCheck check = new ConnectionCheck();

	Stop stopExecution = new Stop();

	OutputStream ops1, ops2, ops3, ops4, ops5, ops6, ops7, ops8, ops9;
	PrintStream ps1, ps2, ps3, ps4, ps5, ps6, ps7, ps8, ps9;

	OutputStream outputDebugging, opsForCleaning;
	PrintStream printstreamDebugging, psForCleaning;

	private String sourcePath1, sourcePath2, sourcePath3, sourcePath4, sourcePath5, sourcePath6, sourcePath7,
			sourcePath8, sourcePath9;

	public void openConnectionForAdminTiers(String host, String user, String password) throws JSchException {
		// TODO Auto-generated method stub

		try {
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");

			JSch jsch = new JSch();

			this.host1 = jsch.getSession(user, host, 22); // create a channel
			this.host1.setConfig(config);

			this.host1.setPassword(password);
			try {
				this.host1.connect(); // connection has been created
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				// Bağlantı hatası
			}

			// Create two ssh shell for execute admin tiers logs.
			this.host2 = jsch.getSession(user, host, 22);
			this.host2.setConfig(config);

			this.host2.setPassword(password);
			this.host2.connect(); // connection has been created
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Exception Dialog");
			alert.setHeaderText("Authentication Error");
			alert.setContentText("Please check your user ID or password!");

			Exception ex = new JSchException("Authentication Error");

			alert.showAndWait();
			controller.getStartButton().setDisable(false);
		}

	}

	public void openConnectionForApplicationTiers(String host, String user, String password) throws JSchException {
		// TODO Auto-generated method stub

		try {
			/**
			 * Open same ssh connections for each host. Host1 will rule other
			 * tiers...
			 */
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");

			JSch jsch = new JSch();

			this.host3 = jsch.getSession(user, host, 22); // create a channel
			this.host3.setConfig(config);

			this.host3.setPassword(password);
			this.host3.connect(); // connection has been created

			// Create two ssh shell for execute other application tiers logs
			// simultaneously.
			// Paralel programming

			this.host4 = jsch.getSession(user, host, 22);
			this.host4.setConfig(config);

			this.host4.setPassword(password);
			this.host4.connect(); // connection has been created

			isConnectedToAppTier = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Exception Dialog");
			alert.setHeaderText("Authentication Error");
			alert.setContentText("Please check your user ID or password!");

			Exception ex = new JSchException("Authentication Error");

			alert.showAndWait();
			controller.getStartButton().setDisable(false);

		}

	}

	public void openConnectionForTurnTiers(String host, String user, String password) throws JSchException {
		// TODO Auto-generated method stub

		try {
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");

			JSch jsch = new JSch();

			this.host5 = jsch.getSession(user, host, 22); // create a channel
			this.host5.setConfig(config);

			this.host5.setPassword(password);
			this.host5.connect(); // connection has been created

			// Create two ssh shell for execute other application tiers logs
			// simultaneously.
			// Paralel programming
			this.host6 = jsch.getSession(user, host, 22);
			this.host6.setConfig(config);

			this.host6.setPassword(password);
			this.host6.connect(); // connection has been created
			// Create two ssh shell for execute other application tiers logs
			// simultaneously.
			// Paralel programming
			this.host7 = jsch.getSession(user, host, 22);
			this.host7.setConfig(config);

			this.host7.setPassword(password);
			this.host7.connect(); // connection has been created

			// Create two ssh shell for execute other application tiers logs
			// simultaneously.
			// Paralel programming
			this.host8 = jsch.getSession(user, host, 22);
			this.host8.setConfig(config);

			this.host8.setPassword(password);
			this.host8.connect(); // connection has been created

			// Create two ssh shell for execute other application tiers logs
			// simultaneously.
			// Paralel programming
			this.host9 = jsch.getSession(user, host, 22);
			this.host9.setConfig(config);

			this.host9.setPassword(password);
			this.host9.connect(); // connection has been created

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Exception Dialog");
			alert.setHeaderText("Authentication Error");
			alert.setContentText("Please check your user ID or password!");

			Exception ex = new JSchException("Authentication Error");

			alert.showAndWait();
			controller.getStartButton().setDisable(false);
		}

	}

	public void openAppDebug(String host, String user, String password) throws Throwable {

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");

		JSch jsch = new JSch();

		this.debugging = jsch.getSession(user, host, 22); // create a channel
		this.debugging.setConfig(config);

		this.debugging.setPassword(password);
		try {
			this.debugging.connect(); // connection has been created
		} catch (Exception e) {
			// TODO: handle exception
			Platform.runLater(new Runnable() {
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Exception Dialog");
					alert.setHeaderText("Unable to connect to server");
					alert.setContentText("Unable to connect the virtual machines via existing network");

					alert.showAndWait();
				}
			});

		}
		/* Create command session for admin1 */
		Session debugSession = this.debugging;
		Channel debugChannel = debugSession.openChannel("shell");

		/* Output and Command list definition */
		outputDebugging = debugChannel.getOutputStream();
		printstreamDebugging = new PrintStream(outputDebugging, true);

		/* Start to command burning */
		debugChannel.connect();

		InputStream inputDebugging = debugChannel.getInputStream();

		// command lists
		printstreamDebugging.println(
				"runon --app \"./logcap.sh -d all\""); /*
														 * activate debug level
														 * for all tiers
														 */

		controller.setLogScreenArea(
				"\n[INFORMATION]: Application Tier Debug Level has been opened, activation may take a few seconds");

		printResult(inputDebugging, debugChannel);

		debugSession.disconnect();
		printstreamDebugging.close();
	}

	public Channel debugSession(String host, String user, String password) throws Throwable {

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");

		JSch jsch = new JSch();

		this.debugging = jsch.getSession(user, host, 22); // create a channel
		this.debugging.setConfig(config);

		this.debugging.setPassword(password);
		try {
			this.debugging.connect(); // connection has been created
		} catch (Exception e) {
			// TODO: handle exception
			Platform.runLater(new Runnable() {
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Exception Dialog");
					alert.setHeaderText("Unable to connect to server");
					alert.setContentText("Unable to connect the virtual machines via existing network");

					alert.showAndWait();
				}
			});

		}
		/* Create command session for admin1 */
		Session debugSession = this.debugging;
		Channel debugChannel = debugSession.openChannel("shell");

		/* Output and Command list definition */
		/*
		 * outputDebugging = debugChannel.getOutputStream();
		 * printstreamDebugging = new PrintStream(outputDebugging, true);
		 */

		/* Start to command burning */
		/* debugChannel.connect(); */

		return debugChannel;

	}

	public void activateDebugLog() {

	}

	public void openAdmDebug(String host, String user, String password) throws Throwable {

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");

		JSch jsch = new JSch();

		this.debugging = jsch.getSession(user, host, 22); // create a channel
		this.debugging.setConfig(config);

		this.debugging.setPassword(password);
		try {
			this.debugging.connect(); // connection has been created
		} catch (Exception e) {
			// TODO: handle exception
			Platform.runLater(new Runnable() {
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Exception Dialog");
					alert.setHeaderText("Unable to connect to server");
					alert.setContentText("Unable to connect the virtual machines via existing network");

					alert.showAndWait();
				}
			});

		}
		/* Create command session for admin1 */
		Session debugSession = this.debugging;
		Channel debugChannel = debugSession.openChannel("shell");

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

		controller.setLogScreenArea(
				"\n[INFORMATION]: Admin Tier Debug Level has been opened, activation may take a few seconds");

		/* Killing threads */

		printResult(inputDebugging, debugChannel);

		// debugging.disconnect();
		printstreamDebugging.close();
	}

	public void openBrokerDebug(String host, String user, String password) throws Throwable {

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");

		JSch jsch = new JSch();

		this.debugging = jsch.getSession(user, host, 22); // create a channel
		this.debugging.setConfig(config);

		this.debugging.setPassword(password);
		try {
			this.debugging.connect(); // connection has been created
		} catch (Exception e) {
			// TODO: handle exception
			Platform.runLater(new Runnable() {
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Exception Dialog");
					alert.setHeaderText("Unable to connect to server");
					alert.setContentText("Unable to connect the virtual machines via existing network");

					alert.showAndWait();
				}
			});

		}
		/* Create command session for admin1 */
		Session debugSession = this.debugging;
		Channel debugChannel = debugSession.openChannel("shell");

		/* Output and Command list definition */
		outputDebugging = debugChannel.getOutputStream();
		printstreamDebugging = new PrintStream(outputDebugging, true);

		/* Start to command burning */
		debugChannel.connect();

		InputStream inputDebugging = debugChannel.getInputStream();

		// command lists
		printstreamDebugging.println(
				"runon --broker --turn \"./logcap.sh -d all\""); /*
																	 * activate
																	 * debug
																	 * level for
																	 * all tiers
																	 */

		controller.setLogScreenArea(
				"\n[INFORMATION]: Broker and Turn Tiers Debug Level has been opened, activation may take a few seconds");

		/* Killing threads */

		printResult(inputDebugging, debugChannel);

		debugSession.disconnect();
		printstreamDebugging.close();
	}

	public void closeAppDebug(String host, String user, String password) throws Throwable {

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");

		JSch jsch = new JSch();

		this.debugging = jsch.getSession(user, host, 22); // create a channel
		this.debugging.setConfig(config);

		this.debugging.setPassword(password);
		this.debugging.connect(); // connection has been created

		/* Create command session for admin1 */
		Session debugSession = this.debugging;
		Channel debugChannel = debugSession.openChannel("shell");

		/* Output and Command list definition */
		outputDebugging = debugChannel.getOutputStream();
		printstreamDebugging = new PrintStream(outputDebugging, true);

		/* Start to command burning */
		debugChannel.connect();

		InputStream inputDebugging = debugChannel.getInputStream();

		// command lists
		printstreamDebugging.println(
				"runon --app \"./logcap.sh -u all\""); /*
														 * activate debug level
														 * for all tiers
														 */
		controller.setLogScreenArea("\n[INFORMATION]: Application Tier Debug Level has been closed");

		/* Killing threads */

		printResult(inputDebugging, debugChannel);

		debugSession.disconnect();
		printstreamDebugging.close();

	}

	public void closeAdmDebug(String host, String user, String password) throws Throwable {

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");

		JSch jsch = new JSch();

		this.debugging = jsch.getSession(user, host, 22); // create a channel
		this.debugging.setConfig(config);

		this.debugging.setPassword(password);
		this.debugging.connect(); // connection has been created

		/* Create command session for admin1 */
		Session debugSession = this.debugging;
		Channel debugChannel = debugSession.openChannel("shell");

		/* Output and Command list definition */
		outputDebugging = debugChannel.getOutputStream();
		printstreamDebugging = new PrintStream(outputDebugging, true);

		/* Start to command burning */
		debugChannel.connect();

		InputStream inputDebugging = debugChannel.getInputStream();

		// command lists
		printstreamDebugging.println(
				"runon --adm \"./logcap.sh -u all\""); /*
														 * activate debug level
														 * for all tiers
														 */
		controller.setLogScreenArea("\n[INFORMATION]: Admin Tier Debug Level has been closed");

		/* Killing threads */

		printResult(inputDebugging, debugChannel);

		debugSession.disconnect();
		printstreamDebugging.close();

	}

	public void closeBrokerDebug(String host, String user, String password) throws Throwable {

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");

		JSch jsch = new JSch();

		this.debugging = jsch.getSession(user, host, 22); // create a channel
		this.debugging.setConfig(config);

		this.debugging.setPassword(password);
		this.debugging.connect(); // connection has been created

		/* Create command session for admin1 */
		Session debugSession = this.debugging;
		Channel debugChannel = debugSession.openChannel("shell");

		/* Output and Command list definition */
		outputDebugging = debugChannel.getOutputStream();
		printstreamDebugging = new PrintStream(outputDebugging, true);

		/* Start to command burning */
		debugChannel.connect();

		InputStream inputDebugging = debugChannel.getInputStream();

		// command lists
		printstreamDebugging.println(
				"runon --turn --broker \"./logcap.sh -u all\""); /*
																	 * activate
																	 * debug
																	 * level for
																	 * all tiers
																	 */
		controller.setLogScreenArea("\n[INFORMATION]: Broker and TURN Tiers Debug Level have been closed");

		/* Killing threads */

		printResult(inputDebugging, debugChannel);

		debugSession.disconnect();
		printstreamDebugging.close();

	}

	public void printResult(InputStream input, Channel channel) throws Exception {

		byte[] tmp = new byte[1024];
		while (true) {
			while (input.available() > 0) {
				int i = input.read(tmp, 0, 1024);
				if (i < 0)
					break;
				System.out.print(new String(tmp, 0, i));
				controller.setLogScreenArea(new String(tmp, 0, i));
			}
			if (channel.isClosed()) {
				System.out.println("exit-status: " + channel.getExitStatus());
				controller.setLogScreenArea("exit-status: " + channel.getExitStatus());
				break;
			}
			try {
				Thread.sleep(1000);
				
			} catch (Exception ee) {
			}
		}

		channel.disconnect();
	}

	public void executeCommandsForHost3Tier(String logName) throws Exception {
		// TODO Auto-generated method stub

		/* Create command session for admin1 */
		Session session3 = this.host3;
		Channel channel3 = session3.openChannel("shell");

		/* Output and Command list definition */
		ops3 = channel3.getOutputStream();
		ps3 = new PrintStream(ops3, true);

		/* Start to command burning */
		channel3.connect();

		InputStream input3 = channel3.getInputStream();

		/** Host4 log collection side */

		// command lists
		Thread.sleep(1000);
		ps3.println("host3"); /* Connect to host3 */

		Thread.sleep(1000);

		ps3.println("./logcap.sh -c " + logName + "_host3"
				+ " -z"); /* run script for log collection */
		printResult(input3, channel3);

	}

	public void executeCommandsForHost4Tier(String logName) throws Exception {

		// TODO Auto-generated method stub

		/* Create command session for admin1 */
		Session session4 = this.host4;
		Channel channel4 = session4.openChannel("shell");

		/* Output and Command list definition */
		ops4 = channel4.getOutputStream();
		ps4 = new PrintStream(ops4, true);

		/* Start to command burning */
		channel4.connect();

		InputStream input4 = channel4.getInputStream();

		/** Host4 log collection side */

		// command lists
		Thread.sleep(1000);
		ps4.println("host4"); /* Connect to host4 */

		Thread.sleep(1000);

		Thread.sleep(500);

		ps4.println("./logcap.sh -c " + logName + "_host4"
				+ " -z"); /* run script for log collection */
		/* logcap.sh ı durdurma */

		// sftpHost3(ps3, session3, channel3, logName);

		printResult(input4, channel4);

	}

	public void stop() {

		String logNameForSCP = controller.getLogName();

		Thread stopThread = new Thread(new Runnable() {
			public void run() {

				try {
					stopExecution.stopLogCollection(ops1);
					// Thread.sleep(1000);
					stopExecution.stopLogCollection(ops2);
					// Thread.sleep(1000);
					stopExecution.stopLogCollection(ops3);
					// Thread.sleep(1000);
					stopExecution.stopLogCollection(ops4);
					// Thread.sleep(1000);
					stopExecution.stopLogCollection(ops5);

					// Thread.sleep(1000);
					stopExecution.stopLogCollection(ops6);
					// Thread.sleep(1000);
					stopExecution.stopLogCollection(ops7);
					// Thread.sleep(1000);
					stopExecution.stopLogCollection(ops8);
					// Thread.sleep(1000);
					stopExecution.stopLogCollection(ops9);
					// Thread.sleep(1000);

					if (controller.checkAdminTier()) {

						// oVerloaded method
						sourcePath1 = stopExecution.ThrownFilesAdminHost(logNameForSCP, ps1, host1);

						sourcePath2 = stopExecution.ThrownFilesAdminHost(logNameForSCP, ps2, 2, host2);
					}

					if (controller.checkApplicationTier()) {
						sourcePath3 = stopExecution.ThrownFilesAdminHost(logNameForSCP, ps3, 3, host3);

						sourcePath4 = stopExecution.ThrownFilesAdminHost(logNameForSCP, ps4, 4, host4);
					}

					if (controller.checkRestTier()) {

						sourcePath5 = stopExecution.ThrownFilesAdminHost(logNameForSCP, ps5, 5, host5);

						sourcePath6 = stopExecution.ThrownFilesAdminHost(logNameForSCP, ps6, 6, host6);

						sourcePath7 = stopExecution.ThrownFilesAdminHost(logNameForSCP, ps7, 7, host7);

						sourcePath8 = stopExecution.ThrownFilesAdminHost(logNameForSCP, ps8, 8, host8);

						sourcePath9 = stopExecution.ThrownFilesAdminHost(logNameForSCP, ps9, 9, host9);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		stopThread.setName("Stopping log collection");
		stopThread.start();

	}

	public void download(String host, String user, String password) throws Exception, JSchException {

		String destPath = controller.getDestPath();
		// String logNameForSCP = controller.getLogName();

		/*
		 * Null checking and connection checking if pass over all of them, lets
		 * download the logs
		 */
		/** Checking for admin tiers */

		if (controller.checkAdminTier()) {
			stopExecution.downloadLogs(sourcePath1, destPath, user, host, password);
			stopExecution.downloadLogs(sourcePath2, destPath, user, host, password);
		}

		/** Checking for application tiers */

		if (controller.checkApplicationTier()) {
			stopExecution.downloadLogs(sourcePath3, destPath, user, host, password);
			stopExecution.downloadLogs(sourcePath4, destPath, user, host, password);
		}

		/** Checking for Broker and Turn tiers */
		if (controller.checkRestTier()) {
			stopExecution.downloadLogs(sourcePath5, destPath, user, host, password);
			stopExecution.downloadLogs(sourcePath6, destPath, user, host, password);
			stopExecution.downloadLogs(sourcePath7, destPath, user, host, password);
			stopExecution.downloadLogs(sourcePath8, destPath, user, host, password);
			stopExecution.downloadLogs(sourcePath9, destPath, user, host, password);
		}
	}

	public boolean clearCache() throws Exception {

		boolean isDeleted = false;

		// Impact -> Extra bir thread
		// TODO Auto-generated method stub

		/* Create command session for admin1 */
		Session sessionForCleaning = this.host1;
		Channel channelForCleaning = sessionForCleaning.openChannel("shell");

		/* Output and Command list definition */
		opsForCleaning = channelForCleaning.getOutputStream();
		psForCleaning = new PrintStream(opsForCleaning, true);

		/* Start to command burning */
		channelForCleaning.connect();

		InputStream inputForCleaning = channelForCleaning.getInputStream();

		/** Host1 log collection site */

		// command lists
		Thread.sleep(1000);
		psForCleaning.println("runon 3 4 \"rm -rf spidrlogs\"");
		Thread.sleep(1500);
		psForCleaning.println("exit");

		isDeleted = true;

		Thread.sleep(1000);

		printResult(inputForCleaning, channelForCleaning);

		channelForCleaning.disconnect();
		host1.disconnect();

		return isDeleted;
	}

	public void executeCommandsForHost1Tier(String logName) throws Exception {
		// TODO Auto-generated method stub

		/* Create command session for admin1 */
		Session session1 = this.host1;
		Channel channel1 = session1.openChannel("shell");

		/* Output and Command list definition */
		ops1 = channel1.getOutputStream();
		ps1 = new PrintStream(ops1, true);

		/* Start to command burning */
		channel1.connect();

		InputStream input1 = channel1.getInputStream();

		/** Host1 log collection side */

		// command lists

		Thread.sleep(500);
		ps1.println("./logcap.sh -c " + logName + "_host1"
				+ " -z"); /* run script for log collection */

		printResult(input1, channel1);

	}

	public void executeCommandsForHost2Tier(String logName) throws Exception {
		// TODO Auto-generated method stub

		/* Create command session for admin1 */
		Session session2 = this.host2;
		Channel channel2 = session2.openChannel("shell");

		/* Output and Command list definition */
		ops2 = channel2.getOutputStream();
		ps2 = new PrintStream(ops2, true);

		/* Start to command burning */
		channel2.connect();

		InputStream input2 = channel2.getInputStream();

		/** Host4 log collection side */

		Thread.sleep(1000);
		ps2.println("host2");
		// command lists
		Thread.sleep(1000);

		Thread.sleep(500);
		ps2.println("./logcap.sh -c " + logName + "_host2"
				+ " -z"); /* run script for log collection */
		/* logcap.sh ı durdurma */

		/*
		 * stop buttonunun methoduna aktarılacaktır gerek yok da olabilir de???
		 */

		printResult(input2, channel2);

	}

	public void executeCommandsForHost5Tier(String logName) throws Exception, JSchException {
		// TODO Auto-generated method stub

		/* Create command session for admin1 */
		Session session5 = this.host5;
		Channel channel5 = session5.openChannel("shell");

		/* Output and Command list definition */
		ops5 = channel5.getOutputStream();
		ps5 = new PrintStream(ops5, true);

		/* Start to command burning */
		channel5.connect();

		InputStream input5 = channel5.getInputStream();

		/** Host5 log collection side */

		Thread.sleep(1000);

		ps5.println("host5");

		// command lists
		Thread.sleep(500);
		ps5.println("./logcap.sh -c " + logName + "_host5"
				+ " -z"); /* run script for log collection */

		printResult(input5, channel5);

	}

	public void executeCommandsForHost6Tier(String logName) throws Exception {
		// TODO Auto-generated method stub

		/* Create command session for host6 */
		Session session6 = this.host6;
		Channel channel6 = session6.openChannel("shell");

		/* Output and Command list definition */
		ops6 = channel6.getOutputStream();
		ps6 = new PrintStream(ops6, true);

		/* Start to command burning */
		channel6.connect();

		InputStream input6 = channel6.getInputStream();

		/** Host5 log collection side */

		Thread.sleep(1000);
		ps6.println("host6");
		// command lists
		Thread.sleep(1000);

		Thread.sleep(500);
		ps6.println("./logcap.sh -c " + logName + "_host6"
				+ " -z"); /* run script for log collection */

		printResult(input6, channel6);

	}

	public void executeCommandsForHost7Tier(String logName) throws Exception {
		// TODO Auto-generated method stub

		/* Create command session for admin1 */
		Session session7 = this.host7;
		Channel channel7 = session7.openChannel("shell");

		/* Output and Command list definition */
		ops7 = channel7.getOutputStream();
		ps7 = new PrintStream(ops7, true);

		/* Start to command burning */
		channel7.connect();

		InputStream input7 = channel7.getInputStream();

		/** Host7 log collection side */

		Thread.sleep(1000);
		ps7.println("host7");
		// command lists
		Thread.sleep(1000);

		Thread.sleep(500);
		ps7.println("./logcap.sh -c " + logName + "_host7"
				+ " -z"); /* run script for log collection */

		printResult(input7, channel7);

	}

	public void executeCommandsForHost8Tier(String logName) throws Exception {
		// TODO Auto-generated method stub

		/* Create command session for admin1 */
		Session session8 = this.host8;
		Channel channel8 = session8.openChannel("shell");

		/* Output and Command list definition */
		ops8 = channel8.getOutputStream();
		ps8 = new PrintStream(ops8, true);

		/* Start to command burning */
		channel8.connect();

		InputStream input8 = channel8.getInputStream();

		/** Host8 log collection side */

		Thread.sleep(1000);
		ps8.println("host8");
		// command lists
		Thread.sleep(1000);

		Thread.sleep(500);
		ps8.println("./logcap.sh -c " + logName + "_host8"
				+ " -z"); /* run script for log collection */

		printResult(input8, channel8);

	}

	public void executeCommandsForHost9Tier(String logName) throws Exception {
		// TODO Auto-generated method stub

		/* Create command session for admin1 */
		Session session9 = this.host9;

		Channel channel9 = session9.openChannel("shell");

		/* Output and Command list definition */
		ops9 = channel9.getOutputStream();
		ps9 = new PrintStream(ops9, true);

		/* Start to command burning */
		channel9.connect();

		InputStream input9 = channel9.getInputStream();

		/** Host5 log collection side */

		Thread.sleep(1000);
		ps9.println("host9");

		if (ps9.checkError())
			check.setConnectedToHost9(false);

		// command lists
		Thread.sleep(1000);

		Thread.sleep(500);
		ps9.println("./logcap.sh -c " + logName + "_host9"
				+ " -z"); /* run script for log collection */

		printResult(input9, channel9);

	}

}
