package net.djb.budget.rest.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.djb.budget.rest.data.repo.BalanceRepository;
import net.djb.budget.rest.util.BalanceTree;

@Service
class BalanceService {

	static final Logger LOG = LoggerFactory.getLogger(BalanceService.class);

	@Autowired BalanceRepository balances;

	Long calcBalance(LocalDateTime asOf = LocalDateTime.now()) {
		return balances.calcBalance(asOf) ?: 0;
	}

	Long calcBalance(String accountName, LocalDateTime asOf = LocalDateTime.now()) {
		return balances.calcBalance(accountName, asOf) ?: 0;
	}

	Map<String, Long> calcBalances(LocalDateTime asOf = LocalDateTime.now()){
		return balances.calcBalances(asOf) ?: Collections.emptyMap();
	}

	Map<String, Long> calcBalances(String accountName, LocalDateTime asOf = LocalDateTime.now()){
		return balances.calcBalances(accountName, asOf) ?: Collections.emptyMap();
	}
	
	BalanceTree calcBalanceTree(LocalDateTime asOf = LocalDateTime.now()){
		return new BalanceTree(calcBalances(asOf));
	}

}
