package ca.concordia.inse6260.services.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.StudentGrade;
import ca.concordia.inse6260.entities.User;
import ca.concordia.inse6260.entities.dto.Transcript;
import ca.concordia.inse6260.entities.enums.AcademicRecordStatus;
import ca.concordia.inse6260.entities.enums.Grade;
import ca.concordia.inse6260.entities.enums.Season;
import ca.concordia.inse6260.exception.CannotPerformOperationException;
import ca.concordia.inse6260.services.CourseService;

@Component
public class DefaultCourseService implements CourseService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCourseService.class);

	@Resource
	private CourseEntryDAO dao;
	@Resource
	private AcademicRecordEntryDAO recordDao;
	@Resource
	private StudentDAO studentDao;

	public Iterable<CourseEntry> findAll() {
		return dao.findAll();
	}

	@Override
	public List<CourseEntry> findBySeason(final String yearSeason) {
		LOGGER.debug("Find course by year season: {}", yearSeason);
		List<CourseEntry> courses = new ArrayList<CourseEntry>();
		validateYearSeason(yearSeason);
		Season season = Season.valueOf(yearSeason.substring(0, yearSeason.length() - 4));
		int year = Integer.parseInt(yearSeason.substring(yearSeason.length() - 4));
		
		Iterable<CourseEntry> allCourses = dao.findAll();
		for(CourseEntry course : allCourses) {
			if (course != null && course.getDates() != null) {
				if (season.equals(course.getDates().getSeason()) && course.getDates().getStartDate().get(Calendar.YEAR) == year) {
					courses.add(course);
				}
			}
		}
		return courses;
	}

	@Override
	public List<CourseEntry> findBySeasonProfessor(String yearSeason, String professorId) {
		LOGGER.debug("Find course by year season {} and professor {}", yearSeason, professorId);

		if (professorId == null || professorId.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid professorId: " + professorId);
		}

		List<CourseEntry> allCourses = findBySeason(yearSeason);
		List<CourseEntry> courses = new ArrayList<CourseEntry>();
		if (allCourses != null && !allCourses.isEmpty()) {
			for (CourseEntry entry : allCourses) {
				User prof = entry.getProfessor();
				if (prof != null && prof.getUsername() != null && professorId.equals(prof.getUsername())) {
					courses.add(entry);
				}
			}
		}
		return courses;
	}

	@Override
	public List<Student> getStudentsForCourse(Long courseEntryId) {
		List<Student> students = null;
		CourseEntry entry = dao.findOne(courseEntryId);
		if (entry != null) {
			students = entry.getStudents();
		}

		return students;
	}

	@Override
	public void updateGradesForCourse(long courseEntryId, StudentGrade[] studentGrades) {
		CourseEntry entry = dao.findOne(courseEntryId);
		if (studentGrades != null && entry != null) {
			for (StudentGrade sGrade : studentGrades) {
				if (sGrade != null && sGrade.getGrade() != null) {
					Student student = findStudentInCourse(entry, sGrade);
					AcademicRecordEntry record = findRecordInStudent(courseEntryId, student);
					// validate update before performing operation
					validateUpdateGrade(sGrade, record);
					// if there was no exception, perform update
					record.setGrade(Grade.valueOf(sGrade.getGrade()));
					record.setStatus(AcademicRecordStatus.FINISHED);
					recordDao.save(record);
				} else {
					throw new IllegalArgumentException("StudentGrade cannot be null!");
				}
			}
		} else {
			throw new IllegalArgumentException(
					String.format("Either courseEntryId [%d] or studentGrades [%s] is invalid", courseEntryId, studentGrades));
		}
	}

	private void validateUpdateGrade(final StudentGrade sGrade, final AcademicRecordEntry record) {
		if (record.getStatus() == AcademicRecordStatus.WAIT_LIST) {
			throw new CannotPerformOperationException("You cannot add grade to a record in wait list");
		}
		if (record.getStatus() == AcademicRecordStatus.DISC) {
			throw new CannotPerformOperationException("You cannot add grade to a record in disc");
		}
		if (Grade.valueOf(sGrade.getGrade()) == Grade.NOT_SET && record.getStatus() == AcademicRecordStatus.FINISHED) {
			throw new CannotPerformOperationException("You can only update grade to NOT_SET if course is not finished");
		}
		Calendar currentDate = Calendar.getInstance();
		Calendar endDate = record.getCourseEntry().getDates().getEndDate();
		if (endDate != null && currentDate.before(endDate)) {
			throw new CannotPerformOperationException(
					"You can only update grade after course end date [" + calendarToFormat(endDate) + "] has passed");
		}
	}

	private String calendarToFormat(Calendar cal) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(cal.getTime());
	}

	@Override
	public Transcript getStudentTranscript(String studentId) {
		Transcript transcript = null;
		if (studentId == null || studentId.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid studentId: " + studentId);
		}
		Student student = studentDao.findOne(studentId);
		if (student != null) {
			transcript = new Transcript();
			transcript.setStudentUsername(studentId);
			List<AcademicRecordEntry> entries = getTranscriptRecords(student);
			transcript.setAcademicRecords(entries);
			// only finished courses affect gpa (not registered or disc)
			List<AcademicRecordEntry> finishedEntries = getTranscriptRecordsFinished(student);
			String gpa = calculateGradePointAverage(finishedEntries);
			transcript.setGpa(gpa);
			List<String> termGPA = calculateTermGPA(finishedEntries);
			transcript.setTermGPA(termGPA);
		} else {
			String baseMsg = "No student found with username: %s.";
			String message = String.format(baseMsg, studentId);
			LOGGER.debug(message);
			throw new IllegalArgumentException(message);
		}
		return transcript;
	}

	private void validateYearSeason(final String yearSeason) {
		if ((yearSeason != null) && (yearSeason.length() > 4)) {
			// throws IllegalArgumentException if enum does not exist
			Season.valueOf(yearSeason.substring(0, yearSeason.length() - 4));
		} else {
			throw new IllegalArgumentException("Invalid yearSeason: " + yearSeason);
		}
	}

	private Student findStudentInCourse(final CourseEntry entry, final StudentGrade sGrade) {
		Student found = null;
		for (Student student : entry.getStudents()) {
			if (student.getUsername().equals(sGrade.getStudentUsername())) {
				found = student;
				break;
			}
		}
		if (found == null) {
			throw new IllegalArgumentException(
					String.format("Student %s not found for course entry %d.", sGrade.getStudentUsername(), entry.getId()));
		}
		return found;
	}

	private AcademicRecordEntry findRecordInStudent(long courseEntryId, Student student) {
		AcademicRecordEntry found = null;
		for (AcademicRecordEntry record : student.getAcademicRecords()) {
			if (record.getCourseEntry() != null && record.getCourseEntry().getId() == courseEntryId) {
				found = record;
				break;
			}
		}
		if (found == null) {
			throw new IllegalArgumentException(
					String.format("Student %s does not have record for course entry %d.", student.getUsername(), courseEntryId));
		}
		return found;
	}

	private List<AcademicRecordEntry> getTranscriptRecords(final Student student) {
		List<AcademicRecordEntry> entries = new ArrayList<>();

		for (AcademicRecordEntry entry : student.getAcademicRecords()) {
			if (!entry.getStatus().equals(AcademicRecordStatus.WAIT_LIST)) {
				entries.add(entry);
			}
		}

		return entries;
	}
	
	private List<AcademicRecordEntry> getTranscriptRecordsFinished(final Student student) {
		List<AcademicRecordEntry> entries = new ArrayList<>();

		for (AcademicRecordEntry entry : student.getAcademicRecords()) {
			if (entry.getStatus().equals(AcademicRecordStatus.FINISHED)) {
				entries.add(entry);
			}
		}

		return entries;
	}

	private String calculateGradePointAverage(List<AcademicRecordEntry> entries) {
		float total = 0;
		int totalCredits = 0;
		for (AcademicRecordEntry entry : entries) {
			int courseCredits = entry.getCourseEntry().getCourse().getCredits();
			totalCredits += courseCredits;
			total += entry.getGrade().getGPAPoint() * courseCredits;
		}

		float cumGPA = total / totalCredits;
		String pattern = "#.###";
		DecimalFormat myFormatter = new DecimalFormat(pattern);
		String output = myFormatter.format(cumGPA);

		return output;
	}

	private List<String> calculateTermGPA(List<AcademicRecordEntry> entries) {
		List<String> output = new ArrayList<String>();
		List<String> seasonYear = new ArrayList<String>();
		List<Integer> seasonCredits = new ArrayList<Integer>();
		List<Float> seasonGPA = new ArrayList<Float>();

		for (AcademicRecordEntry entry : entries) {
			int currentCredits = 0;
			float currentGPA = 0;
			String courseSeasonYear = entry.getCourseEntry().getDates().getSeason().toString() + "/"
					+ entry.getCourseEntry().getDates().getStartDate().get(Calendar.YEAR);

			if (seasonYear.contains(courseSeasonYear)) {
				int index = seasonYear.indexOf(courseSeasonYear);
				currentCredits = seasonCredits.get(index);
				int courseCredits = entry.getCourseEntry().getCourse().getCredits();
				currentCredits += courseCredits;
				seasonCredits.set(index, currentCredits);

				currentGPA = seasonGPA.get(index);
				currentGPA += entry.getGrade().getGPAPoint() * courseCredits;
				seasonGPA.set(index, currentGPA);
			} else {
				seasonYear.add(courseSeasonYear);
				int courseCredits = entry.getCourseEntry().getCourse().getCredits();
				seasonCredits.add(courseCredits);
				seasonGPA.add(entry.getGrade().getGPAPoint() * courseCredits);

			}
		}
		for (String temp : seasonYear) {
			int tempIndex = seasonYear.indexOf(temp);
			float GPA = seasonGPA.get(tempIndex) / seasonCredits.get(tempIndex);
			String pattern = "#.###";
			DecimalFormat myFormatter = new DecimalFormat(pattern);
			String stringGpa = myFormatter.format(GPA);

			output.add(temp + "	GPA:" + stringGpa);
		}

		return output;
	}

	public CourseEntryDAO getDao() {
		return dao;
	}

	public void setDao(CourseEntryDAO dao) {
		this.dao = dao;
	}

	public AcademicRecordEntryDAO getRecordDao() {
		return recordDao;
	}

	public void setRecordDao(AcademicRecordEntryDAO recordDao) {
		this.recordDao = recordDao;
	}

	public StudentDAO getStudentDao() {
		return studentDao;
	}

	public void setStudentDao(StudentDAO studentDao) {
		this.studentDao = studentDao;
	}

}
