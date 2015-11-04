package net.djb.budget.rest.data.repo;

import java.time.LocalDate;

import net.djb.budget.rest.data.schema.RecurringTransfer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface RecurringTransferRepository extends CrudRepository<RecurringTransfer, Long> {

	@Query("SELECT t FROM RecurringTransfer t WHERE t.amount > 0 AND (t.startDate IS NULL OR t.startDate <= :effectiveDate) AND (t.endDate IS NULL OR t.endDate >= :effectiveDate) ORDER BY t.account")
	List<RecurringTransfer> findWhereAmountIsPositive(@Param("effectiveDate") LocalDate effectiveDate);

	@Query("SELECT t FROM RecurringTransfer t WHERE t.amount < 0 AND (t.startDate IS NULL OR t.startDate <= :effectiveDate) AND (t.endDate IS NULL OR t.endDate >= :effectiveDate) ORDER BY t.account")
	List<RecurringTransfer> findWhereAmountIsNegative(@Param("effectiveDate") LocalDate effectiveDate);

	@Query("SELECT SUM(t.amount) FROM RecurringTransfer t WHERE t.amount > 0 AND (t.startDate IS NULL OR t.startDate <= :effectiveDate) AND (t.endDate IS NULL OR t.endDate >= :effectiveDate) ORDER BY t.account")
	long findSumWhereAmountIsPositive(@Param("effectiveDate") LocalDate effectiveDate);

	@Query("SELECT SUM(t.amount) FROM RecurringTransfer t WHERE t.amount < 0 AND (t.startDate IS NULL OR t.startDate <= :effectiveDate) AND (t.endDate IS NULL OR t.endDate >= :effectiveDate) ORDER BY t.account")
	long findSumWhereAmountIsNegative(@Param("effectiveDate") LocalDate effectiveDate);


}

