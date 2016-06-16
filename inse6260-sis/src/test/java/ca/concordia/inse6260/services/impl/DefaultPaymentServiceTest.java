package ca.concordia.inse6260.services.impl;

import java.math.BigDecimal;
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

import ca.concordia.inse6260.dao.StudentDAO;
import ca.concordia.inse6260.entities.AcademicRecordEntry;
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Payment;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.dto.AccountBalance;
import ca.concordia.inse6260.entities.enums.AcademicRecordStatus;
import ca.concordia.inse6260.entities.enums.StudentOrigin;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPaymentServiceTest {

	private DefaultPaymentService service;

	@Mock
	private StudentDAO studentDao;

	@Before
	public void setup() {
		service = new DefaultPaymentService();
		service.setStudentDao(studentDao);
	}

	@Test
	public void shouldCalculateBalanceWithoutWaitlist() {
		String studentId = "testStudent";
		double[] payments = new double[] { 500, 100 };
		double[] baseCosts = new double[] { 500, 500, 700, 700 };
		AcademicRecordStatus[] status = new AcademicRecordStatus[] { AcademicRecordStatus.FINISHED, AcademicRecordStatus.FINISHED,
				AcademicRecordStatus.WAIT_LIST, AcademicRecordStatus.REGISTERED };
		Student student = buildStudent(studentId, StudentOrigin.QUEBEC, payments, baseCosts, status);
		Mockito.when(studentDao.findOne(studentId)).thenReturn(student);

		AccountBalance balance = service.getBalance(studentId);
		Assert.assertNotNull(balance);
		Assert.assertNotNull(balance.getDebts());
		Assert.assertEquals(3, balance.getDebts().size());
		Assert.assertEquals(new BigDecimal(-1100), balance.getTotal());
	}

	@Test
	public void shouldCalculateBalancePerOrigin() {
		String studentId = "testStudent";
		double[] payments = new double[] { 500, 100 };
		double[] baseCosts = new double[] { 500, 700 };
		AcademicRecordStatus[] status = new AcademicRecordStatus[] { AcademicRecordStatus.FINISHED,
				AcademicRecordStatus.FINISHED };
		Student student = buildStudent(studentId, StudentOrigin.CANADA, payments, baseCosts, status);
		Mockito.when(studentDao.findOne(studentId)).thenReturn(student);

		AccountBalance balance = service.getBalance(studentId);
		Assert.assertNotNull(balance);
		// canadian pays 2x cost, so -500*2 -700*2 +500 +100 = 1800
		Assert.assertEquals(new BigDecimal(-1800), balance.getTotal());

	}

	private Student buildStudent(final String studentId, final StudentOrigin origin, final double[] payments,
			final double[] baseCosts, final AcademicRecordStatus[] status) {
		Student student = new Student();
		student.setUsername(studentId);
		student.setOrigin(origin);

		// payments
		List<Payment> pays = new ArrayList<>();
		for (int i = 0; i < payments.length; i++) {
			Payment pay = new Payment();
			pay.setDate(Calendar.getInstance());
			pay.setValue(new BigDecimal(payments[i]));
			pays.add(pay);
		}
		student.setPayments(pays);

		// academic records with base cost
		List<AcademicRecordEntry> academicRecords = new ArrayList<>();
		for (int i = 0; i < status.length; i++) {
			AcademicRecordEntry record = new AcademicRecordEntry();
			record.setStatus(status[i]);
			
			CourseEntry courseEntry = new CourseEntry();
			courseEntry.setBaseCost(new BigDecimal(baseCosts[i]));
			record.setCourseEntry(courseEntry);
			
			academicRecords.add(record);
		}
		student.setAcademicRecords(academicRecords);
		return student;
	}

}
