package ca.concordia.inse6260.entities.dto;

import java.math.BigDecimal;
import java.util.List;

import ca.concordia.inse6260.entities.Payment;

/**
 * DTO for account balance.
 */
public class AccountBalance {

	private List<AccountDebtEntry> debts;
	private List<Payment> payments;
	private BigDecimal total;
	
	public List<AccountDebtEntry> getDebts() {
		return debts;
	}
	public void setDebts(List<AccountDebtEntry> debts) {
		this.debts = debts;
	}
	public List<Payment> getPayments() {
		return payments;
	}
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
