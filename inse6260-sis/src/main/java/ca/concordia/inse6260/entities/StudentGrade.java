package ca.concordia.inse6260.entities;

import java.math.BigDecimal;

/**
 * DTO to map student to grade.
 */
public class StudentGrade {

	private String studentUsername;
	private BigDecimal grade;
	
	public String getStudentUsername() {
		return studentUsername;
	}
	public void setStudentUsername(String studentUsername) {
		this.studentUsername = studentUsername;
	}
	public BigDecimal getGrade() {
		return grade;
	}
	public void setGrade(BigDecimal grade) {
		this.grade = grade;
	}
	
	
}
