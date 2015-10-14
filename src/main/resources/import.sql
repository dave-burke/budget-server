INSERT INTO transaction (id, time, description, status) VALUES (1, '2015-01-01', 'Init', 'CLEARED');
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (1, 1, 'equity/init', -100);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (2, 1, 'assets/checking/bills', 60);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (3, 1, 'assets/checking/save', 20);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (4, 1, 'assets/checking/misc', 10);
INSERT INTO transfer (id, transaction_id, account, amount) VALUES (5, 1, 'assets/checking/fun', 10);

