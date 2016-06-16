package ca.concordia.inse6260.entities.dto;

import java.math.BigDecimal;

import ca.concordia.inse6260.entities.CourseEntry;

public class AccountDebtEntry {

	private CourseEntry courseEntry;
	private BigDecimal value;
	
	public CourseEntry getCourseEntry() {
		return courseEntry;
	}
	public void setCourseEntry(CourseEntry courseEntry) {
		this.courseEntry = courseEntry;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	
}
