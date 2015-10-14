package net.djb.budget.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.djb.budget.rest.data.repo.TransferRepository;

@Service
class AccountService {

	static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

	@Autowired TransferRepository transfers;

	List<String> list() {
		return transfers.findAccountNames();
	}

	Map<String, Long> balances() {
		return list().collectEntries{[it, transfers.calcBalance(it)]};
	}

}
