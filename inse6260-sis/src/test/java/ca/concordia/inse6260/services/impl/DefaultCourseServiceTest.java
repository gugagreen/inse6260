package ca.concordia.inse6260.services.impl;

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
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.dto.Transcript;
import ca.concordia.inse6260.entities.enums.AcademicRecordStatus;
import ca.concordia.inse6260.entities.enums.Grade;
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
		Mockito.when(dao.findBySeason(Mockito.any(Season.class), Mockito.any(Calendar.class))).thenReturn(mockedCourses);

		List<CourseEntry> courses = service.findBySeason(yearSeason);
		Assert.assertNotNull(courses);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotWorkWithInvalidSeason() {
		String yearSeason = "NOTASEASON2016";
		List<CourseEntry> mockedCourses = new ArrayList<>();
		Mockito.when(dao.findBySeason(Mockito.any(Season.class), Mockito.any(Calendar.class))).thenReturn(mockedCourses);

		service.findBySeason(yearSeason);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotWorkWithInvalidYear() {
		String yearSeason = "SUMMER----";
		List<CourseEntry> mockedCourses = new ArrayList<>();
		Mockito.when(dao.findBySeason(Mockito.any(Season.class), Mockito.any(Calendar.class))).thenReturn(mockedCourses);

		service.findBySeason(yearSeason);
	}

	@Test
	public void shouldHaveBGpa() {
		String studentId = "testStudent";
		Grade[] grades = new Grade[] { Grade.A, Grade.B_MINUS, Grade.NOT_SET, Grade.C_PLUS };
		AcademicRecordStatus[] status = new AcademicRecordStatus[] { AcademicRecordStatus.FINISHED, AcademicRecordStatus.FINISHED,
				AcademicRecordStatus.WAIT_LIST, AcademicRecordStatus.FINISHED };
		Student student = buildStudentWithRecords(studentId, grades, status);
		Mockito.when(studentDao.findOne(studentId)).thenReturn(student);

		Transcript transcript = service.getStudentTranscript(studentId);
		Assert.assertNotNull(transcript);
		Assert.assertEquals("3.34", transcript.getGpa());
	}
	
	@Test
	public void shouldOnlyHaveFinishedCoursesInTranscript() {
		String studentId = "testStudent";
		Grade[] grades = new Grade[] { Grade.A, Grade.B_MINUS, Grade.NOT_SET, Grade.C_PLUS };
		AcademicRecordStatus[] status = new AcademicRecordStatus[] { AcademicRecordStatus.FINISHED, AcademicRecordStatus.FINISHED,
				AcademicRecordStatus.WAIT_LIST, AcademicRecordStatus.FINISHED };
		Student student = buildStudentWithRecords(studentId, grades, status);
		Mockito.when(studentDao.findOne(studentId)).thenReturn(student);

		Transcript transcript = service.getStudentTranscript(studentId);
		Assert.assertNotNull(transcript);
		Assert.assertEquals(3, transcript.getAcademicRecords().size());
	}

	private Student buildStudentWithRecords(final String studentId, final Grade[] grades, final AcademicRecordStatus[] status) {
		Student student = new Student();
		student.setUsername(studentId);
		List<AcademicRecordEntry> academicRecords = new ArrayList<>();
		for (int i = 0; i < grades.length; i++) {
			AcademicRecordEntry record = new AcademicRecordEntry();
			record.setGrade(grades[i]);
			record.setStatus(status[i]);
			academicRecords.add(record);
		}
		student.setAcademicRecords(academicRecords);
		return student;
	}
}
