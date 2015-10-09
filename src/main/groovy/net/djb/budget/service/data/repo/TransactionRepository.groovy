package net.djb.budget.service.data.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import net.djb.budget.service.data.schema.Transaction;
import org.springframework.stereotype.Repository;

@Repository
interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

}

