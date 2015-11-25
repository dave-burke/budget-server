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
import net.djb.budget.rest.util.BalanceTree;

@RestController
@RequestMapping("/balance")
class BalanceController {

	@Autowired BalanceService balanceService;

	@RequestMapping(value="/map", method = RequestMethod.GET)
	Map<String,Long> getBalanceMap(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") LocalDateTime asOf) {
		asOf = asOf ?: LocalDateTime.now();
		return balanceService.calcBalances(asOf);
	}

	@RequestMapping(value="/tree", method = RequestMethod.GET)
	BalanceTree getBalanceTree(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") LocalDateTime asOf) {
		asOf = asOf ?: LocalDateTime.now();
		return balanceService.calcBalanceTree(asOf);
	}

}
