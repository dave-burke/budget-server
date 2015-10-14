INSERT INTO account (id, name, is_active) VALUES (1, 'expenses', true);
INSERT INTO account (id, name, is_active) VALUES (2, 'equity/init', true);
INSERT INTO account (id, name, is_active) VALUES (3, 'equity/income/paycheck', true);
INSERT INTO account (id, name, is_active) VALUES (4, 'liabilities/credit/credit card', true);
INSERT INTO account (id, name, is_active) VALUES (5, 'liabilities/loans/auto', true);
INSERT INTO account (id, name, is_active) VALUES (6, 'assets/checking/bills', true);
INSERT INTO account (id, name, is_active) VALUES (7, 'assets/checking/save', true);
INSERT INTO account (id, name, is_active) VALUES (8, 'assets/checking/misc', true);
INSERT INTO account (id, name, is_active) VALUES (9, 'assets/checking/fun', true);
INSERT INTO account (id, name, is_active) VALUES (10, 'assets/checking/pending/credit card', true);
INSERT INTO account (id, name, is_active) VALUES (11, 'assets/checking/pending/old card', false);
INSERT INTO account (id, name, is_active) VALUES (12, 'assets/checking/pending/checks', true);
INSERT INTO account (id, name, is_active) VALUES (13, 'assets/savings', true);

INSERT INTO transaction (id, time, description, status) VALUES (1, '2015-01-01', 'Init', 'CLEARED');
INSERT INTO transfer (id, transaction_id, account_id, amount) VALUES (1, 1, 2, 100);
INSERT INTO transfer (id, transaction_id, account_id, amount) VALUES (2, 1, 6, 60);
INSERT INTO transfer (id, transaction_id, account_id, amount) VALUES (3, 1, 7, 20);
INSERT INTO transfer (id, transaction_id, account_id, amount) VALUES (4, 1, 8, 10);
INSERT INTO transfer (id, transaction_id, account_id, amount) VALUES (5, 1, 9, 10);

