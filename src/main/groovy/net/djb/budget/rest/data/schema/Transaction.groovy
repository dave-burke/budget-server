package net.djb.budget.rest.data.schema;

import groovy.transform.Canonical;
import groovy.transform.Sortable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import net.djb.budget.rest.constant.TransactionStatus;
import java.time.LocalDateTime;

@Canonical
@Sortable(includes=["time","description"])
@Entity
class Transaction implements Comparable<Transaction> {

	LocalDateTime time;

	@Column(nullable = false)
	String description;

	String otherParty;

	Integer code;

	@Basic
	@Enumerated(EnumType.STRING)
	TransactionStatus status;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "transaction_id", nullable = false)
	List<Transfer> transfers;

	@Id
	@GeneratedValue
	Long id;

}

