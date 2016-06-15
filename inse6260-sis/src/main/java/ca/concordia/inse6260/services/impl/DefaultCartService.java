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
import ca.concordia.inse6260.entities.Grade;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.exception.CannotPerformOperationException;
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
			noStudentFound(username);
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
				entry.setGrade(Grade.NOT_SET);
				entry.setStatus(calculateStatus(courseEntry));
				records.add(entry);
				studentDao.save(student);
				
				// also add student to course entry list 
				courseEntry.getStudents().add(student);
				courseEntryDao.save(courseEntry);
			} else {
				String baseMsg = "Student %s already has course %d in his academic record.";
				String message = String.format(baseMsg, username, courseEntryId);
				LOGGER.debug(message);
				throw new CannotPerformOperationException(message);
			}
		} else {
			noStudentFound(username);
		}
	}
	
	private AcademicRecordStatus calculateStatus(CourseEntry courseEntry) {
		// by default, status will be registered
		AcademicRecordStatus status = AcademicRecordStatus.REGISTERED;
		// if course is full, goes to wait list
		if (courseEntry.getSize() <= courseEntry.getStudents().size()) {
			status = AcademicRecordStatus.WAIT_LIST;
		}
		
		return status;
	}

	@Override
	public void deleteCourseForStudent(String username, long courseEntryId) {
		Student student = studentDao.findOne(username);
		if (student != null) {
			final List<AcademicRecordEntry> records = student.getAcademicRecords();
			AcademicRecordEntry existentRecord = hasCourse(records, courseEntryId);
			if (existentRecord != null) {
				// check if course is not finished
				if (!AcademicRecordStatus.FINISHED.equals(existentRecord.getStatus())) {
					records.remove(existentRecord);
					studentDao.save(student);
					
					// also remove student from course entry list
					CourseEntry courseEntry = existentRecord.getCourseEntry();
					courseEntry.getStudents().remove(student);
					courseEntryDao.save(courseEntry);
				} else {
					String baseMsg = "Student %s cannot remove course %d from his academic record because it is already finished.";
					String message = String.format(baseMsg, username, courseEntryId);
					LOGGER.debug(message);
					throw new CannotPerformOperationException(message);
				}
			} else {
				String baseMsg = "Student %s does not have course %d in his academic record to be removed.";
				String message = String.format(baseMsg, username, courseEntryId);
				LOGGER.debug(message);
				throw new CannotPerformOperationException(message);
			}
		} else {
			noStudentFound(username);
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
	
	private void noStudentFound(final String username) {
		String baseMsg = "No student found with username: %s.";
		String message = String.format(baseMsg, username);
		LOGGER.debug(message);
		throw new CannotPerformOperationException(message);
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
