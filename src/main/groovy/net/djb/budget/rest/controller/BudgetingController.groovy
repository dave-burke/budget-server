package net.djb.budget.rest.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.djb.budget.rest.data.schema.RecurringTransfer;
import net.djb.budget.rest.data.schema.Transaction;
import net.djb.budget.rest.service.BudgetingService;

@RestController
class BudgetingController {

	@Autowired BudgetingService budgetingService;

	@RequestMapping(value = "/expenses", method = RequestMethod.GET)
	List<RecurringTransfer> listFixedExpenses(@RequestParam(required=false) LocalDate effectiveDate) {
		effectiveDate = effectiveDate ?: LocalDate.now();
		return budgetingService.listFixedExpenses(effectiveDate);
	}

	@RequestMapping(value = "/income", method = RequestMethod.GET)
	List<RecurringTransfer> listFixedIncome(@RequestParam(required=false) LocalDate effectiveDate) {
		effectiveDate = effectiveDate ?: LocalDate.now();
		return budgetingService.listFixedIncome(effectiveDate);
	}

	@RequestMapping(value = "/income/{id}/budget", method = RequestMethod.GET)
	Transaction buildBudget(@PathVariable long id, @RequestParam(required=false) LocalDate effectiveDate) {
		effectiveDate = effectiveDate ?: LocalDate.now();
		return budgetingService.buildBudget(id, effectiveDate);
	}

}
