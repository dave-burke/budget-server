package net.djb.budget.service.data.schema;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import net.djb.budget.service.constant.TransactionStatus;
import java.time.LocalDateTime;

@Entity
class Transaction {

	@Id
	@GeneratedValue
	Long id;

	LocalDateTime time;

	@Column(nullable = false)
	String description;

	String otherParty;

	Integer code;

	TransactionStatus status;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "transactionId", nullable = false)
	List<Transfer> transfers;

}

