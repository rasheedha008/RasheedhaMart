<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Product Moderation — RasheedhaAdmin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        :root { --navy-dark: #0a192f; --sky-blue: #0284c7; }
        body { background-color: #f8fafc; font-family: 'Segoe UI', system-ui, sans-serif; }
        .sidebar { width: 260px; min-height: 100vh; background-color: var(--navy-dark); color: white; position: fixed; }
        .sidebar .nav-link { color: #cbd5e1; padding: 0.8rem 1.2rem; border-radius: 0.5rem; margin-bottom: 0.3rem; }
        .sidebar .nav-link.active, .sidebar .nav-link:hover { background-color: var(--sky-blue); color: white; }
        .main-content { margin-left: 260px; padding: 2rem; }
    </style>
</head>
<body>
    <div class="sidebar p-3">
        <h4 class="fw-bold text-info mb-4 ps-2"><i class="bi bi-shield-lock-fill me-2"></i> Rasheedha Admin</h4>
        <nav class="nav flex-column">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard"><i class="bi bi-speedometer2 me-2"></i> Dashboard</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/users"><i class="bi bi-people-fill me-2"></i> User Management</a>
            <a class="nav-link active" href="${pageContext.request.contextPath}/admin/products"><i class="bi bi-box-seam me-2"></i> Product Moderation</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/orders"><i class="bi bi-bag-check me-2"></i> Global Orders</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/marketing"><i class="bi bi-megaphone me-2"></i> Marketing Banners</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/analytics"><i class="bi bi-graph-up-arrow me-2"></i> Financial Analytics</a>
        </nav>
    </div>

    <div class="main-content">
        <h2 class="fw-bold mb-4">Platform Product Moderation</h2>
        <div class="card border-0 shadow-sm rounded-4 p-4">
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr><th>ID</th><th>Seller ID</th><th>Product Name</th><th>Category</th><th>Price</th><th>Stock</th><th>Status</th><th class="text-end">Action</th></tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td>${p.id}</td>
                                <td>Seller #${p.sellerId}</td>
                                <td><strong>${p.name}</strong></td>
                                <td>${p.category}</td>
                                <td>₹${p.price}</td>
                                <td>${p.stockQuantity}</td>
                                <td><span class="badge ${p.hidden ? 'bg-warning text-dark' : 'bg-success'}">${p.hidden ? 'Hidden' : 'Visible'}</span></td>
                                <td class="text-end">
                                    <form action="${pageContext.request.contextPath}/admin/products" method="POST" style="display:inline;">
                                        <input type="hidden" name="productId" value="${p.id}">
                                        <input type="hidden" name="action" value="toggleHide">
                                        <button type="submit" class="btn btn-sm ${p.hidden ? 'btn-outline-success' : 'btn-outline-warning'}">
                                            ${p.hidden ? 'Unhide Product' : 'Hide Product'}
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
