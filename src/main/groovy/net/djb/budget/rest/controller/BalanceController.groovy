package net.djb.budget.rest.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.djb.budget.rest.service.BalanceService;

@RestController
class BalanceController {

	@Autowired BalanceService balanceService;

	@RequestMapping(value="/balance", method = RequestMethod.GET)
	Map<String,Long> getBalance(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") LocalDateTime asOf) {
		asOf = asOf ?: LocalDateTime.now();
		return ["": balanceService.calcBalance(asOf)];
	}

	@RequestMapping(value="/balances", method = RequestMethod.GET)
	Map<String,Long> getBalances(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") LocalDateTime asOf) {
		asOf = asOf ?: LocalDateTime.now();
		return balanceService.calcBalances(asOf);
	}

	@RequestMapping(value="/balance/{accountName}", method = RequestMethod.GET)
	Map<String,Long> getBalance(@PathVariable String accountName, @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") LocalDateTime asOf) {
		asOf = asOf ?: LocalDateTime.now();
		return [accountName: balanceService.calcBalance(accountName, asOf)];
	}

	@RequestMapping(value="/balances/{accountName}", method = RequestMethod.GET)
	Map<String,Long> getBalances(@PathVariable String accountName, @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") LocalDateTime asOf) {
		asOf = asOf ?: LocalDateTime.now();
		return balanceService.calcBalances(accountName, asOf);
	}

}
