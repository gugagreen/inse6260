package ca.concordia.inse6260.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import ca.concordia.inse6260.entities.enums.AcademicRecordStatus;
import ca.concordia.inse6260.entities.enums.Grade;

@Entity
public class AcademicRecordEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;
	@ManyToOne
	private CourseEntry courseEntry;
	@Column(name = "grade", nullable = false)
	@Enumerated(EnumType.STRING)
	private Grade grade;
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private AcademicRecordStatus status;
	
	public AcademicRecordEntry() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CourseEntry getCourseEntry() {
		return courseEntry;
	}

	public void setCourseEntry(CourseEntry courseEntry) {
		this.courseEntry = courseEntry;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public AcademicRecordStatus getStatus() {
		return status;
	}

	public void setStatus(AcademicRecordStatus status) {
		this.status = status;
	}
}
