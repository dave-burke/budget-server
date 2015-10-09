package net.djb.budget.service.data.schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class Account {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable = false)
	String name;

	boolean isActive = true;

}

