package helper;

public class CellHelper {
	public static int getDistance(Pair from, Pair to) {
		int xDist = from.getX() - to.getX();
		int yDist = from.getY() - to.getY();
		return (int) Math.sqrt(xDist * xDist + yDist * yDist);
	}
}
