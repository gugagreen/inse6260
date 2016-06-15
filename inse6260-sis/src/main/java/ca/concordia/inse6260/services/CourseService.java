package ca.concordia.inse6260.services;

import java.util.List;

import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Season;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.StudentGrade;
import ca.concordia.inse6260.entities.Transcript;

public interface CourseService {

	Iterable<CourseEntry> findAll();

	/**
	 * Find course entry by string with year and season.
	 * 
	 * @param yearSeason
	 *            The search string in the format "{season}{year}" where {season} is any value in {@link Season} and {year} is a 4
	 *            digits number (e.g. "SUMMER2016")
	 * @return The list of matching CourseEntry.
	 * @throws IllegalArgumentException
	 *             if String is in wrong format.
	 */
	List<CourseEntry> findBySeason(final String yearSeason);
	
	/**
	 * Find course entry for a given professor by string with year and season.
	 * @param yearSeason The search string in the format "{season}{year}" where {season} is any value in {@link Season} and {year} is a 4
	 *            digits number (e.g. "SUMMER2016")
	 * @param professorId	The professor teaching the class.
	 * @return The list of matching CourseEntry.
	 * @throws IllegalArgumentException
	 *             if String is in wrong format.
	 */
	List<CourseEntry> findBySeasonProfessor(final String yearSeason, final String professorId);
	
	/**
	 * Find students for a course entry.
	 * @param courseEntryId	The course entry id.
	 * @return	List of students subscribed to course.
	 */
	List<Student> getStudentsForCourse(final Long courseEntryId);
	
	/**
	 * Update grades of students in a given course entry.
	 * @param courseEntryId	The course entry id.
	 * @param studentGrades	Array of students with their grades.
	 */
	void updateGradesForCourse(final long courseEntryId, final StudentGrade[] studentGrades);
	
	/**
	 * Get transcript for a given student.
	 * @param studentId
	 * @return The transcript.
	 */
	Transcript getStudentTranscript(final String studentId);
}
