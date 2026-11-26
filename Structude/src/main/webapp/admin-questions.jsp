<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%
if(session==null||!Boolean.TRUE.equals(session.getAttribute("admin"))){response.sendRedirect(request.getContextPath()+"/admin-login.jsp");return;}
List<String[]> questions=(List<String[]>)request.getAttribute("questions");
%>
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Manage Questions</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"></head>
<body class="bg-light"><div class="container py-4">
<div class="d-flex justify-content-between mb-4"><h2>Manage Questions</h2><a class="btn btn-outline-dark" href="admin-dashboard.jsp">Dashboard</a></div>
<%if(request.getAttribute("error")!=null){%><div class="alert alert-danger"><%=request.getAttribute("error")%></div><%}%>
<div class="card border-0 shadow-sm p-4 mb-4"><h5>Add Question</h5><form class="row g-2" method="post" action="admin-questions">
<input type="hidden" name="action" value="add"><div class="col-md-2"><input class="form-control" type="number" name="qno" placeholder="Q No." required></div>
<div class="col-md-8"><input class="form-control" name="title" maxlength="200" placeholder="Question title" required></div>
<div class="col-md-2"><button class="btn btn-primary w-100">Add</button></div></form></div>
<div class="table-responsive bg-white shadow-sm"><table class="table table-hover"><thead class="table-dark"><tr><th>No.</th><th>Question</th><th>Action</th></tr></thead><tbody>
<%if(questions!=null)for(String[] q:questions){%><tr><td><%=q[0]%></td><td><%=q[1]%></td><td><form method="post" action="admin-questions" onsubmit="return confirm('Delete this question?')"><input type="hidden" name="action" value="delete"><input type="hidden" name="qno" value="<%=q[0]%>"><button class="btn btn-sm btn-danger">Delete</button></form></td></tr><%}%>
</tbody></table></div></div></body></html>
