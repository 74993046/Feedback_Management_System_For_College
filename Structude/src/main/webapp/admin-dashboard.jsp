<%@ page contentType="text/html;charset=UTF-8" %>
<%
if (session == null || !Boolean.TRUE.equals(session.getAttribute("admin"))) {
    response.sendRedirect(request.getContextPath()+"/admin-login.jsp"); return;
}
%>
<!DOCTYPE html>
<html><head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Admin Dashboard - STRUCTUDE</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<style>.card{transition:.2s}.card:hover{transform:translateY(-3px)}</style>
</head><body class="bg-light">
<nav class="navbar navbar-dark bg-dark"><div class="container">
<span class="navbar-brand fw-bold">DEPT. ADMIN</span>
<a class="btn btn-outline-light btn-sm" href="<%=request.getContextPath()%>/admin-logout">Logout</a>
</div></nav>
<div class="container py-5"><h2 class="mb-4">Admin Dashboard</h2>
<div class="row g-4">
<div class="col-md-6 col-lg-3"><a class="text-decoration-none" href="<%=request.getContextPath()%>/admin-students">
<div class="card shadow-sm border-0 p-4 h-100"><h4>Students</h4><p class="text-muted">Add, view and delete students.</p></div></a></div>
<div class="col-md-6 col-lg-3"><a class="text-decoration-none" href="<%=request.getContextPath()%>/admin-questions">
<div class="card shadow-sm border-0 p-4 h-100"><h4>Questions</h4><p class="text-muted">Manage feedback questions.</p></div></a></div>
<div class="col-md-6 col-lg-3"><a class="text-decoration-none" href="<%=request.getContextPath()%>/admin-subjects">
<div class="card shadow-sm border-0 p-4 h-100"><h4>Subjects</h4><p class="text-muted">Assign or modify teachers.</p></div></a></div>
<div class="col-md-6 col-lg-3"><a class="text-decoration-none" href="<%=request.getContextPath()%>/feedback-report">
<div class="card shadow-sm border-0 p-4 h-100"><h4>Reports</h4><p class="text-muted">View and filter student feedback.</p></div></a></div>
<div class="col-md-4 mb-3">
    <div class="card shadow-sm h-100">
        <div class="card-body text-center">

            <h5 class="card-title">
                Teacher Feedback
            </h5>

            <p class="card-text">
                View subject-wise teacher feedback
                and download Excel reports.
            </p>

            <a href="<%=request.getContextPath()%>/teacher-feedback-report.jsp"
               class="btn btn-primary">
                View Report
            </a>

        </div>
    </div>
</div>
</div></div></body></html>
