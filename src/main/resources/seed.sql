-- Seed Data for RasheedhaMart Platform
-- Admin Account: RasheedhaMartAdmin@gmail.com / rasheedhamart00

INSERT INTO users (id, email, password_hash, full_name, phone_number, role) VALUES (1, 'rasheedhamartadmin@gmail.com', '$2a$10$pD9d0r05GKYE8DPJ0c/qyufNdb00s9SprexayZH3TxyaI4P2XVOhS', 'System Administrator', '+91 9876543210', 'ADMIN');
INSERT INTO users (id, email, password_hash, full_name, phone_number, role) VALUES (2, 'seller.rasheedha@rasheedhamart.com', '$2a$10$76g.p5nS7uS8.5/VzBqOvuM2FvN92X5T1G.L4P/8J3QZ5X0W9K02S', 'Rasheedha Merchant Store', '+91 9876543211', 'SELLER');
INSERT INTO users (id, email, password_hash, full_name, phone_number, role) VALUES (3, 'seller.tech@rasheedhamart.com', '$2a$10$76g.p5nS7uS8.5/VzBqOvuM2FvN92X5T1G.L4P/8J3QZ5X0W9K02S', 'TechZone Electronics', '+91 9876543212', 'SELLER');
INSERT INTO users (id, email, password_hash, full_name, phone_number, role) VALUES (4, 'buyer.john@gmail.com', '$2a$10$76g.p5nS7uS8.5/VzBqOvuM2FvN92X5T1G.L4P/8J3QZ5X0W9K02S', 'John Doe', '+91 9876543213', 'BUYER');
INSERT INTO users (id, email, password_hash, full_name, phone_number, role) VALUES (5, 'buyer.priya@gmail.com', '$2a$10$76g.p5nS7uS8.5/VzBqOvuM2FvN92X5T1G.L4P/8J3QZ5X0W9K02S', 'Priya Sharma', '+91 9876543214', 'BUYER');
INSERT INTO users (id, email, password_hash, full_name, phone_number, role) VALUES (6, 'rasheedhamart_admin@gmail.com', '$2a$10$pD9d0r05GKYE8DPJ0c/qyufNdb00s9SprexayZH3TxyaI4P2XVOhS', 'System Administrator', '+91 9876543219', 'ADMIN');
ALTER TABLE users ALTER COLUMN id RESTART WITH 7;

INSERT INTO categories (id, name, description) VALUES (1, 'Electronics', 'Smartphones, audio gear, and gadgets');
INSERT INTO categories (id, name, description) VALUES (2, 'Fashion', 'Apparel, traditional wear, and accessories');
INSERT INTO categories (id, name, description) VALUES (3, 'Groceries', 'Organic spices, teas, and food products');
INSERT INTO categories (id, name, description) VALUES (4, 'Home & Living', 'Eco-friendly bottles, decor, and cookware');
ALTER TABLE categories ALTER COLUMN id RESTART WITH 5;

INSERT INTO products (id, seller_id, name, description, price, cost_price, stock_quantity, category, image_url) VALUES (1, 2, 'Organic Spices Pack', 'Premium quality authentic South Indian organic spice gift box.', 499.00, 300.00, 50, 'Groceries', 'https://images.unsplash.com/photo-1596040033229-a9821ebd058d?auto=format&fit=crop&w=500&q=80');
INSERT INTO products (id, seller_id, name, description, price, cost_price, stock_quantity, category, image_url) VALUES (2, 2, 'Handcrafted Cotton Saree', 'Traditional handmade pure cotton saree with elegant zari border.', 1850.00, 1100.00, 20, 'Fashion', 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&w=500&q=80');
INSERT INTO products (id, seller_id, name, description, price, cost_price, stock_quantity, category, image_url) VALUES (3, 3, 'Wireless Headphones', 'High fidelity audio with 30-hour battery life and active noise reduction.', 3499.00, 2200.00, 15, 'Electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=500&q=80');
INSERT INTO products (id, seller_id, name, description, price, cost_price, stock_quantity, category, image_url) VALUES (4, 3, 'Smart Fitness Watch v2', 'Track heart rate, sleep, steps and oxygen levels with HD AMOLED display.', 2299.00, 1500.00, 30, 'Electronics', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=500&q=80');
INSERT INTO products (id, seller_id, name, description, price, cost_price, stock_quantity, category, image_url) VALUES (5, 2, 'Bamboo Water Bottle', 'BPA-free stainless steel bottle with natural bamboo outer insulation.', 799.00, 450.00, 100, 'Home & Living', 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?auto=format&fit=crop&w=500&q=80');
ALTER TABLE products ALTER COLUMN id RESTART WITH 6;

INSERT INTO addresses (id, user_id, full_name, phone_number, address_line1, address_line2, city, state, postal_code, country, is_default) VALUES (1, 4, 'John Doe', '+91 9876543213', '123 Park Street', 'Suite 4B', 'Chennai', 'Tamil Nadu', '600001', 'India', TRUE);
INSERT INTO addresses (id, user_id, full_name, phone_number, address_line1, address_line2, city, state, postal_code, country, is_default) VALUES (2, 5, 'Priya Sharma', '+91 9876543214', '45 MG Road', 'Near City Mall', 'Bengaluru', 'Karnataka', '560001', 'India', TRUE);
ALTER TABLE addresses ALTER COLUMN id RESTART WITH 3;

INSERT INTO orders (id, buyer_id, shipping_address_id, total_amount, status) VALUES (1, 4, 1, 3998.00, 'DELIVERED');
INSERT INTO orders (id, buyer_id, shipping_address_id, total_amount, status) VALUES (2, 5, 2, 2299.00, 'SHIPPED');
ALTER TABLE orders ALTER COLUMN id RESTART WITH 3;

INSERT INTO order_address_snapshots (id, order_id, full_name, phone_number, address_line1, address_line2, city, state, postal_code, country) VALUES (1, 1, 'John Doe', '+91 9876543213', '123 Park Street', 'Suite 4B', 'Chennai', 'Tamil Nadu', '600001', 'India');
INSERT INTO order_address_snapshots (id, order_id, full_name, phone_number, address_line1, address_line2, city, state, postal_code, country) VALUES (2, 2, 'Priya Sharma', '+91 9876543214', '45 MG Road', 'Near City Mall', 'Bengaluru', 'Karnataka', '560001', 'India');

INSERT INTO order_items (id, order_id, product_id, seller_id, quantity, unit_price, unit_cost) VALUES (1, 1, 1, 2, 1, 499.00, 300.00);
INSERT INTO order_items (id, order_id, product_id, seller_id, quantity, unit_price, unit_cost) VALUES (2, 1, 3, 3, 1, 3499.00, 2200.00);
INSERT INTO order_items (id, order_id, product_id, seller_id, quantity, unit_price, unit_cost) VALUES (3, 2, 4, 3, 1, 2299.00, 1500.00);
ALTER TABLE order_items ALTER COLUMN id RESTART WITH 4;

INSERT INTO reviews (id, product_id, buyer_id, rating, comment) VALUES (1, 1, 4, 5, 'Authentic aroma and excellent freshness! Highly recommended.');
INSERT INTO reviews (id, product_id, buyer_id, rating, comment) VALUES (2, 3, 4, 4, 'Great sound quality and battery life. Fast delivery by seller.');
ALTER TABLE reviews ALTER COLUMN id RESTART WITH 3;

INSERT INTO promotions (id, title, description, banner_url, discount_percentage, target_category, is_active) VALUES (1, 'Festive Tech Clearance', 'Get up to 20% off on top electronics and wearable tech.', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=1000&q=80', 20, 'Electronics', TRUE);
INSERT INTO promotions (id, title, description, banner_url, discount_percentage, target_category, is_active) VALUES (2, 'Organic Harvest Festival', 'Special discounts on fresh organic spices and teas.', 'https://images.unsplash.com/photo-1596040033229-a9821ebd058d?auto=format&fit=crop&w=1000&q=80', 15, 'Groceries', TRUE);
ALTER TABLE promotions ALTER COLUMN id RESTART WITH 3;
