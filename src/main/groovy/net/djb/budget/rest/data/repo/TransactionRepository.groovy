package net.djb.budget.rest.data.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import net.djb.budget.rest.data.schema.Transaction;
import org.springframework.stereotype.Repository;

@Repository
interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

}

