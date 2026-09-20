-- Seed Data for RasheedhaMart Capstone
-- Default Passwords for all seeded users: password123 (BCrypt hashed)

INSERT INTO users (id, email, password_hash, full_name, role) VALUES (1, 'admin@rasheedhamart.com', '$2a$10$76g.p5nS7uS8.5/VzBqOvuM2FvN92X5T1G.L4P/8J3QZ5X0W9K02S', 'System Administrator', 'ADMIN');
INSERT INTO users (id, email, password_hash, full_name, role) VALUES (2, 'seller.rasheedha@rasheedhamart.com', '$2a$10$76g.p5nS7uS8.5/VzBqOvuM2FvN92X5T1G.L4P/8J3QZ5X0W9K02S', 'Rasheedha Seller Store', 'SELLER');
INSERT INTO users (id, email, password_hash, full_name, role) VALUES (3, 'seller.tech@rasheedhamart.com', '$2a$10$76g.p5nS7uS8.5/VzBqOvuM2FvN92X5T1G.L4P/8J3QZ5X0W9K02S', 'TechZone Electronics', 'SELLER');
INSERT INTO users (id, email, password_hash, full_name, role) VALUES (4, 'buyer.john@gmail.com', '$2a$10$76g.p5nS7uS8.5/VzBqOvuM2FvN92X5T1G.L4P/8J3QZ5X0W9K02S', 'John Doe', 'BUYER');
INSERT INTO users (id, email, password_hash, full_name, role) VALUES (5, 'buyer.priya@gmail.com', '$2a$10$76g.p5nS7uS8.5/VzBqOvuM2FvN92X5T1G.L4P/8J3QZ5X0W9K02S', 'Priya Sharma', 'BUYER');
ALTER TABLE users ALTER COLUMN id RESTART WITH 6;

INSERT INTO products (id, seller_id, name, description, price, stock_quantity, category, image_url) VALUES (1, 2, 'Organic Organic Spices Pack', 'Premium quality authentic South Indian organic spice gift box.', 499.00, 50, 'Groceries', 'https://images.unsplash.com/photo-1596040033229-a9821ebd058d?auto=format&fit=crop&w=500&q=80');
INSERT INTO products (id, seller_id, name, description, price, stock_quantity, category, image_url) VALUES (2, 2, 'Handcrafted Cotton Saree', 'Traditional handmade pure cotton saree with elegant zari border.', 1850.00, 20, 'Fashion', 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&w=500&q=80');
INSERT INTO products (id, seller_id, name, description, price, stock_quantity, category, image_url) VALUES (3, 3, 'Wireless Noise Cancelling Headphones', 'High fidelity audio with 30-hour battery life and active noise reduction.', 3499.00, 15, 'Electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=500&q=80');
INSERT INTO products (id, seller_id, name, description, price, stock_quantity, category, image_url) VALUES (4, 3, 'Smart Fitness Watch v2', 'Track heart rate, sleep, steps and oxygen levels with HD AMOLED display.', 2299.00, 30, 'Electronics', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=500&q=80');
INSERT INTO products (id, seller_id, name, description, price, stock_quantity, category, image_url) VALUES (5, 2, 'Eco-Friendly Bamboo Water Bottle', 'BPA-free stainless steel bottle with natural bamboo outer insulation.', 799.00, 100, 'Home & Living', 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?auto=format&fit=crop&w=500&q=80');
ALTER TABLE products ALTER COLUMN id RESTART WITH 6;

INSERT INTO orders (id, buyer_id, total_amount, status) VALUES (1, 4, 3998.00, 'DELIVERED');
INSERT INTO orders (id, buyer_id, total_amount, status) VALUES (2, 5, 2299.00, 'SHIPPED');
ALTER TABLE orders ALTER COLUMN id RESTART WITH 3;

INSERT INTO order_items (id, order_id, product_id, seller_id, quantity, unit_price) VALUES (1, 1, 1, 2, 1, 499.00);
INSERT INTO order_items (id, order_id, product_id, seller_id, quantity, unit_price) VALUES (2, 1, 3, 3, 1, 3499.00);
INSERT INTO order_items (id, order_id, product_id, seller_id, quantity, unit_price) VALUES (3, 2, 4, 3, 1, 2299.00);
ALTER TABLE order_items ALTER COLUMN id RESTART WITH 4;

INSERT INTO reviews (id, product_id, buyer_id, rating, comment) VALUES (1, 1, 4, 5, 'Authentic aroma and excellent freshness! Highly recommended.');
INSERT INTO reviews (id, product_id, buyer_id, rating, comment) VALUES (2, 3, 4, 4, 'Great sound quality and battery life. Fast delivery by seller.');
ALTER TABLE reviews ALTER COLUMN id RESTART WITH 3;
