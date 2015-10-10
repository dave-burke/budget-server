package net.djb.budget.service.data.repo;

import org.springframework.data.repository.CrudRepository;
import net.djb.budget.service.data.schema.Account;
import org.springframework.stereotype.Repository;

@Repository
interface AccountRepository extends CrudRepository<Account, Long> {

	Account findByName(String name);

	List<Account> findByIsActive(boolean isActive);

}

