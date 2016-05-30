package ca.concordia.inse6260.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("S")
@Table(name = "student")
public class Student extends User {

	@Column(name="origin", nullable = false)
	@Enumerated(EnumType.STRING)
	private StudentOrigin origin;
	@OneToMany
	private List<AcademicRecordEntry> academicRecords;
	
	public StudentOrigin getOrigin() {
		return origin;
	}
	public void setOrigin(StudentOrigin origin) {
		this.origin = origin;
	}
	public List<AcademicRecordEntry> getAcademicRecords() {
		return academicRecords;
	}
	public void setAcademicRecords(List<AcademicRecordEntry> academicRecords) {
		this.academicRecords = academicRecords;
	}
}
