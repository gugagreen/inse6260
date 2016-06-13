package ca.concordia.inse6260.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.concordia.inse6260.dao.AcademicRecordEntryDAO;
import ca.concordia.inse6260.dao.CourseEntryDAO;
import ca.concordia.inse6260.entities.AcademicRecordEntry;
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Season;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.StudentGrade;
import ca.concordia.inse6260.entities.User;
import ca.concordia.inse6260.services.CourseService;

@Component
public class DefaultCourseService implements CourseService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCourseService.class);

	@Resource
	private CourseEntryDAO dao;
	@Resource
	private AcademicRecordEntryDAO recordDao;

	public Iterable<CourseEntry> findAll() {
		return dao.findAll();
	}

	@Override
	public List<CourseEntry> findBySeason(final String yearSeason) {
		LOGGER.debug("Find course by year season: {}", yearSeason);
		List<CourseEntry> courses = new ArrayList<CourseEntry>();
		validateYearSeason(yearSeason);
		Season season = Season.valueOf(yearSeason.substring(0, yearSeason.length() - 4));
		String year = yearSeason.substring(yearSeason.length() - 4);
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), 0, 1, 0, 0, 0);
		courses = dao.findBySeason(season, cal);
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
				if (sGrade != null) {
					Student student = findStudentInCourse(entry, sGrade);
					AcademicRecordEntry record = findRecordInStudent(courseEntryId, student);
					record.setGrade(sGrade.getGrade());
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
			throw new IllegalArgumentException(String.format("Student %s not found for course entry %d.",
					sGrade.getStudentUsername(), entry.getId()));
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
			throw new IllegalArgumentException(String.format("Student %s does not have record for course entry %d.",
					student.getUsername(), courseEntryId));
		}
		return found;
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

}
