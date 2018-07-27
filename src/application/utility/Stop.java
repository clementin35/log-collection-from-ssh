package application.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.NoSuchFileException;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import application.view.LogOverviewController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Stop {

	LogOverviewController controller = new LogOverviewController();
	ConnectionCheck check = new ConnectionCheck();

	String sourcePath;

	public int stopLogCollection(OutputStream os) throws IOException {
		// TODO Auto-generated method stub

		if (os != null) {
			os.write(0x03);
			os.flush();
		}

		return 0;
	}

	public String ThrownFilesAdminHost(String logName, PrintStream ps, int hostNumber, Session session)
			throws InterruptedException {

		if (session == null) {
			System.out.println("There is no host for " + hostNumber);
			System.out.println("Unreachable Host!!!");
			return null;
		}

		else {

			String path = "/root/" + logName + "_host" + hostNumber + ".zip";

			ps.println("exit");
			Thread.sleep(1500); // After 1.5 seconds, hopefully all exits was
								// runned.

			ps.println("getfrom " + hostNumber + " /root/spidrlogs/" + logName + "_host" + hostNumber + ".zip");
			Thread.sleep(2000);

			ps.close();
			session.disconnect();

			return path;
		}

	}

	public String ThrownFilesAdminHost(String logName, PrintStream ps, Session session) throws InterruptedException {

		String path = "/root/" + logName + "_host" + 1 + ".zip";

		ps.println("getfrom " + 1 + " /root/spidrlogs/" + logName + "_host" + 1 + ".zip");
		Thread.sleep(1000);
		ps.close();
		session.disconnect();

		return path;

	}

	public boolean downloadLogs(String sourcePath, String destPath, String user, String host, String password)
			throws SftpException, JSchException {

		JSch jsch = new JSch();

		java.util.Properties config = new java.util.Properties();

		config.put("StrictHostKeyChecking", "no");

		Session session = jsch.getSession(user, host, 22); // Establish channel
		session.setConfig(config);
		session.setPassword(password);
		session.connect(); // Connect to FTP server

		ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
		sftpChannel.connect(); // Establishment of secure ftp line

		System.out.println(sourcePath);

		try {
			SftpATTRS attr = sftpChannel.stat(sourcePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block

			Platform.runLater(new Runnable() {
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Download Error");
					alert.setHeaderText("No VM Exist");
					alert.setContentText("Unable to connect " + host + " or there is no host called ");

					alert.showAndWait();

				}
			});

			sourcePath = "/root/install.log";
			e.printStackTrace();
		}

		System.out.println("Downloading test file");
		System.out.println("------->" + destPath);
		sftpChannel.get(sourcePath, destPath); // File download progress

		System.out.println("Downloaded");
		sftpChannel.exit();
		session.disconnect();
		System.out.println("Session disconnected -------------> ");

		return true;

	}

}
