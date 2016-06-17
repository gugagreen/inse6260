package ca.concordia.inse6260.services.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ca.concordia.inse6260.dao.CourseEntryDAO;
import ca.concordia.inse6260.dao.StudentDAO;
import ca.concordia.inse6260.entities.AcademicRecordEntry;
import ca.concordia.inse6260.entities.Course;
import ca.concordia.inse6260.entities.CourseDates;
//import ca.concordia.inse6260.entities.AcademicRecordEntry;
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.dto.Transcript;
import ca.concordia.inse6260.entities.enums.AcademicRecordStatus;
import ca.concordia.inse6260.entities.enums.Grade;
//import ca.concordia.inse6260.entities.Student;
//import ca.concordia.inse6260.entities.dto.Transcript;
//import ca.concordia.inse6260.entities.enums.AcademicRecordStatus;
//import ca.concordia.inse6260.entities.enums.Grade;
import ca.concordia.inse6260.entities.enums.Season;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCourseServiceTest {

	private DefaultCourseService service;

	@Mock
	private CourseEntryDAO dao;

	@Mock
	private StudentDAO studentDao;

	@Before
	public void setup() {
		service = new DefaultCourseService();
		service.setDao(dao);
		service.setStudentDao(studentDao);
	}

	@Test
	public void shouldFindSeason() {
		String yearSeason = "SUMMER2016";
		List<CourseEntry> mockedCourses = new ArrayList<>();
		Mockito.when(dao.findAll()).thenReturn(mockedCourses);

		List<CourseEntry> courses = service.findBySeason(yearSeason);
		Assert.assertNotNull(courses);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotWorkWithInvalidSeason() {
		String yearSeason = "NOTASEASON2016";
		List<CourseEntry> mockedCourses = new ArrayList<>();
		Mockito.when(dao.findAll()).thenReturn(mockedCourses);

		service.findBySeason(yearSeason);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotWorkWithInvalidYear() {
		String yearSeason = "SUMMER----";
		List<CourseEntry> mockedCourses = new ArrayList<>();
		Mockito.when(dao.findAll()).thenReturn(mockedCourses);

		service.findBySeason(yearSeason);
	}

	@Test
	public void shouldHaveBGpa() {
		String studentId = "testStudent";
		Grade[] grades = new Grade[] { Grade.A, Grade.B_MINUS, Grade.NOT_SET, Grade.C_PLUS };
		CourseEntry entry = buildCourseEntry(4, Season.SUMMER, string2Calendar("16.05.2016"));
		CourseEntry[] courseEntries = new CourseEntry[] { entry, entry, entry, entry };
		AcademicRecordStatus[] status = new AcademicRecordStatus[] { AcademicRecordStatus.FINISHED, AcademicRecordStatus.FINISHED,
				AcademicRecordStatus.WAIT_LIST, AcademicRecordStatus.FINISHED };
		Student student = buildStudentWithRecords(studentId, grades, courseEntries, status);
		Mockito.when(studentDao.findOne(studentId)).thenReturn(student);

		Transcript transcript = service.getStudentTranscript(studentId);
		Assert.assertNotNull(transcript);
		// GPA should be => A(4.0) + B-(2.7) + C+(2.3) = 9.0/3 = 3.0
		Assert.assertEquals(new BigDecimal("3.00").stripTrailingZeros(),
				new BigDecimal(transcript.getGpa()).stripTrailingZeros());
	}

	@Test
	public void shouldOnlyHaveFinishedCoursesInTranscript() {
		String studentId = "testStudent";
		Grade[] grades = new Grade[] { Grade.A, Grade.B_MINUS, Grade.NOT_SET, Grade.C_PLUS };
		CourseEntry entry = buildCourseEntry(4, Season.SUMMER, string2Calendar("16.05.2016"));
		CourseEntry[] courseEntries = new CourseEntry[] { entry, entry, entry, entry };
		AcademicRecordStatus[] status = new AcademicRecordStatus[] { AcademicRecordStatus.FINISHED, AcademicRecordStatus.FINISHED,
				AcademicRecordStatus.WAIT_LIST, AcademicRecordStatus.FINISHED };
		Student student = buildStudentWithRecords(studentId, grades, courseEntries, status);
		Mockito.when(studentDao.findOne(studentId)).thenReturn(student);

		Transcript transcript = service.getStudentTranscript(studentId);
		Assert.assertNotNull(transcript);
		Assert.assertEquals(3, transcript.getAcademicRecords().size());
	}

	private Student buildStudentWithRecords(final String studentId, final Grade[] grades, CourseEntry[] courseEntries,
			final AcademicRecordStatus[] status) {
		Student student = new Student();
		student.setUsername(studentId);
		List<AcademicRecordEntry> academicRecords = new ArrayList<>();
		for (int i = 0; i < grades.length; i++) {
			AcademicRecordEntry record = new AcademicRecordEntry();
			record.setGrade(grades[i]);
			record.setStatus(status[i]);
			record.setCourseEntry(courseEntries[i]);
			academicRecords.add(record);
		}
		student.setAcademicRecords(academicRecords);
		return student;
	}

	private CourseEntry buildCourseEntry(int credits, final Season season, final Calendar startDate) {
		Course course = new Course();
		course.setCredits(credits);

		CourseDates dates = new CourseDates();
		dates.setSeason(season);
		dates.setStartDate(startDate);

		CourseEntry courseEntry = new CourseEntry();
		courseEntry.setCourse(course);
		courseEntry.setDates(dates);
		return courseEntry;
	}

	private Calendar string2Calendar(final String dateString) {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(df.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal;
	}
}
