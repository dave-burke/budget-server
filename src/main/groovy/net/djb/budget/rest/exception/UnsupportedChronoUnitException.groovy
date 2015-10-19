package net.djb.budget.rest.exception;

import java.time.temporal.ChronoUnit;

class UnsupportedChronoUnitException extends RuntimeException {
	
	ChronoUnit unit;

	UnsupportedChronoUnitException(ChronoUnit unit, String message = "${unit} is not supported.") {
		super(message);
		this.unit = unit;
	}

}
