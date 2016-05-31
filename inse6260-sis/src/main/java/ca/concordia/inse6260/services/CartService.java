package ca.concordia.inse6260.services;

import java.util.List;

import ca.concordia.inse6260.entities.AcademicRecordEntry;

public interface CartService {

	List<AcademicRecordEntry> findCartByStudent(final String username);
	
	void addCourseForStudent(final String username, final long courseEntryId);
}
