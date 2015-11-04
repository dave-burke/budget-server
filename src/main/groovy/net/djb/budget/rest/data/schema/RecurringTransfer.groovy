package net.djb.budget.rest.data.schema;

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
import javax.persistence.ManyToOne;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
class RecurringTransfer {

	@Id
	@GeneratedValue
	long id;

	@Column(nullable = false)
	String description;

	@Column(nullable = false)
	int frequency;

	@Column(nullable = false)
	int quantity;

	@Basic
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	ChronoUnit unit;

	@Column(nullable = false)
	long amount;

	@Column(nullable = false)
	String account;

	@Column(nullable = false)
	LocalDate startDate;

	LocalDate endDate;

}

