package viewModel;

import helper.CellHelper;
import helper.Observable;
import helper.Observer;
import helper.Pair;
import model.Player;

import java.util.*;

public class GameManager implements Observable {
	private final int LINE_COUNT;
	private List<Player> players;
	private Map<Pair, Integer> board;
	private int currentPlayerIndex = 0;
	private List<Observer> observers;

	private enum Status {notSelected, clicked, afterClicked}
	private Status status;
	private Pair selectedCell;
	private int turnCount;

	public GameManager(final int LINE_COUNT) {
		this.LINE_COUNT = LINE_COUNT;
		this.players = new ArrayList<>();
		this.board = new HashMap<>();
		this.observers = new ArrayList<>();

		this.players.add(new Player(0));
		this.players.add(new Player(1));
	}

	/**
	 * When click event happens, check the current status
	 * @param clickedCell represents currently clicked cell
	 */
	public void clickEvent(Pair clickedCell) {
		Player currPlayer = this.players.get(this.currentPlayerIndex);
		switch (status) {
			case notSelected:
				if (currentPlayerIndex != board.get(clickedCell)) break;
				if (isPlayerCell(clickedCell, currPlayer)) {
					this.selectedCell = clickedCell;
					this.status = Status.clicked;
					updateAvailableCells(selectedCell);
					notifyObserver();
				}
				break;

			case clicked:
				if (isPlayerCell(clickedCell, currPlayer)) {
					this.selectedCell = clickedCell;
					updateAvailableCells(selectedCell);
					clearAvailableCells();
					notifyObserver();
					break;
				}

				if (currentPlayerIndex == 0) {
					if (players.get(1).getCellCoords().contains(clickedCell)) break;

				} else if (currentPlayerIndex == 1) {
					if (players.get(0).getCellCoords().contains(clickedCell)) break;
				}

				this.status = Status.afterClicked;
				attack(clickedCell);
				notifyObserver();

				break;
			case afterClicked:
				this.status = Status.notSelected;
				notifyObserver();
				break;
		}
	}

	private void attack(Pair clickedCell) {
		int distance = CellHelper.getDistance(selectedCell, clickedCell);
		if (distance == 2) {
			removeCurrentCell();
		}
		moveCell(clickedCell);
		consumeCell(clickedCell);
		endTurn();
	}

	private void endTurn() {
		currentPlayerIndex = (currentPlayerIndex == 0) ? 1 : 0;

		if (!canContinue()) {
			gameOver();
			return;
		}
		clearAvailableCells();
		notifyObserver();
		turnCount++;
	}

	private void gameOver() {
		System.out.println("GAME OVER!");
	}

	private boolean canContinue() {
		List<Pair> temp;
		for (Map.Entry<Pair, Integer> entry : board.entrySet()) {
			if (entry.getValue() == 0 || entry.getValue() == 1) {
				temp = findAvailableCells(entry.getKey());
				if (temp.size() > 0) return true;
			}
		}
		return false;
	}

	private void consumeCell(Pair clickedCell) {
		Player currPlayer = players.get(currentPlayerIndex);
		Player otherPlayer = (currentPlayerIndex == 0) ? players.get(1) : players.get(0);
		clearAvailableCells();

		List<Pair> neighbors = findNeighborCells(clickedCell, 1);

		for (Pair pair : neighbors) {
			if (board.get(pair).equals(0)) {
				board.put(pair, currentPlayerIndex);
				currPlayer.add(pair);
				otherPlayer.remove(pair);
			}

		}

	}

	private void moveCell(Pair clickedCell) {
		clearAvailableCells();
		board.put(clickedCell, currentPlayerIndex);
		players.get(currentPlayerIndex).add(clickedCell.getX(), clickedCell.getY());
	}

	private void removeCurrentCell() {
		board.put(selectedCell, Integer.MAX_VALUE);
		players.get(currentPlayerIndex).getCellCoords().remove(selectedCell);
	}

	private void updateAvailableCells(Pair selectedCell) {
		List<Pair> list = findAvailableCells(selectedCell);
		for (Pair pair : list) {
			if (board.containsKey(pair)) board.put(pair, 2);
		}
		System.out.println("update available cells!");
	}

	private void clearAvailableCells() {
		for (Map.Entry<Pair, Integer> entry : board.entrySet()) {
			if (entry.getValue() == 2) entry.setValue(Integer.MAX_VALUE);
		}
	}

	private List<Pair> findAvailableCells(Pair base) {
		List<Pair> retList = findNeighborCells(base, 2);
		for (Map.Entry<Pair, Integer> entry : board.entrySet()) {
			if (entry.getValue() == 0 || entry.getValue() == 1) {
				retList.remove(entry.getKey());
			}
		}
		System.out.println("find available cells");
		return retList;
	}

	private List<Pair> findNeighborCells(Pair base, int gap) {
		List<Pair> retList = new ArrayList<>();
		for (int y = 0; y < LINE_COUNT; y++) {
			for (int x = 0; x < LINE_COUNT; x++) {
				Pair temp = new Pair(x, y);
				if (CellHelper.getDistance(base, temp) <= gap) {
					retList.add(temp);
				}
			}
		}
		return retList;
	}

	private boolean isPlayerCell(Pair cell, Player currPlayer) {
		return currPlayer.getCellCoords().contains(cell);
	}

	private void initCells() {
		this.players.get(0).add(0, 0);
		this.players.get(0).add(1, 0);
		this.players.get(0).add(LINE_COUNT - 1, LINE_COUNT - 1);
		this.players.get(0).add(LINE_COUNT - 2, LINE_COUNT - 1);

		this.players.get(1).add(LINE_COUNT - 1, 0);
		this.players.get(1).add(LINE_COUNT - 2, 0);
		this.players.get(1).add(0, LINE_COUNT - 1);
		this.players.get(1).add(1, LINE_COUNT - 1);

		this.board.clear();
		for (int y = 0; y < LINE_COUNT; y++) { // Initialize the board with max_int values
			for (int x = 0; x < LINE_COUNT; x++) {
				this.board.put(new Pair(x, y), Integer.MAX_VALUE);
			}
		}

		for (Player player : players) { // put each players' cells into the board
			for (Pair pair : player.getCellCoords()) {
				this.board.put(pair, player.getPlayerIndex());
			}
		}

		this.currentPlayerIndex = 0;
		this.status = Status.notSelected;
		selectedCell = new Pair(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	public Map<Pair, Integer> getBoard() {
		return board;
	}

	public void startProcedure() {
		initCells();
	}

	@Override
	public void addObserver(Observer o) {
		if (!observers.contains(o)) observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
	}

	@Override
	public void notifyObserver() {
		for (Observer o : observers) {
			o.update();
		}
	}
}
