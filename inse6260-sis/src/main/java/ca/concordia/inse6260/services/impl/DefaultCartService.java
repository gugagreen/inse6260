package ca.concordia.inse6260.services.impl;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.concordia.inse6260.dao.CourseEntryDAO;
import ca.concordia.inse6260.dao.StudentDAO;
import ca.concordia.inse6260.entities.AcademicRecordEntry;
import ca.concordia.inse6260.entities.Course;
import ca.concordia.inse6260.entities.CourseDates;
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.enums.AcademicRecordStatus;
import ca.concordia.inse6260.entities.enums.Grade;
import ca.concordia.inse6260.exception.CannotPerformOperationException;
import ca.concordia.inse6260.services.CartService;

@Component
public class DefaultCartService implements CartService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCartService.class);
	private static final int MAX_COURSES_PER_SEASON = 2;

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
			if (hasCourse(records, courseEntryId) != null) {
				String baseMsg = "Student %s already has course %d in his academic record.";
				String message = String.format(baseMsg, username, courseEntryId);
				LOGGER.debug(message);
				throw new CannotPerformOperationException(message);
			} else if (hasTimeConflict(records, courseEntryId)) {
				String baseMsg = "Class was not added due to a time conflict please check to make sure you are registering for a future semester and that no other courses are at the same time.";
				String message = String.format(baseMsg, courseEntryId, username);
				LOGGER.debug(message);
				throw new CannotPerformOperationException(message);
			} else if (hasMaxCourses(records, courseEntryId)) {
				String baseMsg = "Student %s already has %d courses in this season.";
				String message = String.format(baseMsg, username, MAX_COURSES_PER_SEASON);
				LOGGER.debug(message);
				throw new CannotPerformOperationException(message);
			} else {
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
			}
		} else {
			noStudentFound(username);
		}
	}

	private boolean hasMaxCourses(final List<AcademicRecordEntry> records, final long courseEntryId) {
		CourseDates dates = courseEntryDao.findOne(courseEntryId).getDates();
		int currentOnSeason = 0;
		for (AcademicRecordEntry record : records) {
			CourseDates recordDates = record.getCourseEntry().getDates();
			// if same season and year, increment counter
			if (recordDates.getSeason().equals(dates.getSeason())
					&& recordDates.getStartDate().get(Calendar.YEAR) == dates.getStartDate().get(Calendar.YEAR)) {
				currentOnSeason++;
			}
		}
		return currentOnSeason >= MAX_COURSES_PER_SEASON;
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
		Course course = courseEntryDao.findOne(courseEntryId).getCourse();
		for (AcademicRecordEntry record : records) {
			if ((record.getCourseEntry() != null) && record.getCourseEntry().getCourse().equals(course)) {
				existentRecord = record;
				break;
			}
		}
		return existentRecord;
	}

	private boolean hasTimeConflict(final List<AcademicRecordEntry> records, final long courseEntryId) {
		boolean hasConflict = false;
		CourseDates courseDates = courseEntryDao.findOne(courseEntryId).getDates();

		for (AcademicRecordEntry record : records) {
			CourseDates registeredCourseDates = record.getCourseEntry().getDates();
			hasConflict = hasConflict(courseDates, registeredCourseDates);
			if (hasConflict) {
				break;
			}
		}
		return hasConflict;
	}

	private boolean hasConflict(CourseDates cd1, CourseDates cd2) {
		boolean hasConflict = true;

		// first check if there is no date conflict
		// no conflict if (Start1 > End2) or (End1 < Start2)
		if (cd1.getStartDate().after(cd2.getEndDate()) || cd1.getEndDate().before(cd2.getStartDate())) {
			hasConflict = false;
			// then check day conflict
		} else if (!matchingWeekdays(cd1.getWeekDays(), cd2.getWeekDays())) {
			hasConflict = false;
			// then check time conflict
		} else if (cd1.getStartTime().after(cd2.getEndTime()) || cd1.getEndTime().before(cd2.getStartTime())) {
			hasConflict = false;
		}

		return hasConflict;
	}

	private boolean matchingWeekdays(String weekDays, String otherDays) {
		boolean matching = false;
		String noDays = "-";
		char noDay = noDays.charAt(0);

		for (int i = 0; i < weekDays.length(); i++) {
			if ((weekDays.charAt(i) != (noDay)) && (otherDays.charAt(i) != (noDay))) {
				matching = true;
			}
		}
		return matching;
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
