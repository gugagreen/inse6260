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
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Season;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCourseServiceTest {
	
	private DefaultCourseService service;
	
	@Mock
	private CourseEntryDAO dao;
	
	@Before
	public void setup() {
		service = new DefaultCourseService();
		service.setDao(dao);
	}

	@Test
	public void shouldFindSeason() {
		String yearSeason = "SUMMER2016";
		List<CourseEntry> mockedCourses = new ArrayList<>();
		Mockito.when(dao.findBySeason(Mockito.any(Season.class), Mockito.any(Calendar.class))).thenReturn(mockedCourses);
		
		List<CourseEntry> courses = service.findBySeason(yearSeason);
		Assert.assertNotNull(courses);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotWorkWithInvalidSeason() {
		String yearSeason = "NOTASEASON2016";
		List<CourseEntry> mockedCourses = new ArrayList<>();
		Mockito.when(dao.findBySeason(Mockito.any(Season.class), Mockito.any(Calendar.class))).thenReturn(mockedCourses);
		
		service.findBySeason(yearSeason);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotWorkWithInvalidYear() {
		String yearSeason = "SUMMER----";
		List<CourseEntry> mockedCourses = new ArrayList<>();
		Mockito.when(dao.findBySeason(Mockito.any(Season.class), Mockito.any(Calendar.class))).thenReturn(mockedCourses);
		
		service.findBySeason(yearSeason);
	}
}
