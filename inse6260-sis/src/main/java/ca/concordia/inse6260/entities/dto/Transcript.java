package ca.concordia.inse6260.entities.dto;

import java.util.List;

import ca.concordia.inse6260.entities.AcademicRecordEntry;

/**
 * DTO for transcript.
 */
public class Transcript {

	private String studentUsername;
	private List<AcademicRecordEntry> academicRecords;
	private String gpa;
	private List<String> termGPA;
	
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
	public List<String> getTermGPA() {
		return termGPA;
	}
	public void setTermGPA(List<String> termGPA) {
		this.termGPA = termGPA;
	}
	
	
}
