package net.djb.budget.rest.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import net.djb.budget.rest.data.schema.Transfer;
import org.springframework.stereotype.Repository;

@Repository
interface TransferRepository extends CrudRepository<Transfer, Long> {

	@Query("SELECT t.account FROM Transfer t")
	List<String> findAccountNames();

	@Query("SELECT SUM(t.amount) FROM Transfer t WHERE t.account = :account")
	Long calcBalance(@Param("account") String account);

}

