package ca.concordia.inse6260.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.concordia.inse6260.dao.AcademicRecordEntryDAO;
import ca.concordia.inse6260.dao.CourseEntryDAO;
import ca.concordia.inse6260.dao.StudentDAO;
import ca.concordia.inse6260.entities.AcademicRecordEntry;
import ca.concordia.inse6260.entities.Course;
import ca.concordia.inse6260.entities.CourseDates;
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Payment;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.dto.AccountBalance;
import ca.concordia.inse6260.entities.dto.AccountDebtEntry;
import ca.concordia.inse6260.entities.enums.AcademicRecordStatus;
import ca.concordia.inse6260.entities.enums.Grade;
import ca.concordia.inse6260.entities.enums.StudentOrigin;
import ca.concordia.inse6260.exception.CannotPerformOperationException;
import ca.concordia.inse6260.services.CartService;

@Component
public class DefaultCartService implements CartService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCartService.class);
	private static final int MAX_COURSES_PER_SEASON = 4;

	@Resource
	private StudentDAO studentDao;
	@Resource
	private CourseEntryDAO courseEntryDao;
	@Resource
	private AcademicRecordEntryDAO recordDao;

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
			CourseEntry courseEntry = courseEntryDao.findOne(courseEntryId);
			if (courseEntry != null) {
				final List<AcademicRecordEntry> records = student.getAcademicRecords();
				if (hasCourse(records, courseEntryId)) {
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
				} else if (hasStartDatePassed(courseEntry)) {
					String baseMsg = "Student %s cannot add course entry %d because it has already started.";
					String message = String.format(baseMsg, username, courseEntryId);
					LOGGER.debug(message);
					throw new CannotPerformOperationException(message);
				} else if(hasBalanceOwing(student)){
					String baseMsg = "Student %s cannot add course entry %d because there is a balance owing for a completed semseter.";
					String message = String.format(baseMsg, username, courseEntryId);
					LOGGER.debug(message);
					throw new CannotPerformOperationException(message);
				} else if (doesntHavePrereq(student, courseEntry)){
					String baseMsg = "Student %s cannot add course entry %d because there is a missing PreRequisite.";
					String message = String.format(baseMsg, username, courseEntryId);
					LOGGER.debug(message);
					throw new CannotPerformOperationException(message);
				}else {
					//Last error check for failed grades (F or two C's)
					checkForFailedGrades(student, username, courseEntryId);
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
				noCourseEntryFound(courseEntryId);
			}
		} else {
			noStudentFound(username);
		}
	}
	
	private boolean doesntHavePrereq(Student student, CourseEntry courseEntry){
		boolean invalidPrereq = false;
		Course prereq1 = null;
		Course prereq2 = null;
		for(CourseEntry entry :courseEntryDao.findAll()){
			if(courseEntry.getPrereq() == entry.getCourse().getId()){
				prereq1 = entry.getCourse();
				break;
			}
			if(courseEntry.getPrereq2() == entry.getCourse().getId()){
				prereq2 = entry.getCourse();
				break;
			}
		}
		if (student != null){
			List<AcademicRecordEntry> records = student.getAcademicRecords();
			if(prereq1 != null){
				invalidPrereq = true;
				for (AcademicRecordEntry record : records) {
					if(record.getCourseEntry().getCourse().equals(prereq1)){
						if(!(record.getGrade().equals(Grade.F) || record.getGrade().equals(Grade.INCOMPLETE))&& AcademicRecordStatus.FINISHED.equals(record.getStatus())){
							invalidPrereq = false;
						}
						break;
					}else{
						invalidPrereq = true;
					}	
				}
			}
			if(prereq2 != null){
				invalidPrereq = true;
				for (AcademicRecordEntry record : records) {
					if(record.getCourseEntry().getCourse().equals(prereq2)){
						if(!(record.getGrade().equals(Grade.F) || record.getGrade().equals(Grade.INCOMPLETE)) && AcademicRecordStatus.FINISHED.equals(record.getStatus())){
							invalidPrereq = false;
						}
						break;
					}else{
						invalidPrereq = true;
					}	
				}
			}
		}
		return invalidPrereq;
	}
	
	private void checkForFailedGrades(Student student, final String username, final long courseEntryId){
		int cCount = 0;
		List<AcademicRecordEntry> records = student.getAcademicRecords();
		for(AcademicRecordEntry record : records){
			if(record.getGrade().equals(Grade.F)){
				String baseMsg = "Student %s cannot add course entry %d because he has an F Grade on record";
				String message = String.format(baseMsg, username, courseEntryId);
				throw new CannotPerformOperationException(message);
			}
			if(record.getGrade().equals(Grade.C)||record.getGrade().equals(Grade.C_MINUS)||record.getGrade().equals(Grade.C_PLUS)){
				cCount +=1;
				if(cCount > 1){
					String baseMsg = "Student %s cannot add course entry %d because he has two or more C Grades";
					String message = String.format(baseMsg, username, courseEntryId);
					throw new CannotPerformOperationException(message);
				}
			}
		}
	}

	private boolean hasBalanceOwing(Student student) {
		boolean owes = false;
		if (student != null) {
			AccountBalance balance = new AccountBalance();
			List<AcademicRecordEntry> records = student.getAcademicRecords();
			List<AccountDebtEntry> debts = new ArrayList<>();
			for (AcademicRecordEntry record : records) {
				// get balance for finished/disc status, except wait list and registered
				boolean balanceFinished = (AcademicRecordStatus.FINISHED.equals(record.getStatus()) || AcademicRecordStatus.DISC.equals(record.getStatus()));
				if (balanceFinished) {
					AccountDebtEntry debt = new AccountDebtEntry();
					debt.setCourseEntry(record.getCourseEntry());
					debt.setValue(calculateDebt(student.getOrigin(), record.getCourseEntry()));
					debts.add(debt);
				}
			}
			balance.setDebts(debts);
			balance.setPayments(student.getPayments());
			balance.setTotal(calculateTotal(balance));
			BigDecimal zero = new BigDecimal("0"); 
			if (balance.getTotal().compareTo(zero) == -1){
				owes = true;
			}
		}
		
		return owes;
	}
	
	private BigDecimal calculateTotal(final AccountBalance balance) {
		BigDecimal total = new BigDecimal(0);
		for (AccountDebtEntry debt : balance.getDebts()) {
			total = total.subtract(debt.getValue());
		}
		for (Payment pay : balance.getPayments()) {
			total = total.add(pay.getValue());
		}
		return total;
	}

	private BigDecimal calculateDebt(final StudentOrigin origin, final CourseEntry courseEntry) {
		BigDecimal debt = null;
		if (courseEntry != null && courseEntry.getBaseCost() != null) {
			BigDecimal baseCost = courseEntry.getBaseCost();
			switch (origin) {
			case QUEBEC:
				debt = baseCost.multiply(new BigDecimal(1));
				break;
			case CANADA:
				debt = baseCost.multiply(new BigDecimal(2));
				break;
			case INTERNATIONAL:
				debt = baseCost.multiply(new BigDecimal(5));
				break;
			default:
				throw new IllegalArgumentException("Invalid student origin");
			}
		}
		return debt;
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
			AcademicRecordEntry existentRecord = hasCourseEntry(records, courseEntryId);
			if (existentRecord != null) {
				CourseEntry courseEntry = existentRecord.getCourseEntry();
				// check if course is not finished
				if ((!AcademicRecordStatus.FINISHED.equals(existentRecord.getStatus())) && !hasEndDatePassed(courseEntry)) {
					// check if disc date has passed, don't remove from record, just change status
					if (hasDiscDatePassed(courseEntry)) {
						LOGGER.debug("Moving course entry {} for student {} to disc.", courseEntryId, username);
						existentRecord.setStatus(AcademicRecordStatus.DISC);
						studentDao.save(student);
					} else {
						LOGGER.debug("Deleting course entry {} from student {} records.", courseEntryId, username);
						records.remove(existentRecord);
						studentDao.save(student);
					}

					// also remove student from course entry list
					courseEntry.getStudents().remove(student);
					courseEntryDao.save(courseEntry);

					// then add next wait list student to class
					moveNextWaitListStudentToRegistered(courseEntry);
				} else {
					String baseMsg = "Student %s cannot remove course entry %d from his academic record because it is already finished or end date passed.";
					String message = String.format(baseMsg, username, courseEntryId);
					LOGGER.debug(message);
					throw new CannotPerformOperationException(message);
				}
			} else {
				String baseMsg = "Student %s does not have course entry %d in his academic record to be removed.";
				String message = String.format(baseMsg, username, courseEntryId);
				LOGGER.debug(message);
				throw new CannotPerformOperationException(message);
			}
		} else {
			noStudentFound(username);
		}
	}

	private boolean hasCourse(final List<AcademicRecordEntry> records, final long courseEntryId) {
		boolean existentRecord = false;
		Course course = courseEntryDao.findOne(courseEntryId).getCourse();
		for (AcademicRecordEntry record : records) {
			if ((record.getCourseEntry() != null) && record.getCourseEntry().getCourse().equals(course) && !record.getGrade().equals(Grade.INCOMPLETE)) {
				existentRecord = true;
				break;
			}
		}
		return existentRecord;
	}

	private AcademicRecordEntry hasCourseEntry(final List<AcademicRecordEntry> records, final long courseEntryId) {
		AcademicRecordEntry existentRecord = null;
		for (AcademicRecordEntry record : records) {
			if ((record.getCourseEntry() != null) && (record.getCourseEntry().getId() == courseEntryId)) {
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

	private boolean hasDiscDatePassed(CourseEntry courseEntry) {
		Calendar disc = courseEntry.getDates().getDiscDate();
		return hasDatePassed(disc);
	}

	private boolean hasEndDatePassed(CourseEntry courseEntry) {
		Calendar end = courseEntry.getDates().getEndDate();
		return hasDatePassed(end);
	}

	private boolean hasStartDatePassed(CourseEntry courseEntry) {
		Calendar start = courseEntry.getDates().getStartDate();
		return hasDatePassed(start);
	}

	private boolean hasDatePassed(final Calendar baseDate) {
		return Calendar.getInstance().after(baseDate);
	}

	private void moveNextWaitListStudentToRegistered(final CourseEntry entry) {
		Iterable<AcademicRecordEntry> recordsIter = recordDao.findAll();
		AcademicRecordEntry firstWaitList = null;

		for (AcademicRecordEntry iter : recordsIter) {
			// check if course entry is the same, and if course entry is wait_list
			if (iter.getCourseEntry().getId() == entry.getId() && AcademicRecordStatus.WAIT_LIST.equals(iter.getStatus())) {
				// if iter record is before current firstWaitList, set firstWaitList to iter
				if (firstWaitList == null || firstWaitList.getId() > iter.getId()) {
					firstWaitList = iter;
				}
			}
		}

		if (firstWaitList != null) {
			firstWaitList.setStatus(AcademicRecordStatus.REGISTERED);
			recordDao.save(firstWaitList);
		}

	}

	private void noStudentFound(final String username) {
		String baseMsg = "No student found with username: %s.";
		String message = String.format(baseMsg, username);
		LOGGER.debug(message);
		throw new CannotPerformOperationException(message);
	}

	private void noCourseEntryFound(final long id) {
		String baseMsg = "No course entry found with id: %d.";
		String message = String.format(baseMsg, id);
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

	public AcademicRecordEntryDAO getRecordDao() {
		return recordDao;
	}

	public void setRecordDao(AcademicRecordEntryDAO recordDao) {
		this.recordDao = recordDao;
	}

}
