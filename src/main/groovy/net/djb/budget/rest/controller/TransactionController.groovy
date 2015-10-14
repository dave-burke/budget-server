package net.djb.budget.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.djb.budget.rest.exception.UnbalancedTransactionException;
import net.djb.budget.rest.service.TransactionService;
import net.djb.budget.rest.data.schema.Transaction;

@RestController
@RequestMapping("/transactions")
class TransactionController {

	@Autowired TransactionService transactionService;

	@RequestMapping(method = RequestMethod.GET)
	List<Transaction> list(@RequestParam(required = false, defaultValue = "0") int pageNumber) {
		return transactionService.list(pageNumber);
	}

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<Transaction> save(@RequestBody Transaction transaction) {
		boolean isNew = transaction.id == null;
		transaction = transactionService.save(transaction);
		if(isNew){
			return new ResponseEntity<Transaction>(transaction, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	ResponseEntity<Transaction> update(@PathVariable long id, @RequestBody Transaction transaction) {
		transaction.id = id;
		return save(transaction);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	void update(@PathVariable long id) {
		transactionService.delete(id);
	}

}
