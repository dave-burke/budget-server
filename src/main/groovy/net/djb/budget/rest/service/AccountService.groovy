package net.djb.budget.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.djb.budget.rest.data.schema.Account;
import net.djb.budget.rest.data.repo.AccountRepository;

@Service
class AccountService {

	static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

	@Autowired AccountRepository accounts;

	List<Account> listActive() {
		return accounts.findByIsActive(true);
	}

	List<Account> listAll() {
		return accounts.findAll();
	}

	void save(Account account) {
		accounts.save(account);
	}

	void delete(long accountId) {
		accounts.delete(accountId);
	}

	Account deactivate(long accountId) {
		Account account = accounts.findOne(accountId);
		//TODO reject if balance is non-zero
		if(account == null){
			return null;
		} else {
			account.isActive = false;
			accounts.save(account);
			LOG.info("Deactivated {}", account.name);
			return account;
		}
	}

}
