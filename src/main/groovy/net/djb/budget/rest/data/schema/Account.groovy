package net.djb.budget.rest.data.schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class Account {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable = false, unique = true)
	String name;

	boolean isActive = true;

}

