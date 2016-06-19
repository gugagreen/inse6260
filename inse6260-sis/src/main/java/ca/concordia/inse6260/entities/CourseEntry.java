package ca.concordia.inse6260.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CourseEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;
	@OneToOne(optional = false)
	@JoinColumn(name = "course_id")
	private Course course;
	@Column(name = "PREREQ_1_ID", nullable = false, updatable = false)
	private long prereq;
	@Column(name = "PREREQ_2_ID", nullable = false, updatable = false)
	private long prereq2;
	@Column(name = "base_cost")
	private BigDecimal baseCost;
	@OneToOne
	@JoinColumn(name = "date_id")
	private CourseDates dates;
	private int size;
	@ManyToOne
	private User professor;
	@JsonIgnore
	@ManyToMany
	private List<Student> students;

	public CourseEntry() {
		super();
		setStudents(new ArrayList<Student>());
	}

	@Override
	public String toString() {
		return "CourseEntry [id=" + id + ", course=" + course + ", baseCost=" + baseCost + ", dates=" + dates + ", size=" + size
				+ ", professor=" + professor + ", students=" + students + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	public long getPrereq() {
		return prereq;
	}

	public void setPrereq(long prereq) {
		this.prereq = prereq;
	}
	public long getPrereq2() {
		return prereq2;
	}

	public void setPrereq2(long prereq2) {
		this.prereq2 = prereq2;
	}

	public BigDecimal getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(BigDecimal baseCost) {
		this.baseCost = baseCost;
	}

	public CourseDates getDates() {
		return dates;
	}

	public void setDates(CourseDates dates) {
		this.dates = dates;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public User getProfessor() {
		return professor;
	}

	public void setProfessor(User professor) {
		this.professor = professor;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}
}
