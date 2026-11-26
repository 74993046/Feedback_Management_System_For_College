<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%
if (session == null || !Boolean.TRUE.equals(session.getAttribute("admin"))) { response.sendRedirect(request.getContextPath()+"/admin-login.jsp"); return; }
List<String[]> students=(List<String[]>)request.getAttribute("students");
%>
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Manage Students</title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"></head>
<body class="bg-light"><div class="container py-4">
<div class="d-flex justify-content-between mb-4"><h2>Manage Students</h2><a class="btn btn-outline-dark" href="admin-dashboard.jsp">Dashboard</a></div>
<%if(request.getAttribute("error")!=null){%><div class="alert alert-danger"><%=request.getAttribute("error")%></div><%}%>
<div class="card border-0 shadow-sm p-4 mb-4"><h5>Add Student</h5>
<form class="row g-2" method="post" action="admin-students">
<input type="hidden" name="action" value="add"><div class="col-md-3"><input class="form-control" name="enrollment" placeholder="Enrollment" required></div>
<div class="col-md-3"><input class="form-control" name="name" placeholder="Name" maxlength="30" required></div>
<div class="col-md-3"><input class="form-control" name="mono" placeholder="Mobile"></div>
<div class="col-md-3"><button class="btn btn-primary w-100">Add Student</button></div>
</form></div>
<div class="table-responsive bg-white shadow-sm"><table class="table table-hover mb-0"><thead class="table-dark"><tr><th>Enrollment</th><th>Name</th><th>Mobile</th><th>Action</th></tr></thead><tbody>
<%if(students!=null) for(String[] s:students){%><tr><td><%=s[0]%></td><td><%=s[1]%></td><td><%=s[2]==null?"":s[2]%></td><td>
<form method="post" action="admin-students" onsubmit="return confirm('Delete this student?')"><input type="hidden" name="action" value="delete"><input type="hidden" name="enrollment" value="<%=s[0]%>"><button class="btn btn-sm btn-danger">Delete</button></form>
</td></tr><%}%></tbody></table></div></div></body></html>
