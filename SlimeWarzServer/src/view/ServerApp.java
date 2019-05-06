package view;

import viewModel.Helper;
import model.Observer;
import model.SlimeServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;

public class ServerApp extends JFrame implements Observer {
	private final Dimension SERVER_FRAME_DIM = new Dimension(500, 300);

	private Helper helper = Helper.getInstance();
	private InetAddress ip;
	private JTextField portText;
	private JTextArea serverLog;
	private int portNum;
	private SlimeServer slimeServer;

	public static void main(String[] args) {
		ServerApp serverApp = new ServerApp();
		serverApp.loadGUI();
	}

	private void loadGUI() {
		final String MSG_PORT_LABEL = "Enter the PORT number (1024-49151): ";
		final String MSG_CURRENT_IP = "Current IP: ";
		final String MSG_PORT_NUMBER = "8888";
		final String MSG_CREATE_BUTTON = "Create a Socket";
		final String MSG_SERVER_LOG = "Please let the client player know your IP address and Port number.";
		final String MSG_CLOSING_SERVER = "Closing Server";

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("SlimeWars server");
		setPreferredSize(SERVER_FRAME_DIM);
		setResizable(false);
		setLayout(null);
		getIP();

		JPanel serverPanel = new JPanel();
		serverPanel.setLayout(null);
		helper.attach(this, serverPanel, 0, 200, 500, 300);
		JLabel currentIPLabel = new JLabel(MSG_CURRENT_IP);
		JTextField currentIP = new JTextField(getIP());
		currentIP.setEditable(false);
		JLabel portLabel = new JLabel(MSG_PORT_LABEL);
		portText = new JTextField(MSG_PORT_NUMBER);

		helper.attach(this, currentIPLabel, 30, 30, 80, 25);
		helper.attach(this, currentIP, 110, 30, 100, 25);
		helper.attach(this, portLabel, 30, 60, 250, 25);
		helper.attach(this, portText, 280, 60, 50, 25);

		serverLog = new JTextArea();
		serverLog.setEditable(false);
		serverLog.append(MSG_SERVER_LOG);
		JScrollPane sp = new JScrollPane(serverLog);
		JButton createServerSocketBtn = new JButton(MSG_CREATE_BUTTON);

		helper.attach(this, sp, 30, 150, 400, 100);
		helper.attach(this, createServerSocketBtn, 30, 100, 150, 40);

		createServerSocketBtn.addActionListener(e -> createServerSocket());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (slimeServer != null) {
					try {
						slimeServer.close();
						System.out.println(MSG_CLOSING_SERVER);
					} catch (IOException err) {
						System.out.println(err.getMessage());
					}
				}
			}
		});
		pack();
		setVisible(true);
	}

	private void createServerSocket() {
		try {
			portNum = Integer.parseInt(portText.getText());
			slimeServer = new SlimeServer(portNum);
			slimeServer.start();
			slimeServer.addObserver(this);
		} catch (IOException e) {
			String errorMSG = "PORT #" + portNum + " is already opened.\r\n";
			printLog(errorMSG);
		}
	}

	private void printLog(String str) {
		serverLog.append(str + "\r\n");
	}

	private String getIP() {
		try {
			ip = InetAddress.getLocalHost();
		} catch (Exception ignored) {}
		return String.valueOf(ip.getHostAddress());
	}

	@Override
	public void update(String message) {
		printLog(message);
	}
}