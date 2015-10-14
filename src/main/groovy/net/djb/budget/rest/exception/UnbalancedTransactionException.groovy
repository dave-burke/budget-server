package net.djb.budget.rest.exception;

class UnbalancedTransactionException extends RuntimeException {
	
	long balance;

	UnbalancedTransactionException(
			long balance,
			String message = "Transaction balanced to ${balance}. Amounts in Transfers under a single Transaction must sum to zero.") {
		super(message);
		this.balance = balance;
	}

}
