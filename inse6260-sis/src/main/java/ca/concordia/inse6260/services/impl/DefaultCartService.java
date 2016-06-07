package ca.concordia.inse6260.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCartService.class);

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
		} else {
			LOGGER.debug("No student found with username: {}.", username);
			// FIXME - else - throw exception
		}
		return records;
	}

	@Override
	public void addCourseForStudent(final String username, final long courseEntryId) {
		Student student = studentDao.findOne(username);
		if (student != null) {
			final List<AcademicRecordEntry> records = student.getAcademicRecords();
			if (hasCourse(records, courseEntryId) == null) {
				CourseEntry courseEntry = courseEntryDao.findOne(courseEntryId);
				AcademicRecordEntry entry = new AcademicRecordEntry();
				entry.setCourseEntry(courseEntry);
				// TODO - treat other status
				entry.setStatus(AcademicRecordStatus.REGISTERED);
				records.add(entry);
				studentDao.save(student);
				
				// also add student to course entry list 
				courseEntry.getStudents().add(student);
				courseEntryDao.save(courseEntry);
			} else {
				LOGGER.info("Student {} already has course {} in his academic record.", username, courseEntryId);
				// FIXME - throw exception
			}
		} else {
			LOGGER.debug("No student found with username: {}.", username);
			// FIXME - throw exception
		}
	}

	@Override
	public void deleteCourseForStudent(String username, long courseEntryId) {
		Student student = studentDao.findOne(username);
		if (student != null) {
			final List<AcademicRecordEntry> records = student.getAcademicRecords();
			AcademicRecordEntry existentRecord = hasCourse(records, courseEntryId);
			if (existentRecord != null) {
				records.remove(existentRecord);
				studentDao.save(student);
				
				// also remove student from course entry list
				CourseEntry courseEntry = existentRecord.getCourseEntry();
				courseEntry.getStudents().remove(student);
				courseEntryDao.save(courseEntry);
			} else {
				LOGGER.info("Student {} does not have course {} in his academic record to be removed.", username, courseEntryId);
				// FIXME - throw exception
				// FIXME - handle when course cannot be removed
			}
		} else {
			LOGGER.debug("No student found with username: {}.", username);
			// FIXME - throw exception
		}
	}

	private AcademicRecordEntry hasCourse(final List<AcademicRecordEntry> records, final long courseEntryId) {
		AcademicRecordEntry existentRecord = null;
		for (AcademicRecordEntry record : records) {
			if ((record.getCourseEntry() != null) && (record.getCourseEntry().getId() == courseEntryId)) {
				existentRecord = record;
				break;
			}
		}
		return existentRecord;
	}

	public StudentDAO getStudentDao() {
		return studentDao;
	}

	public void setStudentDao(StudentDAO studentDao) {
		this.studentDao = studentDao;
	}

	public CourseEntryDAO getCourseEntryDao() {
		return courseEntryDao;
	}

	public void setCourseEntryDao(CourseEntryDAO courseEntryDao) {
		this.courseEntryDao = courseEntryDao;
	}
	
}
