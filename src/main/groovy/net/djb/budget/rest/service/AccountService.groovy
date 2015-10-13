package net.djb.budget.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException

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

	Account findByName(String name){
		return accounts.findByName(name);
	}

	Account save(Account account) {
		try {
			accounts.save(account);
			LOG.info("Saved {}", account.name);
			return account;
		} catch(DataIntegrityViolationException e) {
			Account existing = accounts.findByName(account.name);
			if(existing.isActive) {
				LOG.warn("{} already exists and is active", existing.name);
				throw e;
			} else {
				existing.isActive = true;
				accounts.save(existing);
				LOG.info("Activated {}", existing.name);
				return existing;
			}
		}
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
