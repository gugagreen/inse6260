package ca.concordia.inse6260.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import ca.concordia.inse6260.dao.CourseEntryDAO;
import ca.concordia.inse6260.dao.StudentDAO;
import ca.concordia.inse6260.entities.AcademicRecordEntry;
import ca.concordia.inse6260.entities.AcademicRecordStatus;
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.services.CartService;

@Component
public class DefaultCartService implements CartService {
	
	@Resource
	private StudentDAO studentDao;
	@Resource
	private CourseEntryDAO courseEntryDao;

	@Override
	public List<AcademicRecordEntry> findCartByStudent(final String username) {
		List<AcademicRecordEntry> records = null;
		Student student = studentDao.findOne(username);
		if (student != null) {
			records = student.getAcademicRecords();
		}
		// FIXME - else - throw exception
		return records;
	}

	@Override
	public void addCourseForStudent(final String username, final long courseEntryId) {
		Student student = studentDao.findOne(username);
		if (student != null) {
			final List<AcademicRecordEntry> records = student.getAcademicRecords();
			if (!hasCourse(records, courseEntryId)) {
				CourseEntry courseEntry = courseEntryDao.findOne(courseEntryId);
				AcademicRecordEntry entry = new AcademicRecordEntry();
				entry.setCourseEntry(courseEntry);
				// TODO - treat other status
				entry.setStatus(AcademicRecordStatus.REGISTERED);
				records.add(entry);
				studentDao.save(student);
			} else {
				// FIXME - throw exception
			}
		} else {
			// FIXME - throw exception
		}
	}
	
	private boolean hasCourse(final List<AcademicRecordEntry> records, final long courseEntryId) {
		boolean hasCourse = false;
		for (AcademicRecordEntry record : records) {
			if ((record.getCourseEntry() != null) && (record.getCourseEntry().getId() == courseEntryId)) {
				hasCourse = true;
				break;
			}
		}
		return hasCourse;
	}

}
