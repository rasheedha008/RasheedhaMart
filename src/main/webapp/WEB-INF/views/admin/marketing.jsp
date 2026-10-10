<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Marketing Campaigns & Banners — RasheedhaAdmin</title>
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
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/products"><i class="bi bi-box-seam me-2"></i> Product Moderation</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/orders"><i class="bi bi-bag-check me-2"></i> Global Orders</a>
            <a class="nav-link active" href="${pageContext.request.contextPath}/admin/marketing"><i class="bi bi-megaphone me-2"></i> Marketing Banners</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/analytics"><i class="bi bi-graph-up-arrow me-2"></i> Financial Analytics</a>
        </nav>
    </div>

    <div class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold">Marketing Banners & Campaigns</h2>
        </div>

        <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
            <h5 class="fw-bold mb-3">Create New Banner Campaign</h5>
            <form action="${pageContext.request.contextPath}/admin/marketing" method="POST" class="row g-3">
                <input type="hidden" name="action" value="create">
                <div class="col-md-6">
                    <label class="form-label">Banner Title</label>
                    <input type="text" name="title" class="form-control" placeholder="Festive Season Offer..." required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Banner Image URL</label>
                    <input type="url" name="bannerUrl" class="form-control" placeholder="https://images.unsplash.com/..." required>
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-primary rounded-pill px-4"><i class="bi bi-plus-circle me-1"></i> Publish Campaign</button>
                </div>
            </form>
        </div>

        <div class="card border-0 shadow-sm rounded-4 p-4">
            <h5 class="fw-bold mb-3">Active Promotional Banners</h5>
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr><th>ID</th><th>Campaign Title</th><th>Image Preview</th><th>Status</th><th class="text-end">Action</th></tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${promotions}">
                            <tr>
                                <td>${p.id}</td>
                                <td><strong>${p.title}</strong></td>
                                <td><img src="${p.bannerUrl}" style="max-height: 40px; border-radius: 4px;" alt="Banner"></td>
                                <td><span class="badge ${p.active ? 'bg-success' : 'bg-secondary'}">${p.active ? 'Active' : 'Inactive'}</span></td>
                                <td class="text-end">
                                    <form action="${pageContext.request.contextPath}/admin/marketing" method="POST" style="display:inline;">
                                        <input type="hidden" name="promoId" value="${p.id}">
                                        <input type="hidden" name="action" value="toggle">
                                        <button type="submit" class="btn btn-sm btn-outline-primary me-1">Toggle Active</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin/marketing" method="POST" style="display:inline;">
                                        <input type="hidden" name="promoId" value="${p.id}">
                                        <input type="hidden" name="action" value="delete">
                                        <button type="submit" class="btn btn-sm btn-outline-danger">Delete</button>
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
