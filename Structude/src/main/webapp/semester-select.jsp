<%@ page contentType="text/html;charset=UTF-8" %>
<%
    if (session == null || session.getAttribute("student") == null) {
        response.sendRedirect(request.getContextPath() + "/student-login.html");
        return;
    }
    com.structude.model.Student student =
        (com.structude.model.Student) session.getAttribute("student");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Select Semester - STRUCTUDE</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <div>
      <h2>Welcome, <%= student.getName() %></h2>
      <p class="text-muted mb-0">Enrollment: <%= student.getEnrollment() %></p>
    </div>
  </div>
  <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
  <% } %>
  <div class="card shadow-sm border-0">
    <div class="card-body p-4">
      <h4 class="mb-3">Select Semester</h4>
      <form action="<%= request.getContextPath() %>/display-subjects" method="post">
        <select name="sem" class="form-select form-select-lg mb-3" required>
          <option value="">Choose semester</option>
          <% for (int i = 1; i <= 6; i++) { %>
            <option value="<%= i %>">Semester <%= i %></option>
          <% } %>
        </select>
        <button class="btn btn-primary">View Subjects</button>
      </form>
    </div>
  </div>
</div>
</body>
</html>
