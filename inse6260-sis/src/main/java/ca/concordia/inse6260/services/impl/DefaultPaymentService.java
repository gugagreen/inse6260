package ca.concordia.inse6260.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.concordia.inse6260.dao.StudentDAO;
import ca.concordia.inse6260.entities.AcademicRecordEntry;
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Payment;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.dto.AccountBalance;
import ca.concordia.inse6260.entities.dto.AccountDebtEntry;
import ca.concordia.inse6260.entities.enums.AcademicRecordStatus;
import ca.concordia.inse6260.entities.enums.StudentOrigin;
import ca.concordia.inse6260.exception.CannotPerformOperationException;
import ca.concordia.inse6260.services.PaymentService;

@Component
public class DefaultPaymentService implements PaymentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPaymentService.class);

	@Resource
	private StudentDAO studentDao;

	@Override
	public void addPayment(String studentId, BigDecimal value) {
		Student student = studentDao.findOne(studentId);
		if (student != null) {
			List<Payment> payments = student.getPayments();
			Payment pay = new Payment();
			pay.setDate(Calendar.getInstance());
			pay.setValue(value);
			payments.add(pay);

			studentDao.save(student);
		} else {
			noStudentFound(studentId);
		}

	}

	@Override
	public AccountBalance getBalance(String studentId) {
		AccountBalance balance = null;
		Student student = studentDao.findOne(studentId);
		if (student != null) {
			balance = new AccountBalance();
			List<AcademicRecordEntry> records = student.getAcademicRecords();
			List<AccountDebtEntry> debts = new ArrayList<>();
			for (AcademicRecordEntry record : records) {
				if (AcademicRecordStatus.FINISHED.equals(record.getStatus())
						|| AcademicRecordStatus.REGISTERED.equals(record.getStatus())) {
					AccountDebtEntry debt = new AccountDebtEntry();
					debt.setCourseEntry(record.getCourseEntry());
					debt.setValue(calculateDebt(student.getOrigin(), record.getCourseEntry()));
					debts.add(debt);
				}
			}
			balance.setDebts(debts);
			balance.setPayments(student.getPayments());
			balance.setTotal(calculateTotal(balance));
		} else {
			noStudentFound(studentId);
		}
		return balance;
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
}
