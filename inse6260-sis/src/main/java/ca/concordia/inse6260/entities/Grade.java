package ca.concordia.inse6260.entities;

public enum Grade {

	NOT_SET(0, 0), A_PLUS(100, 96), A(95, 91), A_MINUS(90, 86), B_PLUS(85, 81), B(80, 76), B_MINUS(75, 71), 
	C_PLUS(70, 66), C(65, 61), C_MINUS(60, 50), F(49, 0);
	
	private int maxPoint;
	private int minPoint;

	private Grade(int maxPoint, int minPoint) {
		this.maxPoint = maxPoint;
		this.minPoint = minPoint;
	}

	public int getMaxPoint() {
		return maxPoint;
	}

	public int getMinPoint() {
		return minPoint;
	}
}
