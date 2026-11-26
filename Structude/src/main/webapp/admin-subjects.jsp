<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%
if(session==null||!Boolean.TRUE.equals(session.getAttribute("admin"))){response.sendRedirect(request.getContextPath()+"/admin-login.jsp");return;}
List<String[]> subjects=(List<String[]>)request.getAttribute("subjects");
%>
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Manage Subjects</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"></head>
<body class="bg-light"><div class="container py-4">
<div class="d-flex justify-content-between mb-4"><h2>Manage Subjects & Teachers</h2><a class="btn btn-outline-dark" href="admin-dashboard.jsp">Dashboard</a></div>
<%if(request.getAttribute("error")!=null){%><div class="alert alert-danger"><%=request.getAttribute("error")%></div><%}%>
<div class="card border-0 shadow-sm p-4 mb-4"><h5>Add Subject</h5><form class="row g-2" method="post" action="admin-subjects">
<input type="hidden" name="action" value="add"><div class="col-md-2"><input class="form-control" type="number" name="code" placeholder="Code" required></div>
<div class="col-md-3"><input class="form-control" name="name" placeholder="Subject name" required></div><div class="col-md-3"><input class="form-control" name="teacher" placeholder="Teacher"></div>
<div class="col-md-2"><input class="form-control" type="number" name="semester" min="1" max="12" placeholder="Semester" required></div><div class="col-md-2"><button class="btn btn-primary w-100">Add</button></div>
</form></div>
<div class="table-responsive bg-white shadow-sm"><table class="table table-hover align-middle"><thead class="table-dark"><tr><th>Code</th><th>Subject</th><th>Teacher</th><th>Sem</th><th>Update</th><th>Delete</th></tr></thead><tbody>
<%if(subjects!=null)for(String[] s:subjects){%><tr>
<form method="post" action="admin-subjects"><td><input type="hidden" name="action" value="update"><input type="number" class="form-control form-control-sm" name="code" value="<%=s[0]%>" readonly></td>
<td><input class="form-control form-control-sm" name="name" value="<%=s[1]%>" required></td><td><input class="form-control form-control-sm" name="teacher" value="<%=s[2]==null?"":s[2]%>"></td>
<td><input class="form-control form-control-sm" type="number" name="semester" value="<%=s[3]%>" min="1" max="12" required></td><td><button class="btn btn-sm btn-success">Save</button></td></form>
<td><form method="post" action="admin-subjects" onsubmit="return confirm('Delete this subject?')"><input type="hidden" name="action" value="delete"><input type="hidden" name="code" value="<%=s[0]%>"><button class="btn btn-sm btn-danger">Delete</button></form></td>
</tr><%}%></tbody></table></div></div></body></html>
