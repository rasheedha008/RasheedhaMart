const express = require('express');
const session = require('express-session');
const bcrypt = require('bcryptjs');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 8080;

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(session({
    secret: 'rasheedhamart_secret_key_2026',
    resave: false,
    saveUninitialized: false,
    cookie: { maxAge: 1800000 } // 30 mins
}));

// In-Memory Database with Pre-Populated Seed Data
const defaultHash = bcrypt.hashSync('password123', 10);

const users = [
    { id: 1, email: 'admin@rasheedhamart.com', passwordHash: defaultHash, fullName: 'System Administrator', role: 'ADMIN', createdAt: new Date() },
    { id: 2, email: 'seller.rasheedha@rasheedhamart.com', passwordHash: defaultHash, fullName: 'Rasheedha Seller Store', role: 'SELLER', createdAt: new Date() },
    { id: 3, email: 'seller.tech@rasheedhamart.com', passwordHash: defaultHash, fullName: 'TechZone Electronics', role: 'SELLER', createdAt: new Date() },
    { id: 4, email: 'buyer.john@gmail.com', passwordHash: defaultHash, fullName: 'John Doe', role: 'BUYER', createdAt: new Date() },
    { id: 5, email: 'buyer.priya@gmail.com', passwordHash: defaultHash, fullName: 'Priya Sharma', role: 'BUYER', createdAt: new Date() }
];

const products = [
    { id: 1, sellerId: 2, name: 'Organic Spices Pack', description: 'Premium quality authentic South Indian organic spice gift box.', price: 499.00, stockQuantity: 50, category: 'Groceries', imageUrl: 'https://images.unsplash.com/photo-1596040033229-a9821ebd058d?auto=format&fit=crop&w=500&q=80', createdAt: new Date() },
    { id: 2, sellerId: 2, name: 'Handcrafted Cotton Saree', description: 'Traditional handmade pure cotton saree with elegant zari border.', price: 1850.00, stockQuantity: 20, category: 'Fashion', imageUrl: 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&w=500&q=80', createdAt: new Date() },
    { id: 3, sellerId: 3, name: 'Wireless Noise Cancelling Headphones', description: 'High fidelity audio with 30-hour battery life and active noise reduction.', price: 3499.00, stockQuantity: 15, category: 'Electronics', imageUrl: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=500&q=80', createdAt: new Date() },
    { id: 4, sellerId: 3, name: 'Smart Fitness Watch v2', description: 'Track heart rate, sleep, steps and oxygen levels with HD AMOLED display.', price: 2299.00, stockQuantity: 30, category: 'Electronics', imageUrl: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=500&q=80', createdAt: new Date() },
    { id: 5, sellerId: 2, name: 'Eco-Friendly Bamboo Water Bottle', description: 'BPA-free stainless steel bottle with natural bamboo outer insulation.', price: 799.00, stockQuantity: 100, category: 'Home & Living', imageUrl: 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?auto=format&fit=crop&w=500&q=80', createdAt: new Date() }
];

const cartItems = [];
const orders = [
    { id: 1, buyerId: 4, totalAmount: 3998.00, status: 'DELIVERED', createdAt: new Date() },
    { id: 2, buyerId: 5, totalAmount: 2299.00, status: 'SHIPPED', createdAt: new Date() }
];

const orderItems = [
    { id: 1, orderId: 1, productId: 1, sellerId: 2, quantity: 1, unitPrice: 499.00 },
    { id: 2, orderId: 1, productId: 3, sellerId: 3, quantity: 1, unitPrice: 3499.00 },
    { id: 3, orderId: 2, productId: 4, sellerId: 3, quantity: 1, unitPrice: 2299.00 }
];

const reviews = [
    { id: 1, productId: 1, buyerId: 4, rating: 5, comment: 'Authentic aroma and excellent freshness! Highly recommended.', createdAt: new Date() },
    { id: 2, productId: 3, buyerId: 4, rating: 4, comment: 'Great sound quality and battery life. Fast delivery by seller.', createdAt: new Date() }
];

let userSeq = 6;
let productSeq = 6;
let orderSeq = 3;

// Serve Web Static Files
app.use(express.static(path.join(__dirname, 'src/main/resources/web')));
app.use(express.static(path.join(__dirname, 'public')));

// Helper Response Format
function success(data) {
    return { success: true, data, error: null, timestamp: new Date().toISOString() };
}
function error(msg) {
    return { success: false, data: null, error: msg, timestamp: new Date().toISOString() };
}
function sanitizeUser(u) {
    return { id: u.id, email: u.email, fullName: u.fullName, role: u.role, createdAt: u.createdAt };
}

// HEALTH CHECK ENDPOINT
app.get('/api/v1/health', (req, res) => {
    res.json({ status: 'UP', db: 'UP' });
});

// AUTH ROUTES
app.post('/api/v1/auth/register', (req, res) => {
    const { email, password, fullName, role } = req.body;
    if (!email || !password || !fullName) return res.status(400).json(error('All fields are required.'));
    if (users.find(u => u.email.toLowerCase() === email.toLowerCase())) {
        return res.status(400).json(error('Email already registered.'));
    }
    const newUser = {
        id: userSeq++,
        email: email.trim().toLowerCase(),
        passwordHash: bcrypt.hashSync(password, 10),
        fullName: fullName.trim(),
        role: (role || 'BUYER').toUpperCase(),
        createdAt: new Date()
    };
    users.push(newUser);
    res.status(201).json(success(sanitizeUser(newUser)));
});

app.post('/api/v1/auth/login', (req, res) => {
    const { email, password } = req.body;
    const user = users.find(u => u.email.toLowerCase() === (email || '').toLowerCase());
    if (!user || !bcrypt.compareSync(password || '', user.passwordHash)) {
        return res.status(400).json(error('Invalid email or password.'));
    }
    req.session.user = user;
    res.json(success(sanitizeUser(user)));
});

app.post('/api/v1/auth/logout', (req, res) => {
    req.session.destroy();
    res.json(success('Logged out successfully.'));
});

app.get('/api/v1/auth/me', (req, res) => {
    if (req.session && req.session.user) {
        res.json(success(sanitizeUser(req.session.user)));
    } else {
        res.status(401).json(error('Not authenticated.'));
    }
});

// PRODUCT ROUTES
app.get('/api/v1/products/categories', (req, res) => {
    const cats = [...new Set(products.map(p => p.category))];
    res.json(success(cats));
});

app.get('/api/v1/products/search', (req, res) => {
    const { keyword, category, page = 1, pageSize = 10 } = req.query;
    let filtered = [...products];
    if (keyword && keyword.trim() !== '') {
        const kw = keyword.toLowerCase();
        filtered = filtered.filter(p => p.name.toLowerCase().includes(kw) || p.description.toLowerCase().includes(kw));
    }
    if (category && category !== 'ALL') {
        filtered = filtered.filter(p => p.category.toLowerCase() === category.toLowerCase());
    }

    const p = parseInt(page);
    const ps = parseInt(pageSize);
    const totalCount = filtered.length;
    const totalPages = Math.ceil(totalCount / ps);
    const paginated = filtered.slice((p - 1) * ps, p * ps);

    res.json(success({ products: paginated, totalCount, page: p, pageSize: ps, totalPages }));
});

app.get('/api/v1/products/seller/listings', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const sellerProds = products.filter(p => p.sellerId === req.session.user.id);
    res.json(success(sellerProds));
});

app.get('/api/v1/products/:id', (req, res) => {
    const p = products.find(prod => prod.id === parseInt(req.params.id));
    if (!p) return res.status(404).json(error('Product not found.'));
    res.json(success(p));
});

app.post('/api/v1/products', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const { name, category, price, stockQuantity, imageUrl, description } = req.body;
    const newProd = {
        id: productSeq++,
        sellerId: req.session.user.id,
        name,
        category,
        price: parseFloat(price),
        stockQuantity: parseInt(stockQuantity),
        imageUrl: imageUrl || '',
        description: description || '',
        createdAt: new Date()
    };
    products.push(newProd);
    res.status(201).json(success(newProd));
});

app.put('/api/v1/products', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const { id, name, category, price, stockQuantity, imageUrl, description } = req.body;
    const prod = products.find(p => p.id === parseInt(id));
    if (!prod) return res.status(404).json(error('Product not found.'));
    prod.name = name;
    prod.category = category;
    prod.price = parseFloat(price);
    prod.stockQuantity = parseInt(stockQuantity);
    prod.imageUrl = imageUrl;
    prod.description = description;
    res.json(success(prod));
});

app.delete('/api/v1/products/:id', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const idx = products.findIndex(p => p.id === parseInt(req.params.id));
    if (idx !== -1) products.splice(idx, 1);
    res.json(success(true));
});

// CART ROUTES
app.get('/api/v1/cart', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const buyerCart = cartItems.filter(c => c.buyerId === req.session.user.id).map(c => {
        const p = products.find(prod => prod.id === c.productId);
        return { ...c, product: p };
    });
    const runningTotal = buyerCart.reduce((sum, item) => sum + (item.product ? item.product.price * item.quantity : 0), 0);
    res.json(success({ items: buyerCart, itemCount: buyerCart.length, runningTotal }));
});

app.post('/api/v1/cart', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const { productId, quantity } = req.body;
    const pId = parseInt(productId);
    const qty = parseInt(quantity);
    let item = cartItems.find(c => c.buyerId === req.session.user.id && c.productId === pId);
    if (item) {
        item.quantity += qty;
    } else {
        item = { id: cartItems.length + 1, buyerId: req.session.user.id, productId: pId, quantity: qty };
        cartItems.push(item);
    }
    res.json(success(item));
});

app.put('/api/v1/cart', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const { productId, quantity } = req.body;
    const pId = parseInt(productId);
    const qty = parseInt(quantity);
    const idx = cartItems.findIndex(c => c.buyerId === req.session.user.id && c.productId === pId);
    if (qty <= 0 && idx !== -1) {
        cartItems.splice(idx, 1);
        return res.json(success(null));
    }
    if (idx !== -1) cartItems[idx].quantity = qty;
    res.json(success(cartItems[idx]));
});

app.delete('/api/v1/cart/:productId', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const pId = parseInt(req.params.productId);
    const idx = cartItems.findIndex(c => c.buyerId === req.session.user.id && c.productId === pId);
    if (idx !== -1) cartItems.splice(idx, 1);
    res.json(success(true));
});

// CHECKOUT ROUTE
app.post('/api/v1/checkout', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const buyerCart = cartItems.filter(c => c.buyerId === req.session.user.id);
    if (buyerCart.length === 0) return res.status(400).json(error('Cart is empty.'));

    let total = 0;
    const oItems = [];
    const newOrder = { id: orderSeq++, buyerId: req.session.user.id, totalAmount: 0, status: 'PENDING', createdAt: new Date(), items: [] };

    for (const c of buyerCart) {
        const p = products.find(prod => prod.id === c.productId);
        if (p) {
            p.stockQuantity = Math.max(0, p.stockQuantity - c.quantity);
            const sub = p.price * c.quantity;
            total += sub;
            oItems.push({ id: orderItems.length + 1, orderId: newOrder.id, productId: p.id, sellerId: p.sellerId, quantity: c.quantity, unitPrice: p.price, product: p });
        }
    }

    newOrder.totalAmount = total;
    newOrder.items = oItems;
    orders.push(newOrder);

    // Clear buyer cart
    for (let i = cartItems.length - 1; i >= 0; i--) {
        if (cartItems[i].buyerId === req.session.user.id) cartItems.splice(i, 1);
    }

    res.status(201).json(success(newOrder));
});

// ORDER ROUTES
app.get('/api/v1/orders', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const buyerOrders = orders.filter(o => o.buyerId === req.session.user.id);
    res.json(success(buyerOrders));
});

app.get('/api/v1/orders/seller', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const sellerOrders = orders.filter(o => o.items.some(i => i.sellerId === req.session.user.id));
    res.json(success(sellerOrders));
});

app.put('/api/v1/orders', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const { orderId, status } = req.body;
    const order = orders.find(o => o.id === parseInt(orderId));
    if (!order) return res.status(404).json(error('Order not found.'));
    order.status = status.toUpperCase();
    res.json(success(order));
});

// REVIEWS
app.get('/api/v1/reviews', (req, res) => {
    const pId = parseInt(req.query.productId);
    const pReviews = reviews.filter(r => r.productId === pId).map(r => {
        const u = users.find(user => user.id === r.buyerId);
        return { ...r, buyerName: u ? u.fullName : 'Verified Buyer' };
    });
    const avg = pReviews.length > 0 ? pReviews.reduce((sum, r) => sum + r.rating, 0) / pReviews.length : 0;
    res.json(success({ reviews: pReviews, averageRating: avg, reviewCount: pReviews.length }));
});

app.post('/api/v1/reviews', (req, res) => {
    if (!req.session.user) return res.status(401).json(error('Authentication required.'));
    const { productId, rating, comment } = req.body;
    const newReview = { id: reviews.length + 1, productId: parseInt(productId), buyerId: req.session.user.id, rating: parseInt(rating), comment, createdAt: new Date() };
    reviews.push(newReview);
    res.status(201).json(success(newReview));
});

// ADMIN ROUTES
app.get('/api/v1/admin/users', (req, res) => {
    if (!req.session.user || req.session.user.role !== 'ADMIN') return res.status(403).json(error('Admin role required.'));
    res.json(success(users.map(sanitizeUser)));
});

app.get('/api/v1/admin/orders', (req, res) => {
    if (!req.session.user || req.session.user.role !== 'ADMIN') return res.status(403).json(error('Admin role required.'));
    res.json(success(orders));
});

app.delete('/api/v1/admin/user/:id', (req, res) => {
    if (!req.session.user || req.session.user.role !== 'ADMIN') return res.status(403).json(error('Admin role required.'));
    const idx = users.findIndex(u => u.id === parseInt(req.params.id));
    if (idx !== -1) users.splice(idx, 1);
    res.json(success(true));
});

// Fallback index.html
app.get('*', (req, res) => {
    res.sendFile(path.join(__dirname, 'src/main/resources/web/index.html'));
});

app.listen(PORT, () => {
    console.log(`==================================================`);
    console.log(` RasheedhaMart Node.js Server is running!`);
    console.log(` Web Portal: http://localhost:${PORT}`);
    console.log(` Health API: http://localhost:${PORT}/api/v1/health`);
    console.log(`==================================================`);
});
