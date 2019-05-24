package view;

import model.Observer;
import viewModel.ConnectionManager;
import viewModel.Helper;
import viewModel.ServerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerApp extends JFrame implements Observer {
	private final Dimension SERVER_FRAME_DIM = new Dimension(500, 300);

	private Helper helper = Helper.getInstance();
	private JTextArea serverLog;
	private ServerManager serverManager = new ServerManager();
	private ConnectionManager connectionManager = new ConnectionManager();

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

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("SlimeWars server");
		setPreferredSize(SERVER_FRAME_DIM);
		setResizable(false);
		setLayout(null);

		JPanel serverPanel = new JPanel();
		JLabel currentIPLabel = new JLabel(MSG_CURRENT_IP);
		JTextField currentIP = new JTextField(connectionManager.getIP());
		JLabel portLabel = new JLabel(MSG_PORT_LABEL);
		JTextField portText = new JTextField(MSG_PORT_NUMBER);
		serverLog = new JTextArea(MSG_SERVER_LOG);
		JScrollPane sp = new JScrollPane(serverLog);
		JButton createServerSocketBtn = new JButton(MSG_CREATE_BUTTON);

		serverPanel.setLayout(null);
		currentIP.setEditable(false);
		serverLog.setEditable(false);
		helper.attach(this, serverPanel, 0, 200, 500, 300);
		helper.attach(this, currentIPLabel, 30, 30, 80, 25);
		helper.attach(this, currentIP, 110, 30, 100, 25);
		helper.attach(this, portLabel, 30, 60, 250, 25);
		helper.attach(this, portText, 280, 60, 50, 25);
		helper.attach(this, sp, 30, 150, 400, 100);
		helper.attach(this, createServerSocketBtn, 30, 100, 150, 40);

		createServerSocketBtn.addActionListener(e -> {
			connectionManager.createServer(Integer.parseInt(portText.getText()));
		});

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				connectionManager.closeServer();
			}
		});
		pack();
		setVisible(true);
	}

	private void printLog(String str) {
		serverLog.append(str + "\r\n");
	}

	@Override
	public void update(String message) {
		printLog(message);
	}
}