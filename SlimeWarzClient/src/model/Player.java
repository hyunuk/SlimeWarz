package model;

import java.util.ArrayList;

public class Player {
	private ArrayList<Integer> cellIndices;
	private int playerIndex;

	public Player() {
		this.cellIndices = new ArrayList<>();
	}

	public ArrayList<Integer> getCellIndices() {
		return cellIndices;
	}

	public void init(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public void add(int cell) {
		if (this.cellIndices.contains(cell)) {
			System.out.println("Occupied in " + cell);
			return;
		}
		this.cellIndices.add(cell);
	}

	private void remove(int cell) {
		this.cellIndices.remove(cellIndices.indexOf(cell));
	}


}
