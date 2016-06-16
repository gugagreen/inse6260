package ca.concordia.inse6260.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ca.concordia.inse6260.entities.enums.StudentOrigin;

@Entity
@DiscriminatorValue("S")
@Table(name = "student")
public class Student extends User {

	@Column(name="origin", nullable = false)
	@Enumerated(EnumType.STRING)
	private StudentOrigin origin;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<AcademicRecordEntry> academicRecords;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Payment> payments;
	
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
	public List<Payment> getPayments() {
		return payments;
	}
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
}
