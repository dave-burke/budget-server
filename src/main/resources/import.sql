INSERT INTO transaction (id, time, description, status) VALUES (1, '2015-01-01', 'Init', 'CLEARED');
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (1, 1, 'equity/init', -100);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (2, 1, 'assets/checking/bills', 60);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (3, 1, 'assets/checking/save', 20);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (4, 1, 'assets/checking/misc', 10);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (5, 1, 'assets/checking/fun', 10);

INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (1, 'John''s paycheck', 1, 2, 'WEEKS', 'income/paychecks/john', -1000, '2015-01-02')
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (2, 'Jane''s paycheck (15th)', 1, 1, 'MONTHS', 'income/paychecks/jane', -2000, '2015-01-15')
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (3, 'Jane''s paycheck (30th)', 1, 1, 'MONTHS', 'income/paychecks/jane', -2000, '2015-01-30')
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (4, 'Bills', 1, 1, 'MONTHS', 'assets/checking/bills', 2400, '2015-01-01')
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (5, 'Savings', 1, 1, 'WEEKS', 'assets/checking/save', 200, '2015-01-02')
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (6, 'Misc', 1, 1, 'WEEKS', 'assets/checking/misc', 100, '2015-01-02')
INSERT INTO recurring_transfer (id, description, frequency, quantity, unit, account, amount, start_date) VALUES (7, 'Fun', 1, 1, 'WEEKS', 'assets/checking/fun', 100, '2015-01-02')

