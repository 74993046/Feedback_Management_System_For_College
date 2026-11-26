<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.structude.database.Database" %>

<%
    // Admin session check
    Boolean admin = (Boolean) session.getAttribute("admin");

    if (admin == null || !admin) {
        response.sendRedirect("admin-login.jsp");
        return;
    }

    class TeacherData {
        String teacher;
        List<String[]> subjects = new ArrayList<>();

        TeacherData(String teacher) {
            this.teacher = teacher;
        }
    }

    Map<String, TeacherData> teacherMap = new LinkedHashMap<>();

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = Database.connectDatabase();

        String sql =
            "SELECT s.teacher, s.code, s.name, s.semester, " +
            "COUNT(r.enrollment) AS feedback_count, " +
            "ROUND(AVG((r.q1 + r.q2 + r.q3) / 3.0), 2) AS average " +
            "FROM subjects s " +
            "LEFT JOIN responce r ON s.code = r.code " +
            "GROUP BY s.teacher, s.code, s.name, s.semester " +
            "ORDER BY s.teacher, s.semester, s.code";

        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {

            String teacher = rs.getString("teacher");

            if (teacher == null || teacher.trim().isEmpty()) {
                teacher = "Not Assigned";
            }

            if (!teacherMap.containsKey(teacher)) {
                teacherMap.put(teacher, new TeacherData(teacher));
            }

            String average = rs.getString("average");

            if (average == null) {
                average = "No Feedback";
            }

            String[] subject = {
                rs.getString("code"),
                rs.getString("name"),
                String.valueOf(rs.getInt("semester")),
                String.valueOf(rs.getInt("feedback_count")),
                average
            };

            teacherMap.get(teacher).subjects.add(subject);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (con != null) con.close(); } catch (Exception e) {}
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Teacher Feedback Report</title>

    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">

    <style>

        body {
            background: #f5f7fa;
        }

        .container {
            margin-top: 40px;
        }

        .report-card {
            background: white;
            border-radius: 12px;
            padding: 25px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            margin-bottom: 25px;
        }

        .teacher-title {
            font-size: 22px;
            font-weight: 600;
            margin-bottom: 15px;
        }

        .download-btn {
            margin-top: 15px;
        }

        table {
            vertical-align: middle;
        }

        .back-btn {
            margin-bottom: 20px;
        }

    </style>

</head>

<body>

<div class="container">

    <div class="d-flex justify-content-between align-items-center mb-4">

        <h2>Teacher Overall Feedback Report</h2>

        <a href="admin-dashboard.jsp"
           class="btn btn-secondary">
            Back to Dashboard
        </a>

    </div>


    <%
        if (teacherMap.isEmpty()) {
    %>

        <div class="alert alert-warning">
            No teacher or subject records found.
        </div>

    <%
        } else {

            for (TeacherData teacherData : teacherMap.values()) {
    %>

        <div class="report-card">

            <div class="teacher-title">
                Teacher:
                <strong>
                    <%= teacherData.teacher %>
                </strong>
            </div>

            <div class="table-responsive">

                <table class="table table-bordered table-hover">

                    <thead class="table-dark">

                        <tr>
                            <th>Subject Code</th>
                            <th>Subject Name</th>
                            <th>Semester</th>
                            <th>Feedback Count</th>
                            <th>Average Feedback</th>
                        </tr>

                    </thead>

                    <tbody>

                    <%
                        for (String[] subject : teacherData.subjects) {
                    %>

                        <tr>

                            <td>
                                <%= subject[0] %>
                            </td>

                            <td>
                                <%= subject[1] %>
                            </td>

                            <td>
                                <%= subject[2] %>
                            </td>

                            <td>
                                <%= subject[3] %>
                            </td>

                            <td>
                                <strong>
                                    <%= subject[4] %>
                                </strong>
                            </td>

                        </tr>

                    <%
                        }
                    %>

                    </tbody>

                </table>

            </div>


            <!-- Download Excel for THIS teacher -->

            <a
                href="<%=request.getContextPath()%>/teacher-feedback-excel?teacher=<%=java.net.URLEncoder.encode(teacherData.teacher, "UTF-8")%>"
                class="btn btn-success download-btn">

                Download Excel

            </a>

        </div>

    <%
            }
        }
    %>

</div>

</body>

</html>