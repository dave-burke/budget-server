package net.djb.budget.service.data.schema;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
class Transfer {

	@Id 
	@GeneratedValue
	Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "accountId", nullable=false)
	Account account;

	@Column(nullable = false)
	Long amount;

}

