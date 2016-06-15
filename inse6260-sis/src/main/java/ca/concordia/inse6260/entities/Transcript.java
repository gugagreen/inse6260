package ca.concordia.inse6260.entities;

import java.util.List;

/**
 * DTO for transcript.
 */
public class Transcript {

	private String studentUsername;
	private List<AcademicRecordEntry> academicRecords;
	private String gpa;
	
	public String getStudentUsername() {
		return studentUsername;
	}
	public void setStudentUsername(String studentUsername) {
		this.studentUsername = studentUsername;
	}
	public List<AcademicRecordEntry> getAcademicRecords() {
		return academicRecords;
	}
	public void setAcademicRecords(List<AcademicRecordEntry> academicRecords) {
		this.academicRecords = academicRecords;
	}
	public String getGpa() {
		return gpa;
	}
	public void setGpa(String gpa) {
		this.gpa = gpa;
	}
	
	
}
