package view;

import helper.Observer;
import helper.Pair;
import viewModel.GameManager;
import helper.ComponentAttacher;

import javax.swing.*;
import java.awt.*;

public class ClientApp extends JFrame implements Observer {
	private static final int LINE_COUNT = 7;
	private final Dimension CLIENT_FRAME_DIM = new Dimension(768, 550);
	private final Rectangle CLIENT_FRAME_RECT = new Rectangle(CLIENT_FRAME_DIM);
	private Icon oIcon = new ImageIcon(this.getClass().getResource("/res/red.png"));
	private Icon xIcon = new ImageIcon(this.getClass().getResource("/res/blue.png"));
	private Icon border = new ImageIcon(this.getClass().getResource("/res/border.png"));
	private JButton[][] squares;

	private GameManager gameManager;

	private void start() {
		gameManager = new GameManager(LINE_COUNT);
		gameManager.addObserver(this);
		initView();
		gameManager.startProcedure();
		drawBoard();
	}

	private void drawBoard() {
		for (int y = 0; y < LINE_COUNT; y++) {
			for (int x = 0; x < LINE_COUNT; x++) {
				Pair temp = new Pair(x, y);
				switch (gameManager.getBoard().get(temp)) {
					case 0:
						squares[y][x].setIcon(oIcon);
						break;
					case 1:
						squares[y][x].setIcon(xIcon);
						break;
					case 2:
						squares[y][x].setIcon(border);
						break;
					default:
						squares[y][x].setIcon(null);
						break;
				}
			}
		}

		//gameManager.setInfoPanel();
	}

	private void initView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("SlimeWars Client");
		setPreferredSize(CLIENT_FRAME_DIM);
		setResizable(false);
		setLayout(null);
		ComponentAttacher.attach(this, gamePanel(), CLIENT_FRAME_RECT);
		pack();
		setVisible(true);
	}

	private JPanel gamePanel() {
		JPanel returnPanel = new JPanel(null);
		ComponentAttacher.attach(returnPanel, playPanel(), 0, 0, 512, 512);
		ComponentAttacher.attach(returnPanel, infoPanel(), 512, 0, 256, 512);

		return returnPanel;
	}

	private JPanel playPanel() {
		JPanel returnPanel = new JPanel();
		squares = new JButton[LINE_COUNT][LINE_COUNT];
		returnPanel.setLayout(new GridLayout(LINE_COUNT, LINE_COUNT));
		for (int y = 0; y < LINE_COUNT; y++) {
			for (int x = 0; x < LINE_COUNT; x++) {
				squares[y][x] = new JButton();
				returnPanel.add(squares[y][x]);

				Pair currentCoord = new Pair(x, y);
				squares[y][x].addActionListener(e -> { // set clickEvent to each of buttons
					gameManager.clickEvent(currentCoord);
				});
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

		ComponentAttacher.attach(returnPanel, currentTurnInfo, 10, 100, 200, 25);
		ComponentAttacher.attach(returnPanel, currentTurnCount, 10, 120, 200, 25);
		ComponentAttacher.attach(returnPanel, p1CellInfo, 0, 150, 200, 25);
		ComponentAttacher.attach(returnPanel, p2CellInfo, 0, 180, 200, 25);
		ComponentAttacher.attach(returnPanel, selectedCellInfo, 0, 210, 200, 25);
		return returnPanel;
	}

	public static void main(String[] args) {
		ClientApp clientApp = new ClientApp();
		clientApp.start();
	}

	@Override
	public void update() {
		drawBoard();
	}
}
