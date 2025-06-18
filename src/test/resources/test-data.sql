DELETE FROM orders;
ALTER TABLE orders AUTO_INCREMENT = 1;

INSERT INTO orders (product, price, qty) VALUES
( 'Laptop', 1200.00, 2),
( 'Smartphone', 800.00, 5),
( 'Tablet', 300.00, 10),
('Monitor', 400.00, 3),
('Keyboard', 50.00, 20);
