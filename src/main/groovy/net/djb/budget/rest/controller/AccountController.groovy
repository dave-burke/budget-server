package net.djb.budget.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.dao.DataIntegrityViolationException

import net.djb.budget.rest.service.AccountService;
import net.djb.budget.rest.data.schema.Account;

@RestController
@RequestMapping("/accounts")
class AccountController {

	@Autowired AccountService accountService;

	@RequestMapping(method = RequestMethod.GET)
	List<Account> list(@RequestParam(required = false, defaultValue = "false") boolean includeInactive) {
		if(includeInactive){
			return accountService.listAll();
		} else {
			return accountService.listActive();
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<Account> save(@RequestBody Account account) {
		try {
			account = accountService.save(account);
			return new ResponseEntity<Account>(account, HttpStatus.CREATED);
		} catch(DataIntegrityViolationException e) {
			Account existing = accountService.findByName(account.name);
			return new ResponseEntity<Account>(existing, HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	ResponseEntity<Account> save(@PathVariable long id, @RequestBody Account account) {
		account.id = id;
		return save(account);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	ResponseEntity<Account> delete(@PathVariable long id) {
		Account deactivated = accountService.deactivate(id);
		if(deactivated) {
			return new ResponseEntity<Account>(deactivated, HttpStatus.OK);
		} else {
			return new ResponseEntity<Account>(HttpStatus.NOT_FOUND);
		}
	}

}
