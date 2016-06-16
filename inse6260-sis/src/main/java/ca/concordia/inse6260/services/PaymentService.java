package ca.concordia.inse6260.services;

import java.math.BigDecimal;

import ca.concordia.inse6260.entities.dto.AccountBalance;

public interface PaymentService {

	/**
	 * Add a payment for a student.
	 * @param studentId
	 * @param value
	 */
	void addPayment(final String studentId, final BigDecimal value);
	
	/**
	 * Get account balance for a student.
	 * @param studentId
	 * @return
	 */
	AccountBalance getBalance(final String studentId);
}
