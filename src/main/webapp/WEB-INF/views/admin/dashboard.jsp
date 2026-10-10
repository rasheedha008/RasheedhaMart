<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard — RasheedhaMart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        :root { --navy-dark: #0a192f; --sky-blue: #0284c7; --sky-accent: #38bdf8; }
        body { background-color: #f8fafc; font-family: 'Segoe UI', system-ui, sans-serif; }
        .sidebar { width: 260px; min-height: 100vh; background-color: var(--navy-dark); color: white; position: fixed; }
        .sidebar .nav-link { color: #cbd5e1; padding: 0.8rem 1.2rem; border-radius: 0.5rem; margin-bottom: 0.3rem; }
        .sidebar .nav-link.active, .sidebar .nav-link:hover { background-color: var(--sky-blue); color: white; }
        .main-content { margin-left: 260px; padding: 2rem; }
        .metric-card { border-radius: 1rem; border: none; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
    </style>
</head>
<body>
    <div class="sidebar p-3">
        <h4 class="fw-bold text-info mb-4 ps-2"><i class="bi bi-shield-lock-fill me-2"></i> Rasheedha Admin</h4>
        <nav class="nav flex-column">
            <a class="nav-link active" href="${pageContext.request.contextPath}/admin/dashboard"><i class="bi bi-speedometer2 me-2"></i> Dashboard</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/users"><i class="bi bi-people-fill me-2"></i> User Management</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/products"><i class="bi bi-box-seam me-2"></i> Product Moderation</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/orders"><i class="bi bi-bag-check me-2"></i> Global Orders</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/marketing"><i class="bi bi-megaphone me-2"></i> Marketing Banners</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/analytics"><i class="bi bi-graph-up-arrow me-2"></i> Financial Analytics</a>
            <hr class="text-secondary my-3">
            <a class="nav-link text-danger" href="${pageContext.request.contextPath}/"><i class="bi bi-house-door me-2"></i> Exit to Portal</a>
        </nav>
    </div>

    <div class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold">Platform Overview & Metrics</h2>
            <span class="badge bg-navy text-white px-3 py-2 rounded-pill fs-6"><i class="bi bi-check-circle-fill text-success me-1"></i> System Active</span>
        </div>

        <div class="row g-4 mb-4">
            <div class="col-md-3">
                <div class="card metric-card p-4 bg-white border-start border-primary border-4">
                    <span class="text-secondary small fw-bold">TOTAL BUYERS</span>
                    <h2 class="fw-bold text-primary mb-0 mt-1">${metrics.totalBuyers != null ? metrics.totalBuyers : 0}</h2>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card metric-card p-4 bg-white border-start border-info border-4">
                    <span class="text-secondary small fw-bold">TOTAL SELLERS</span>
                    <h2 class="fw-bold text-info mb-0 mt-1">${metrics.totalSellers != null ? metrics.totalSellers : 0}</h2>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card metric-card p-4 bg-white border-start border-warning border-4">
                    <span class="text-secondary small fw-bold">TOTAL PRODUCTS</span>
                    <h2 class="fw-bold text-warning mb-0 mt-1">${metrics.totalProducts != null ? metrics.totalProducts : 0}</h2>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card metric-card p-4 bg-white border-start border-success border-4">
                    <span class="text-secondary small fw-bold">GROSS REVENUE</span>
                    <h2 class="fw-bold text-success mb-0 mt-1">₹${metrics.totalRevenue != null ? metrics.totalRevenue : 0}</h2>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
