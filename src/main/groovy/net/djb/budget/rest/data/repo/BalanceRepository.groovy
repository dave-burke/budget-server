package net.djb.budget.rest.data.repo;

import java.time.LocalDateTime;

import javax.persistence.PersistenceContext
import javax.persistence.EntityManager
import javax.persistence.Query
import org.springframework.stereotype.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
class BalanceRepository {

	static final Logger LOG = LoggerFactory.getLogger(BalanceRepository.class);

	@PersistenceContext
	private EntityManager em;

	Long calcBalance(LocalDateTime asOf){
		Query query = em.createQuery("SELECT SUM(tr.amount) FROM Transaction tx JOIN tx.transfers tr WHERE tx.time <= :asOf");
		query.setParameter("asOf", asOf);

		return query.getSingleResult();
	}

	Long calcBalance(String account, LocalDateTime asOf) {
		Query query = em.createQuery("SELECT SUM(tr.amount) FROM Transaction tx JOIN tx.transfers tr WHERE tr.account LIKE :account AND tx.time <= :asOf");
		query.setParameter("account", account + "%");
		query.setParameter("asOf", asOf);

		return query.getSingleResult();
	}

	Map<String, Long> calcBalances(LocalDateTime asOf) {
		Query query = em.createQuery("SELECT tr.account, SUM(tr.amount) FROM Transaction tx JOIN tx.transfers tr WHERE tx.time <= :asOf GROUP BY tr.account");
		query.setParameter("asOf", asOf);

		List<Object[]> results = query.getResultList();

		return results.collectEntries {
			String account = it[0];
			Long amount = it[1];
			return [account, amount];
		}
	}

	Map<String, Long> calcBalances(String account, LocalDateTime asOf) {
		Query query = em.createQuery("SELECT tr.account, SUM(tr.amount) FROM Transaction tx JOIN tx.transfers tr WHERE tr.account LIKE :account AND tx.time <= :asOf GROUP BY tr.account");
		query.setParameter("account", account + "%");
		query.setParameter("asOf", asOf);

		List<Object[]> results = query.getResultList();

		return results.collectEntries {
			return [it[0], it[1]];
		}
	}


}
