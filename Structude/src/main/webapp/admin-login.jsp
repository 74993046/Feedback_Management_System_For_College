<%@ page contentType="text/html;charset=UTF-8" %>
<%
if (session != null && Boolean.TRUE.equals(session.getAttribute("admin"))) {
    response.sendRedirect(request.getContextPath() + "/admin-dashboard.jsp");
    return;
}
%>
<!DOCTYPE html>
<html><head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Admin Login - STRUCTUDE</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head><body class="bg-light">
<div class="container min-vh-100 d-flex align-items-center justify-content-center">
<div class="card shadow border-0 p-4" style="max-width:430px;width:100%">
<h2 class="text-center">STRUCTUDE</h2><p class="text-center text-muted">Admin Login</p>
<% if(request.getAttribute("error") != null){ %><div class="alert alert-danger"><%=request.getAttribute("error")%></div><%}%>
<form method="post" action="<%=request.getContextPath()%>/admin-login">
<label class="form-label">Username</label><input class="form-control mb-3" name="username" required>
<label class="form-label">Password</label><input class="form-control mb-3" type="password" name="password" required>
<button class="btn btn-primary w-100">Login</button>
</form>
<div class="small text-muted mt-3">Default: admin / admin123</div>
</div></div></body></html>
