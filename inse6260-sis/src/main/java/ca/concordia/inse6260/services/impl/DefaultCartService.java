package ca.concordia.inse6260.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import ca.concordia.inse6260.dao.StudentDAO;
import ca.concordia.inse6260.entities.AcademicRecordEntry;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.services.CartService;

@Component
public class DefaultCartService implements CartService {
	
	@Resource
	private StudentDAO dao;

	@Override
	public List<AcademicRecordEntry> findCartByStudent(String username) {
		List<AcademicRecordEntry> records = null;
		Student student = dao.findOne(username);
		if (student != null) {
			records = student.getAcademicRecords();
		}
		return records;
	}

}
