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
import java.time.temporal.UnsupportedTemporalTypeException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import net.djb.budget.rest.data.repo.RecurringTransferRepository;
import net.djb.budget.rest.data.schema.RecurringTransfer;
import net.djb.budget.rest.data.schema.Transaction;
import net.djb.budget.rest.data.schema.Transfer;
import net.djb.budget.rest.service.BalanceService;

@Service
class BudgetingService {

	static final Logger LOG = LoggerFactory.getLogger(BudgetingService.class);

	@Autowired RecurringTransferRepository recurringTransfers;
	@Autowired BalanceService balances;

	List<RecurringTransfer> listFixedExpenses(LocalDate effectiveDate) {
		return recurringTransfers.findWhereAmountIsPositive(effectiveDate);
	}

	List<RecurringTransfer> listFixedIncome(LocalDate effectiveDate) {
		return recurringTransfers.findWhereAmountIsNegative(effectiveDate);
	}

	Transaction buildBudget(long incomeId, LocalDate effectiveDate){
		Optional<RecurringTransfer> income = recurringTransfers.findById(incomeId);
		if(income.isPresent()){
			return buildTransaction(income.get(), effectiveDate);
		} else {
			throw new IllegalArgumentException("No recurring transfer found with ID " + incomeId);
		}
	}

	private Transaction buildTransaction(RecurringTransfer income, LocalDate effectiveDate) {
		Transaction transaction = new Transaction();
		transaction.description = income.description;
		transaction.time = effectiveDate.atStartOfDay();

		transaction.transfers = [new Transfer(account: income.account, amount: income.amount)];

		List<RecurringTransfer> allIncome = listFixedIncome(effectiveDate);
		listFixedExpenses(effectiveDate).each{ expense ->
			transaction.transfers << buildTransfer(income, expense, allIncome);
		};

		return transaction;
	}

	private Transfer buildTransfer(RecurringTransfer income, RecurringTransfer expense, List<RecurringTransfer> allIncome){
		Transfer transfer = new Transfer();
		transfer.account = expense.account;

		transfer.amount = calcDeduction(income, expense, allIncome);

		return transfer;
	}

	private long calcDeduction(RecurringTransfer income, RecurringTransfer expense, List<RecurringTransfer> allIncome){
		long annualIncome = annualize(income).amount;
		long annualTotalIncome = allIncome.collect { annualize(it).amount }.sum();

		BigDecimal incomeRatio = annualIncome / annualTotalIncome;

		long annualExpense = annualize(expense).amount;

		BigDecimal annualContribution = annualExpense * incomeRatio;
		
		BigDecimal incomeAnnualizationRatio = income.amount / annualIncome;

		LOG.debug("Deduction for '${income.description}' is ((${annualIncome} income / ${annualTotalIncome} total income) * ${annualExpense} expense) * ${incomeAnnualizationRatio} annual frequency");
		return round(annualContribution * incomeAnnualizationRatio).longValueExact();
	}

	private RecurringTransfer annualize(RecurringTransfer t){
		int quantity = t.quantity;
		int frequency = t.frequency;
		ChronoUnit unit = t.unit;
		long amount = t.amount;

		int ratio;
		switch(t.unit){
		case ChronoUnit.DAYS:
			ratio = 365;
			break;
		case ChronoUnit.WEEKS:
			ratio = 52;
			break;
		case ChronoUnit.MONTHS:
			ratio = 12;
			break;
		case ChronoUnit.YEARS:
			ratio = 1;
			break;
		}
		long annualAmount = amount * ratio * (frequency / quantity);

		RecurringTransfer annualized = new RecurringTransfer();
		annualized.description = t.description;
		annualized.account = t.account;
		annualized.quantity = 1;
		annualized.frequency = 1;
		annualized.unit = ChronoUnit.YEARS;
		annualized.amount = annualAmount;
		return annualized;
	}

	public Transaction[] timeline(
			List<RecurringTransfer> recurring,
			LocalDate start = LocalDate.now(),
			LocalDate end = LocalDate.now().plusYears(1)){
		Transaction[] transactions = [];

		for(RecurringTransfer t : recurring){
			List<LocalDate> theseRecurrences = calcRecurrencesBetween(t, start, end);
			transactions += theseRecurrences.collect {
				if(t.amount > 0){
					return new Transaction(
						description: t.description,
						time: it.atStartOfDay(),
						transfers: [
							new Transfer(account: t.account, amount: t.amount * -1),
							new Transfer(account: "expenses", amount: t.amount)
						]
					)
				} else {
					return buildTransaction(t, it);
				}
			}

		}

		return transactions.toSorted();
	}

	/**
	 * Calculates occurrances of <code>t</code> between <code>start</code> (inclusive) and <code>end</code> (exclusive).
	 */
	private List<LocalDate> calcRecurrencesBetween(RecurringTransfer t, LocalDate start, LocalDate end){
		List<LocalDate> occurrencesBetween = [];
		int occurrencesTotal = 0;
		LocalDate iDate = t.startDate;
		while(iDate < end){
			if(iDate >= start){
				occurrencesBetween << iDate;
			}
			/* We can't just increment iDate by periodOf(t) because:
			 *   01/30 + 1 Month = 02/28
			 *   02/28 + 1 Month = 03/28
			 * BUT
			 *   01/30 + 2 Months = 03/30
			 */
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
			throw new UnsupportedTemporalTypeException("${t.unit} is not a supported ChronoUnit for RecurringTransfer Periods. Supported units are DAYS, WEEKS, MONTHS, & YEARS.");
		}
	}

}
