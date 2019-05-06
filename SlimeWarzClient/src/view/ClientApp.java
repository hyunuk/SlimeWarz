package view;

import viewModel.ConnectionManager;
import viewModel.GameManager;
import viewModel.Helper;
import model.Observer;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;

public class ClientApp extends JFrame implements Observer {
	private static final int LINE_COUNT = 7;
	private final Dimension CLIENT_FRAME_DIM = new Dimension(760, 760);
	private final Rectangle CLIENT_FRAME_RECT = new Rectangle(CLIENT_FRAME_DIM);
	private Helper helper = Helper.getInstance();
	private JButton gameStartBtn, cancelBtn;
	private JTextArea clientLog;
	private JTextField ipTextField, portTextField, nameField;
	private ConnectionManager connectionManager;
	private GameManager gameManager;

	public ClientApp() {
		connectionManager = new ConnectionManager();
		connectionManager.addObserver(this);
		gameManager = new GameManager();
	}

	private void loadGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("SlimeWars Client");
		setPreferredSize(CLIENT_FRAME_DIM);
		setResizable(false);
		setLayout(null);
		helper.attach(this, titlePanel(), CLIENT_FRAME_RECT);

		pack();
		setVisible(true);
	}

	private JPanel gamePanel() {
		JPanel returnPanel = new JPanel(null);
		helper.attach(returnPanel, playPanel(), 0, 0, 500, 500);
		helper.attach(returnPanel, infoPanel(), 500, 0, 200, 500);
		helper.attach(returnPanel, chatPanel(), 0, 500, 700, 200);

		return returnPanel;
	}

	private JPanel titlePanel() {
		JPanel returnPanel = new JPanel(null);
		helper.attach(returnPanel, titleImg(), 0, 0, 700, 180);
		helper.attach(returnPanel, clientPanel(), 25, 200, 640, 300);
		helper.attach(returnPanel, bottomPanel(), 0, 500, 700, 200);
		return returnPanel;
	}

	private void createSocketBtnEvent() {
		final String FOUND_SERVER = "Found server, connected.";
		final String ALREADY_CONNECTED = "You are already connected.";

		String hostname = ipTextField.getText();
		int port = Integer.parseInt(portTextField.getText());
		try {
			if (connectionManager.getClientSocket() == null) {
				connectionManager.setThreads(hostname, port);
				connectionManager.startThreads();
				printLog(FOUND_SERVER);
				nameField.setEditable(false);
			} else {
				printLog(ALREADY_CONNECTED);
			}
		} catch (IOException e) {
			printLog(e.getMessage());
		}
	}

	private void readyBtnEvent() {
		if (connectionManager.getClientSocket() != null) {
			gameStartBtn.setText("Waiting..");
			gameStartBtn.setEnabled(false);
			printLog("Ready for the game. Waiting another player.");
			cancelBtn.setVisible(true);
		} else {
			printLog("You are not connected yet.");
		}
	}

	private void gameStart() {
		getContentPane().removeAll();
		getContentPane().add(gamePanel());
		revalidate();
		repaint();

		gameManager.startProcedure();
	}

	private void cancelBtnEvent() {
		gameStartBtn.setText("Game Start");
		gameStartBtn.setEnabled(true);
		cancelBtn.setVisible(false);
		clientLog.append("Waiting cancelled.\r\n");
	}

	private JLabel titleImg() {
		Icon title = new ImageIcon(this.getClass().getResource("/resource/Title.png"));
		return new JLabel(title);
	}

	private JPanel clientPanel() {
		final String CLIENT_CONNECTION = "Client Connection";
		final String MSG_IP_ADDRESS = "Please enter the IP address of the server.";
		final String MSG_PORT_NUMBER = "Please enter the PORT number of the server.";
		final String PORT_DEFAULT = "8888";
		final String CREATE_A_SOCKET = "Create a Socket";
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(null);
		returnPanel.setBorder(new TitledBorder(new EtchedBorder(), CLIENT_CONNECTION));

		JLabel ipLabel = new JLabel(MSG_IP_ADDRESS);
		JLabel portLabel = new JLabel(MSG_PORT_NUMBER);
		ipTextField = new JTextField();
		portTextField = new JTextField();
		clientLog = new JTextArea();
		JScrollPane sp = new JScrollPane(clientLog);
		JButton createClientSocketBtn = new JButton(CREATE_A_SOCKET);

		helper.attach(returnPanel, ipLabel, 30, 30, 400, 20);
		helper.attach(returnPanel, portLabel, 30, 80, 400, 20);

		ipTextField.setEditable(true);
		helper.attach(returnPanel, ipTextField, 30, 50, 200, 25);
		portTextField.setEditable(true);
		portTextField.setText(PORT_DEFAULT);
		helper.attach(returnPanel, portTextField, 30, 100, 50, 25);

		clientLog.setEditable(false);
		helper.attach(returnPanel, sp, 30, 180, 500, 100);
		helper.attach(returnPanel, createClientSocketBtn, 30, 130, 150, 40);
		createClientSocketBtn.addActionListener(e -> createSocketBtnEvent());

		return returnPanel;
	}

	private JPanel bottomPanel() {
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(null);
		gameStartBtn = new JButton("Ready");
		cancelBtn = new JButton("Cancel");
		JLabel nameLabel = new JLabel("Enter your name: ");
		nameField = new JTextField("Player " + (int)(Math.random() * 1000));

		helper.attach(returnPanel, gameStartBtn, 100, 80, 200, 40);
		helper.attach(returnPanel, cancelBtn, 360, 80, 150, 40);
		helper.attach(returnPanel, nameLabel, 130, 30, 120, 25);
		helper.attach(returnPanel, nameField, 300, 30, 200, 25);
		cancelBtn.setVisible(false);

		gameStartBtn.addActionListener(e -> readyBtnEvent());
		cancelBtn.addActionListener(e -> cancelBtnEvent());
		return returnPanel;
	}

	private void printLog(String str) {
		clientLog.append(str + "\r\n");
	}

	private JPanel playPanel() {
		JPanel returnPanel = new JPanel();
		JButton[][] squares = new JButton[LINE_COUNT][LINE_COUNT];
		returnPanel.setLayout(new GridLayout(LINE_COUNT, LINE_COUNT));
		for (int y = 0; y < LINE_COUNT; y++) {
			for (int x = 0; x < LINE_COUNT; x++) {
				squares[y][x] = new JButton();
				returnPanel.add(squares[y][x]);
			}
		}
		return returnPanel;
	}

	private JPanel infoPanel() {
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(null);
		JLabel currentTurnInfo = new JLabel();
		JLabel currentTurnCount = new JLabel();
		JLabel p1CellInfo = new JLabel();
		JLabel p2CellInfo = new JLabel();
		JLabel selectedCellInfo = new JLabel();

		helper.attach(returnPanel, currentTurnInfo, 10, 100, 200, 25);
		helper.attach(returnPanel, currentTurnCount, 10, 120, 200, 25);
		helper.attach(returnPanel, p1CellInfo, 0, 150, 200, 25);
		helper.attach(returnPanel, p2CellInfo, 0, 180, 200, 25);
		helper.attach(returnPanel, selectedCellInfo, 0, 210, 200, 25);
		return returnPanel;
	}

	private JPanel chatPanel() {
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(null);
		JTextField chatInputField = new JTextField();
		JButton chatSendBtn = new JButton("Send");
		JTextArea chatDisplayArea = new JTextArea();
		chatDisplayArea.setEditable(false);
		JScrollPane sp = new JScrollPane(chatDisplayArea);

		helper.attach(returnPanel, chatInputField, 0, 140, 500, 25);
		helper.attach(returnPanel, chatSendBtn, 500, 140, 80, 25);
		helper.attach(returnPanel, sp, 0, 0, 650, 140);
		return returnPanel;
	}

	public static void main(String[] args) {
		ClientApp clientApp = new ClientApp();
		clientApp.loadGUI();
	}

	@Override
	public void update(String message) {
		if (message.startsWith("[START]")) {
			gameStart();
		}
	}
}
