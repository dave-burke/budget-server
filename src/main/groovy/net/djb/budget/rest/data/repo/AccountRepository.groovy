package net.djb.budget.rest.data.repo;

import org.springframework.data.repository.CrudRepository;
import net.djb.budget.rest.data.schema.Account;
import org.springframework.stereotype.Repository;

@Repository
interface AccountRepository extends CrudRepository<Account, Long> {

	Account findByName(String name);

	List<Account> findByIsActive(boolean isActive);

}

