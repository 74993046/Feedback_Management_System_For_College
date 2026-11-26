<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.structude.model.Subject" %>
<%
    if (session == null || session.getAttribute("student") == null) {
        response.sendRedirect(request.getContextPath() + "/student-login.html");
        return;
    }
    Integer semester = (Integer) request.getAttribute("semester");
    if (semester == null) semester = (Integer) session.getAttribute("semester");
    List<Subject> subjects = (List<Subject>) request.getAttribute("subjects");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Subjects - STRUCTUDE</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <div>
      <h2>Semester <%= semester %> Subjects</h2>
      <p class="text-muted">Select a subject to submit feedback.</p>
    </div>
    <a class="btn btn-outline-primary" href="<%= request.getContextPath() %>/semester-select.jsp">Change Semester</a>
  </div>

  <% if ("1".equals(request.getParameter("success"))) { %>
    <div class="alert alert-success">Feedback submitted successfully.</div>
  <% } %>

  <% if (subjects == null || subjects.isEmpty()) { %>
    <div class="alert alert-info">No subjects found for this semester.</div>
  <% } else { %>
    <div class="table-responsive bg-white rounded shadow-sm">
      <table class="table table-hover align-middle mb-0">
        <thead class="table-dark">
          <tr><th>Code</th><th>Subject</th><th>Teacher</th><th>Action</th></tr>
        </thead>
        <tbody>
        <% for (Subject s : subjects) { %>
          <tr>
            <td><%= s.getCode() %></td>
            <td><%= s.getName() %></td>
            <td><%= s.getTeacher() == null ? "" : s.getTeacher() %></td>
            <td>
              <a class="btn btn-primary btn-sm"
                 href="<%= request.getContextPath() %>/feedback-form.jsp?code=<%= s.getCode() %>&sem=<%= semester %>">
                 Give Feedback
              </a>
            </td>
          </tr>
        <% } %>
        </tbody>
      </table>
    </div>
  <% } %>
</div>
</body>
</html>
