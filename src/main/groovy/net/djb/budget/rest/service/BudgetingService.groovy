package net.djb.budget.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.math.RoundingMode;

import net.djb.budget.rest.data.schema.RecurringTransfer;
import net.djb.budget.rest.data.schema.Transaction;
import net.djb.budget.rest.data.schema.Transfer;
import net.djb.budget.rest.data.repo.RecurringTransferRepository;
import net.djb.budget.rest.data.repo.TransferRepository;
import net.djb.budget.rest.exception.UnsupportedChronoUnitException;

@Service
class BudgetingService {

	static final Logger LOG = LoggerFactory.getLogger(BudgetingService.class);

	@Autowired RecurringTransferRepository recurringTransfers;
	@Autowired TransferRepository transfers;

	List<RecurringTransfer> listFixedExpenses(LocalDate effectiveDate = LocalDate.now()) {
		return recurringTransfers.findWhereAmountIsPositive(effectiveDate);
	}

	List<RecurringTransfer> listFixedIncome(LocalDate effectiveDate = LocalDate.now()) {
		return recurringTransfers.findWhereAmountIsNegative(effectiveDate);
	}

	Transaction buildBudget(long incomeId, LocalDate effectiveDate = LocalDate.now()){
		RecurringTransfer income = recurringTransfers.findOne(incomeId);
		buildTransaction(income, effectiveDate);
	}

	private buildTransaction(RecurringTransfer income, LocalDate effectiveDate) {
		Transaction transaction = new Transaction();
		transaction.description = income.description;
		transaction.time = LocalDateTime.now();

		transaction.transfers = [new Transfer(account: income.account, amount: income.amount)];

		List<RecurringTransfer> allIncome = listFixedIncome(effectiveDate);
		listFixedExpenses(effectiveDate).each{
			transaction.transfers << buildTransfer(income, it, allIncome, effectiveDate);
		};

		return transaction;
	}

	private Transfer buildTransfer(RecurringTransfer income, RecurringTransfer expense, List<RecurringTransfer> allIncome, LocalDate effectiveDate){
		Transfer transfer = new Transfer();
		transfer.account = expense.account;

		transfer.amount = round(calcDeduction(income, expense, allIncome, effectiveDate));

		return transfer;
	}

	private BigDecimal calcDeduction(RecurringTransfer income, RecurringTransfer expense, List<RecurringTransfer> allIncome, LocalDate effectiveDate){
		long currentBalance = transfers.calcBalance(expense.account);

		Long totalExpenses = null;
		Long totalIncome = null;
		if((effectiveDate + periodOf(expense)) < (effectiveDate + periodOf(income))){
			LocalDate endDate = effectiveDate.plus(periodOf(income).multipliedBy(4));

			totalExpenses = calcRecurrencesBetween(expense, effectiveDate, endDate) * expense.amount;
			totalIncome = allIncome.sum{
				calcRecurrencesBetween(it, effectiveDate, endDate) * it.amount;
			};
			BigDecimal ratio = income.amount / totalIncome;
			return (totalExpenses * ratio) - currentBalance;
		} else {
			LocalDate nextExpense = nextOccurrenceOf(expense, effectiveDate); 
			totalExpenses = expense.amount;
			totalIncome = allIncome.sum{
				calcRecurrencesBetween(it, effectiveDate, nextExpense) * it.amount;
			};
			BigDecimal ratio = income.amount / totalIncome;
			return (totalExpenses - currentBalance) * ratio;
		}
	}

	private LocalDate nextOccurrenceOf(RecurringTransfer t, LocalDate effectiveDate){
		LocalDate iDate = t.startDate;
		int n = 0;
		while(iDate < effectiveDate){
			iDate = t.startDate.plus(periodOf(t).multipliedBy(n++));
		}
		return iDate;
	}

	private int calcRecurrencesBetween(RecurringTransfer t, LocalDate start, LocalDate end){
		if(start == end){
			return 1;
		}
		LocalDate iDate = t.startDate;
		int occurrencesTotal = 0;
		int occurrencesBetween = 0;
		while(iDate < end){
			if(iDate >= start){
				occurrencesBetween++;
			}
			occurrencesTotal++;
			iDate = t.startDate.plus(periodOf(t).multipliedBy(occurrencesTotal));
		}
		return occurrencesBetween;
	}

	private BigDecimal round(BigDecimal bd, int digits = 0, RoundingMode roundingMode = RoundingMode.UP){
	    return bd.setScale(digits, roundingMode);
	}

	private Period periodOf(RecurringTransfer t){
		switch(t.unit){
		case ChronoUnit.DAYS:
			return Period.ofDays(t.quantity);
		case ChronoUnit.WEEKS:
			return Period.ofWeeks(t.quantity);
		case ChronoUnit.MONTHS:
			return Period.ofMonths(t.quantity);
		case ChronoUnit.YEARS:
			return Period.ofYears(t.quantity);
		default:
			throw new UnsupportedChronoUnitException(t.unit);
		}
	}
	
}
