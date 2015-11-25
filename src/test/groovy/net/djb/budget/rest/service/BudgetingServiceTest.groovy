package net.djb.budget.rest.service;

import spock.lang.Specification;

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

	def setup(){
		recurringTransfers = Mock(RecurringTransferRepository);
		balances = Mock(BalanceService);
		service.recurringTransfers = recurringTransfers;
                service.balances = balances;
	}

	def "correctly calculates a weekly expense for a semiWeekly paycheck"() {
		setup:
		def incomeId = 0;

		recurringTransfers.findOne(incomeId) >> SEMI_WEEKLY_PAYCHECK;
		recurringTransfers.findWhereAmountIsNegative(effectiveDate) >> [SEMI_WEEKLY_PAYCHECK];
		recurringTransfers.findWhereAmountIsPositive(effectiveDate) >> [WEEKLY_EXPENSE];

		balances.calcBalance(_) >> balance;

		expect:
		Transaction budget = service.buildBudget(incomeId, effectiveDate);
		result == budget.transfers.find{it.account == WEEKLY_EXPENSE.account}.amount;

		where:
		effectiveDate             | balance | result
		LocalDate.of(2015, 1, 2)  | 0       | 200
		LocalDate.of(2015, 1, 16) | 10      | 190 
		LocalDate.of(2015, 1, 30) | 200     | 0
		LocalDate.of(2015, 2, 13) | 210     | -10
	}

	def "correctly calculates a monthly bill for a semiWeekly paycheck"() {
		setup:
		def incomeId = 0;

		recurringTransfers.findOne(incomeId) >> SEMI_WEEKLY_PAYCHECK;
		recurringTransfers.findWhereAmountIsNegative(effectiveDate) >> [SEMI_WEEKLY_PAYCHECK];
		recurringTransfers.findWhereAmountIsPositive(effectiveDate) >> [MONTHLY_BILL];

		balances.calcBalance(_) >> balance;

		expect:
		Transaction budget = service.buildBudget(incomeId, effectiveDate);
		result == budget.transfers.find{it.account == MONTHLY_BILL.account}.amount;

		where:
		effectiveDate             | balance | result
		LocalDate.of(2015, 1, 2)  | 0       | 167
		LocalDate.of(2015, 1, 16) | 167     | 167
		LocalDate.of(2015, 1, 30) | 334     | 166
	}

	def "correctly calculates an annual bill for a semiWeekly paycheck"() {
		setup:
		def incomeId = 0;

		recurringTransfers.findOne(incomeId) >> SEMI_WEEKLY_PAYCHECK;
		recurringTransfers.findWhereAmountIsNegative(effectiveDate) >> [SEMI_WEEKLY_PAYCHECK];
		recurringTransfers.findWhereAmountIsPositive(effectiveDate) >> [ANNUAL_BILL];

		balances.calcBalance(_) >> balance;

		expect:
		Transaction budget = service.buildBudget(incomeId, effectiveDate);
		result == budget.transfers.find{it.account == ANNUAL_BILL.account}.amount;

		where:
		effectiveDate             | balance | result
		LocalDate.of(2015, 1, 2)  | 0       | 2
		LocalDate.of(2015, 1, 16) | 2       | 2 
		LocalDate.of(2015, 1, 30) | 4       | 2
	}

	def "correctly calculates a weekly expense for a biMonthly paycheck 1"() {
		setup:
		def incomeId = 0;

		recurringTransfers.findOne(incomeId) >> paycheck;
		recurringTransfers.findWhereAmountIsNegative(effectiveDate) >> [BI_MONTHLY_PAYCHECK_1, BI_MONTHLY_PAYCHECK_2];
		recurringTransfers.findWhereAmountIsPositive(effectiveDate) >> [WEEKLY_EXPENSE];

		balances.calcBalance(_) >> balance;

		expect:
		Transaction budget = service.buildBudget(incomeId, effectiveDate);
		result == budget.transfers.find{it.account == WEEKLY_EXPENSE.account}.amount;

		where:
		effectiveDate             | paycheck              | balance | result
		LocalDate.of(2015, 1, 15) | BI_MONTHLY_PAYCHECK_1 | 0       | 213 //needs to last 15 days, not 2 weeks
		LocalDate.of(2015, 1, 30) | BI_MONTHLY_PAYCHECK_2 | 0       | 225 //needs to last 16 days
		LocalDate.of(2015, 2, 15) | BI_MONTHLY_PAYCHECK_1 | 0       | 213
		LocalDate.of(2015, 2, 28) | BI_MONTHLY_PAYCHECK_2 | 13      | 200
		LocalDate.of(2015, 3, 15) | BI_MONTHLY_PAYCHECK_1 | 220     | -8
	}

	def "correctly calculates a monthly bill for a biMonthly paycheck 1"() {
		setup:
		def incomeId = 0;

		recurringTransfers.findOne(incomeId) >> BI_MONTHLY_PAYCHECK_1;
		recurringTransfers.findWhereAmountIsNegative(effectiveDate) >> [BI_MONTHLY_PAYCHECK_1, BI_MONTHLY_PAYCHECK_2];
		recurringTransfers.findWhereAmountIsPositive(effectiveDate) >> [MONTHLY_BILL];

		balances.calcBalance(_) >> balance;

		expect:
		Transaction budget = service.buildBudget(incomeId, effectiveDate);
		result == budget.transfers.find{it.account == MONTHLY_BILL.account}.amount;

		where:
		effectiveDate             | paycheck              | balance | result
		LocalDate.of(2015, 1, 15) | BI_MONTHLY_PAYCHECK_1 | 0       | 250
		LocalDate.of(2015, 1, 30) | BI_MONTHLY_PAYCHECK_2 | 250     | 250
		LocalDate.of(2015, 2, 15) | BI_MONTHLY_PAYCHECK_1 | 0       | 250
		LocalDate.of(2015, 2, 28) | BI_MONTHLY_PAYCHECK_2 | 250     | 250
		LocalDate.of(2015, 3, 30) | BI_MONTHLY_PAYCHECK_2 | 0       | 500
	}

	def "correctly calculates an annual bill for a biMonthly paycheck"() {
		setup:
		def incomeId = 0;

		recurringTransfers.findOne(incomeId) >> BI_MONTHLY_PAYCHECK_1;
		recurringTransfers.findWhereAmountIsNegative(effectiveDate) >> [BI_MONTHLY_PAYCHECK_1, BI_MONTHLY_PAYCHECK_2];
		recurringTransfers.findWhereAmountIsPositive(effectiveDate) >> [ANNUAL_BILL];

		balances.calcBalance(_) >> balance;

		expect:
		Transaction budget = service.buildBudget(incomeId, effectiveDate);
		result == budget.transfers.find{it.account == ANNUAL_BILL.account}.amount;

		where:
		effectiveDate             | balance | result
		LocalDate.of(2015, 1, 15) | 0       | 3
		LocalDate.of(2015, 2, 15) | 6       | 3 
		LocalDate.of(2015, 3, 15) | 9       | 3
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
		result == service.calcRecurrencesBetween(SEMI_WEEKLY_PAYCHECK, startDate, endDate);

		where:
		endDate                   | result
		LocalDate.of(2015, 1, 30) | 2
		LocalDate.of(2015, 1, 31) | 3
	}

	def "calcRecurrencesBetween counts monthly paychecks"() {
		expect:
		expected == service.calcRecurrencesBetween(BI_MONTHLY_PAYCHECK_2, startDate, endDate);

		where:
		startDate                 | endDate                   | expected
		LocalDate.of(2015, 1, 30) | LocalDate.of(2015, 1, 30) | 1 //same date is always 1
		LocalDate.of(2015, 1, 30) | LocalDate.of(2015, 2, 28) | 1 //startDate counts, end date doesn't
		LocalDate.of(2015, 1, 30) | LocalDate.of(2015, 3, 1)  | 2
		LocalDate.of(2015, 1, 30) | LocalDate.of(2015, 3, 30) | 2
		LocalDate.of(2015, 1, 30) | LocalDate.of(2015, 3, 31) | 3
		LocalDate.of(2016, 1, 30) | LocalDate.of(2016, 2, 29) | 1
		LocalDate.of(2016, 1, 30) | LocalDate.of(2016, 3, 1)  | 2
	}
}
