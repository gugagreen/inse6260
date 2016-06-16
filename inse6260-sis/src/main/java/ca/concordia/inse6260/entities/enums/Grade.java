package ca.concordia.inse6260.entities.enums;

public enum Grade {

	NOT_SET(0, 0, 0), A_PLUS(100, 96, 43), A(95, 91, 40), A_MINUS(90, 86, 37), B_PLUS(85, 81, 33), B(80, 76, 30), B_MINUS(75, 71, 27), 
	C_PLUS(70, 66, 23), C(65, 61, 20), C_MINUS(60, 50, 17), F(49, 0, 0);
	
	private int maxPoint;
	private int minPoint;
	private float cumGPA;

	private Grade(int maxPoint, int minPoint, float GPAPoint) {
		this.maxPoint = maxPoint;
		this.minPoint = minPoint;
		this.cumGPA = GPAPoint;
	}

	public int getMaxPoint() {
		return maxPoint;
	}
	
	public float getGPAPoint(){
		return cumGPA/10;
	}

	public int getMinPoint() {
		return minPoint;
	}
}
