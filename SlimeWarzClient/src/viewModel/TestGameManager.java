package viewModel;

import helper.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static java.lang.Integer.MAX_VALUE;
import static org.junit.jupiter.api.Assertions.*;

public class TestGameManager {
	GameManager gameManager;
	Pair pair;
	Map<Pair, Integer> board;
	enum Status {notSelected, clicked, afterClicked}
	Status status;
	int LINE_COUNT;


	@BeforeEach
	void setUp(){
		LINE_COUNT = 4;
		gameManager = new GameManager(LINE_COUNT);
		board = gameManager.getBoard();
		status = Status.notSelected;
		pair = new Pair(0, 0);
		gameManager.startProcedure();
	}

	//initial board triggers initCells and initPlayers, then check whether it success or not
	@Test
	void testBoard() {
		// check left top cells
		assertTrue(board.get(pair).equals(0));
		assertTrue(board.get(new Pair(1, 0)).equals(0));
		//check right bottom cells
		assertTrue(board.get(new Pair(LINE_COUNT - 1, LINE_COUNT - 1)).equals(0));
		assertTrue(board.get(new Pair(LINE_COUNT - 2, LINE_COUNT - 1)).equals(0));

		// check left bottom cells
		assertTrue(board.get(new Pair(LINE_COUNT - 1, 0)).equals(1));
		assertTrue(board.get(new Pair(LINE_COUNT - 2, 0)).equals(1));
		//check right top cells
		assertTrue(board.get(new Pair(0, LINE_COUNT - 1)).equals(1));
		assertTrue(board.get(new Pair(1, LINE_COUNT - 1)).equals(1));

		//check unoccupied cells
		assertTrue(board.get(new Pair(0, LINE_COUNT - 2)).equals(MAX_VALUE));
	}

	// check the 3*3 board's initial status
	@Test
	void testSmallBoard() {
		LINE_COUNT = 3;
		GameManager gameManagerSmall = new GameManager(LINE_COUNT);
		gameManagerSmall.startProcedure();
		board = gameManagerSmall.getBoard();
		// check left top cells
		assertTrue(board.get(pair).equals(0));
		assertTrue(board.get(new Pair(1, 0)).equals(MAX_VALUE));

		//check right bottom cells
		assertTrue(board.get(new Pair(LINE_COUNT - 1, LINE_COUNT - 1)).equals(1));
		assertTrue(board.get(new Pair(LINE_COUNT - 2, 0)).equals(MAX_VALUE));
	}

	@Test
	void testClickEventNotSelected() {
		assertTrue(gameManager.getCurrentPlayerIndex() == board.get(pair));
	}





}
