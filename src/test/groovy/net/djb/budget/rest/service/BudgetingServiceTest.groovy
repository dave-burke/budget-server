package net.djb.budget.rest.service;

import spock.lang.Specification;
import spock.lang.Unroll;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import net.djb.budget.rest.data.schema.*;
import net.djb.budget.rest.data.repo.*;
import net.djb.budget.rest.service.*;

class BudgetingServiceTest extends Specification {

	BudgetingService service = new BudgetingService();
	RecurringTransferRepository recurringTransfers;
	BalanceService balances;

	static final RecurringTransfer SEMI_WEEKLY_PAYCHECK = new RecurringTransfer(description: "every two weeks", frequency: 1, quantity: 2, unit: ChronoUnit.WEEKS,
			amount: -1000, account: "income/paychecks/one", startDate: LocalDate.of(2015,1,2));
	static final RecurringTransfer BI_MONTHLY_PAYCHECK = new RecurringTransfer(description: "twice a month", frequency: 2, quantity: 1, unit: ChronoUnit.MONTHS,
			amount: -1000, account: "income/paychecks/two", startDate: LocalDate.of(2015,1,15));
	static final RecurringTransfer BI_MONTHLY_PAYCHECK_1 = new RecurringTransfer(description: "15th", frequency: 1, quantity: 1, unit: ChronoUnit.MONTHS,
			amount: -1000, account: "income/paychecks/two", startDate: LocalDate.of(2015,1,15));
	static final RecurringTransfer BI_MONTHLY_PAYCHECK_2 = new RecurringTransfer(description: "30th", frequency: 1, quantity: 1, unit: ChronoUnit.MONTHS,
			amount: -1000, account: "income/paychecks/two", startDate: LocalDate.of(2015,1,30));

	static final RecurringTransfer MONTHLY_BILL = new RecurringTransfer(description: "monthly bill", frequency: 1, quantity: 1, unit: ChronoUnit.MONTHS,
			amount: 500, account: "checking/bills/monthly", startDate: LocalDate.of(2015,1,1));
	static final RecurringTransfer WEEKLY_EXPENSE = new RecurringTransfer(description: "weekly expense", frequency: 1, quantity: 1, unit: ChronoUnit.WEEKS,
			amount: 100, account: "checking/misc", startDate: LocalDate.of(2015,1,2));
	static final RecurringTransfer ANNUAL_BILL = new RecurringTransfer(description: "annual bill", frequency: 1, quantity: 1, unit: ChronoUnit.YEARS,
			amount: 50, account: "checking/bills/annual", startDate: LocalDate.of(2015,1,1));
	static final RecurringTransfer BI_ANNUAL_BILL = new RecurringTransfer(description: "every six months", frequency: 2, quantity: 1, unit: ChronoUnit.YEARS,
			amount: 300, account: "checking/bills/biannual", startDate: LocalDate.of(2015,1,1));
	static final RecurringTransfer LONG_TERM_GOAL = new RecurringTransfer(description: "long term goal", frequency: 1, quantity: 2, unit: ChronoUnit.YEARS,
			amount: 2000, account: "checking/save/long term", startDate: LocalDate.of(2015,1,1));

	static final List<RecurringTransfer> ALL_INCOME = [
		SEMI_WEEKLY_PAYCHECK,
		BI_MONTHLY_PAYCHECK,
		BI_MONTHLY_PAYCHECK_1,
	]
	static final List<RecurringTransfer> ALL_EXPENSES = [
		MONTHLY_BILL,
		WEEKLY_EXPENSE,
		ANNUAL_BILL,
		BI_ANNUAL_BILL,
		LONG_TERM_GOAL
	]
	static final List<RecurringTransfer> ALL_TRANSFERS = ALL_INCOME + ALL_EXPENSES;

	def setup(){
		recurringTransfers = Mock(RecurringTransferRepository);
		balances = Mock(BalanceService);
		service.recurringTransfers = recurringTransfers;
                service.balances = balances;
	}

	@Unroll
	def "calcDeduction() correctly calculates deduction for #expense.description from #income.description"(def income, def expense, def expectedResult){
		when:
		def result = service.round(service.calcDeduction(income, expense, [income]));

		then:
		def roundedExpectation = service.round(expectedResult);
		result > roundedExpectation * 0.90;
		result < roundedExpectation * 1.10;

		where:
		income | expense | expectedResult
		SEMI_WEEKLY_PAYCHECK | WEEKLY_EXPENSE | WEEKLY_EXPENSE.amount * 2
		SEMI_WEEKLY_PAYCHECK | ANNUAL_BILL | ANNUAL_BILL.amount / 26
		SEMI_WEEKLY_PAYCHECK | MONTHLY_BILL | MONTHLY_BILL.amount / 2.2
		SEMI_WEEKLY_PAYCHECK | BI_ANNUAL_BILL | BI_ANNUAL_BILL.amount / 12
		SEMI_WEEKLY_PAYCHECK | LONG_TERM_GOAL | LONG_TERM_GOAL.amount / 48
	}

	@Unroll
	def "annualize() converts a #paycheck.description paycheck to an annual amount"(def paycheck, def amount){
		when:
		RecurringTransfer result = service.annualize(paycheck);

		then:
		result.frequency == 1;
		result.quantity == 1;
		result.unit == ChronoUnit.YEARS;
		result.amount == amount;

		where:
		paycheck              | amount
		SEMI_WEEKLY_PAYCHECK  | SEMI_WEEKLY_PAYCHECK.amount * 26
		BI_MONTHLY_PAYCHECK   | BI_MONTHLY_PAYCHECK.amount * 24
		BI_MONTHLY_PAYCHECK_1 | BI_MONTHLY_PAYCHECK_1.amount * 12
		MONTHLY_BILL          | MONTHLY_BILL.amount * 12
		WEEKLY_EXPENSE        | WEEKLY_EXPENSE.amount * 52
		ANNUAL_BILL           | ANNUAL_BILL.amount
		BI_ANNUAL_BILL        | BI_ANNUAL_BILL.amount * 2
		LONG_TERM_GOAL        | LONG_TERM_GOAL.amount / 2
	}

	def "a timeline is produced"(){
		when:
		def result = service.timeline(ALL_TRANSFERS);

		then:
		result != null;
	}

	def "a budget with no fixed expenses returns a completely unallocated transaction"() {
		given:
		def incomeId = 0;
		def effectiveDate = LocalDate.now();

		recurringTransfers.findOne(incomeId) >> BI_MONTHLY_PAYCHECK_1;
		recurringTransfers.findWhereAmountIsNegative(effectiveDate) >> [SEMI_WEEKLY_PAYCHECK, BI_MONTHLY_PAYCHECK_1, BI_MONTHLY_PAYCHECK_2];
		recurringTransfers.findWhereAmountIsPositive(effectiveDate) >> [];

		when:
		Transaction result = service.buildBudget(incomeId, effectiveDate);

		then:
		result.transfers.size() == 1;
		result.transfers[0].amount == BI_MONTHLY_PAYCHECK_1.amount;
	}

	def "calcRecurrencesBetween counts semi-weekly paychecks"() {
		setup:
		def startDate = LocalDate.of(2015, 1, 2);

		expect:
		result == service.calcRecurrencesBetween(SEMI_WEEKLY_PAYCHECK, startDate, endDate).size();

		where:
		endDate                   | result
		LocalDate.of(2015, 1, 30) | 2
		LocalDate.of(2015, 1, 31) | 3
	}

	def "calcRecurrencesBetween counts monthly paychecks"() {
		expect:
		expected == service.calcRecurrencesBetween(BI_MONTHLY_PAYCHECK_2, startDate, endDate).size();

		where:
		startDate                 | endDate                   | expected
		LocalDate.of(2015, 1, 30) | LocalDate.of(2015, 1, 30) | 0 //same date is always 0
		LocalDate.of(2015, 1, 30) | LocalDate.of(2015, 2, 28) | 1 //startDate counts, end date doesn't
		LocalDate.of(2015, 1, 30) | LocalDate.of(2015, 3, 1)  | 2
		LocalDate.of(2015, 1, 30) | LocalDate.of(2015, 3, 30) | 2
		LocalDate.of(2015, 1, 30) | LocalDate.of(2015, 3, 31) | 3
		LocalDate.of(2016, 1, 30) | LocalDate.of(2016, 2, 29) | 1
		LocalDate.of(2016, 1, 30) | LocalDate.of(2016, 3, 1)  | 2
	}
}
