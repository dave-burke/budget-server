-- Initial values
INSERT INTO transaction (id, time, description) VALUES (1, '2015-01-01', 'Init');
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (1, 1, 'equity:init', -100);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (2, 1, 'assets:checking:bills', 60);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (3, 1, 'assets:checking:save', 20);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (4, 1, 'assets:checking:misc', 10);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (5, 1, 'assets:checking:fun', 10);

-- Write a check
INSERT INTO transaction (id, time, description, status, code) VALUES (2, '2015-01-02', 'Pay a bill', 'PENDING', 1234);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (6, 2, 'assets:checking:bills', -20);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (7, 2, 'expenses:bills', 20);

-- Check is cleared
UPDATE transaction SET status = 'CLEARED' WHERE id = 2;

-- Use credit card
INSERT INTO transaction (id, time, description, status) VALUES (3, '2015-01-03', 'Buy something', 'PENDING');
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (8, 3, 'liabilities:credit card', -5);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (9, 3, 'expenses:misc', 5);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (10, 3, 'assets:checking:misc', -5);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (11, 3, 'assets:checking:pending:credit card', 5);

-- Credit transaction clears
UPDATE transaction SET status = 'CLEARED' WHERE id = 3;

-- Pay credit card
INSERT INTO transaction (id, time, description) VALUES (4, '2015-01-04', 'Pay credit card');
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (12, 4, 'assets:checking:pending:credit card', -5);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (13, 4, 'liabilities:credit card', 5);

-- Set up budgeting
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (1, 'John''s paycheck', 1, 2, 'WEEKS', 'income:paychecks:john', -1000, '2015-01-02')
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (2, 'Jane''s paycheck (15th)', 1, 1, 'MONTHS', 'income:paychecks:jane', -2000, '2015-01-15')
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (3, 'Jane''s paycheck (30th)', 1, 1, 'MONTHS', 'income:paychecks:jane', -2000, '2015-01-30')
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (4, 'Bills', 1, 1, 'MONTHS', 'assets:checking:bills', 2400, '2015-01-01')
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (5, 'Savings', 1, 1, 'WEEKS', 'assets:checking:save', 200, '2015-01-02')
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (6, 'Misc', 1, 1, 'WEEKS', 'assets:checking:misc', 100, '2015-01-02')
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (7, 'Fun', 1, 1, 'WEEKS', 'assets:checking:fun', 100, '2015-01-02')

