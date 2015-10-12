package net.djb.budget.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.djb.budget.rest.data.schema.Account;
import net.djb.budget.rest.data.repo.AccountRepository;

@Service
class AccountService {

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

	void delete(String name) {
		Account account = accounts.findByName(name);
		if(account != null){
			accounts.delete(account);
		}
	}

}
