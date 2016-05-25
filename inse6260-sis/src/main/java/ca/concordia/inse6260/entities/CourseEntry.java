package ca.concordia.inse6260.entities;

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
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQuery(name="CourseEntry.findBySeason", query="SELECT ce FROM CourseEntry ce, CourseDates cd "
		+ "WHERE ce.dates = cd AND cd.season = ?1 AND cd.startDate >= ?2")
public class CourseEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;
	@OneToOne(optional=false)
	@JoinColumn(name="course_id")
	private Course course;
	@OneToOne
	@JoinColumn(name="date_id")
	private CourseDates dates;
	private int size;
	@ManyToOne
	private User professor;
	@ManyToMany
	private List<User> students;

	public CourseEntry() {
		super();
		setStudents(new ArrayList<User>());
	}

	@Override
	public String toString() {
		return "CourseEntry [id=" + id + ", course=" + course + ", dates=" + dates + ", size=" + size + ", professor=" + professor
				+ ", students=" + students + "]";
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

	public List<User> getStudents() {
		return students;
	}

	public void setStudents(List<User> students) {
		this.students = students;
	}
}
