<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.structude.model.Question" %>
<%@ page import="com.structude.dao.QuestionDAO" %>
<%
    if (session == null || session.getAttribute("student") == null) {
        response.sendRedirect(request.getContextPath() + "/student-login.html");
        return;
    }

    String codeParam = request.getParameter("code");
    String semParam = request.getParameter("sem");
    if (codeParam == null || semParam == null) {
        response.sendRedirect(request.getContextPath() + "/semester-select.jsp");
        return;
    }

    int code = Integer.parseInt(codeParam);
    int sem = Integer.parseInt(semParam);
    List<Question> questions = new QuestionDAO().getAllQuestions();

    if (questions.size() < 3) {
        request.setAttribute("error", "At least 3 questions are required.");
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Feedback - STRUCTUDE</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-5">
  <div class="row justify-content-center">
    <div class="col-lg-8">
      <div class="card border-0 shadow-sm">
        <div class="card-body p-4">
          <h2 class="mb-1">Subject Feedback</h2>
          <p class="text-muted">Subject Code: <strong><%= code %></strong> | Semester: <strong><%= sem %></strong></p>

          <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
          <% } %>

          <% if (questions.size() >= 3) { %>
          <form action="<%= request.getContextPath() %>/submit-feedback" method="post">
            <input type="hidden" name="code" value="<%= code %>">
            <input type="hidden" name="sem" value="<%= sem %>">

            <%
              for (int i = 0; i < 3; i++) {
                Question q = questions.get(i);
            %>
              <div class="mb-4">
                <label class="form-label fw-semibold">
                  Q<%= i + 1 %>. <%= q.getTitle() %>
                </label>
                <select name="q<%= i + 1 %>" class="form-select" required>
                  <option value="">Select rating</option>
                  <option value="1">1 - Poor</option>
                  <option value="2">2 - Below Average</option>
                  <option value="3">3 - Average</option>
                  <option value="4">4 - Good</option>
                  <option value="5">5 - Excellent</option>
                </select>
              </div>
            <% } %>

            <button class="btn btn-success" type="submit">Submit Feedback</button>
            <a class="btn btn-outline-secondary"
               href="<%= request.getContextPath() %>/display-subjects?sem=<%= sem %>">Cancel</a>
          </form>
          <% } %>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
