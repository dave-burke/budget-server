package net.djb.budget.rest.data.schema;

import groovy.transform.Canonical;
import groovy.transform.Sortable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Canonical
@Sortable
@Entity
class Transfer {

	@Column(nullable = false)
	String account;

	@Column(nullable = false)
	Long amount;

	@Id 
	@GeneratedValue
	Long id;

}

