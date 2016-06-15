package ca.concordia.inse6260.entities;

/**
 * DTO to map student to grade.
 */
public class StudentGrade {

	private String studentUsername;
	private String grade;
	
	public String getStudentUsername() {
		return studentUsername;
	}
	public void setStudentUsername(String studentUsername) {
		this.studentUsername = studentUsername;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
}
