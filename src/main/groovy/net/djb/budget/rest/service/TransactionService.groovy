package net.djb.budget.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.djb.budget.rest.data.schema.Transaction;
import net.djb.budget.rest.data.repo.TransactionRepository;
import net.djb.budget.rest.exception.UnbalancedTransactionException;

@Service
class TransactionService {

	static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

	static final int DEFAULT_PAGE_SIZE = 100;

	@Autowired TransactionRepository transactions;

	List<Transaction> list() {
		return list(0);
	}

	List<Transaction> list(int pageNumber) {
		Sort sort = new Sort(Sort.Direction.DESC, "time");
		PageRequest pageRequest = new PageRequest(pageNumber, DEFAULT_PAGE_SIZE, sort);
		Page<Transaction> page = transactions.findAll(pageRequest);
		return page.content;
	}

	Transaction save(Transaction transaction) {
		Long balance = transaction.transfers.sum{it.amount};
		if(balance != 0){
			LOG.warn("Transaction does not balance! ({})", balance);
			throw new UnbalancedTransactionException(balance);
		} else {
			transactions.save(transaction);
			LOG.info("Saved '{}' ({})", transaction.description, transaction.id);
			return transaction;
		}
	}

	void delete(long transactionId) {
		transactions.delete(transactionId);
		LOG.info("Deleted {}", transactionId);
	}

}
