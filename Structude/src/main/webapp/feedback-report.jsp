<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Feedback Report - STRUCTUDE</title>

    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">

    <style>

        body {
            background-color: #f5f6fa;
        }

        .container {
            margin-top: 30px;
        }

        .card {
            border: none;
            border-radius: 12px;
            box-shadow: 0 3px 12px rgba(0,0,0,0.08);
        }

        .table th {
            white-space: nowrap;
        }

        .table td {
            vertical-align: middle;
        }

        .student-card {
            margin-bottom: 20px;
        }

        .download-btn {
            white-space: nowrap;
        }

    </style>

</head>

<body>

<div class="container">

    <!-- Page Header -->

    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>

            <h2 class="fw-bold">
                Student Feedback Report
            </h2>

            <p class="text-muted mb-0">
                View and download student feedback reports
            </p>

        </div>

        <a
            href="<%=request.getContextPath()%>/admin-dashboard.jsp"
            class="btn btn-secondary">

            Back to Dashboard

        </a>

    </div>


    <!-- Filter Card -->

    <div class="card p-4 mb-4">

        <h5 class="fw-bold mb-3">
            Filter Report
        </h5>

        <form
            method="get"
            action="<%=request.getContextPath()%>/feedback-report">

            <div class="row g-3 align-items-end">

                <!-- Semester -->

                <div class="col-md-4">

                    <label class="form-label">
                        Semester
                    </label>

                    <select
                        name="sem"
                        class="form-select">

                        <option value="">
                            All Semesters
                        </option>

                        <%
                            String selectedSem =
                                request.getParameter("sem");

                            for (int i = 1; i <= 8; i++) {
                        %>

                            <option
                                value="<%=i%>"
                                <%=String.valueOf(i).equals(selectedSem)
                                    ? "selected"
                                    : ""%>>

                                Semester <%=i%>

                            </option>

                        <%
                            }
                        %>

                    </select>

                </div>


                <!-- Enrollment -->

                <div class="col-md-5">

                    <label class="form-label">
                        Enrollment Number
                    </label>

                    <input
                        type="text"
                        name="enrollment"
                        class="form-control"
                        placeholder="Enter enrollment number"
                        value="<%=request.getParameter("enrollment") != null
                            ? request.getParameter("enrollment")
                            : ""%>">

                </div>


                <!-- Button -->

                <div class="col-md-3">

                    <button
                        type="submit"
                        class="btn btn-primary w-100">

                        Apply Filter

                    </button>

                </div>

            </div>

        </form>

    </div>


    <%
        List<String[]> rows =
            (List<String[]>) request.getAttribute("rows");

        String error =
            (String) request.getAttribute("error");
    %>


    <!-- Error -->

    <%
        if (error != null) {
    %>

        <div class="alert alert-danger">

            <%=error%>

        </div>

    <%
        }
    %>


    <%
        if (rows != null && !rows.isEmpty()) {

            /*
             * Get unique enrollment numbers.
             * LinkedHashSet keeps the original order.
             */

            Set<String> enrollments =
                new LinkedHashSet<>();

            for (String[] row : rows) {

                if (row != null &&
                    row.length > 0 &&
                    row[0] != null &&
                    !row[0].isBlank()) {

                    enrollments.add(row[0]);
                }
            }
    %>


    <!-- Download Individual Student Reports -->

    <div class="card p-4 student-card">

        <div class="d-flex justify-content-between
                    align-items-center mb-3">

            <h5 class="fw-bold mb-0">

                Download Student Reports

            </h5>

            <span class="badge bg-primary">

                <%=enrollments.size()%> Student(s)

            </span>

        </div>


        <div class="table-responsive">

            <table class="table table-bordered
                          table-hover align-middle mb-0">

                <thead class="table-dark">

                    <tr>

                        <th>
                            #
                        </th>

                        <th>
                            Enrollment No
                        </th>

                        <th>
                            Student Name
                        </th>

                        <th>
                            Download
                        </th>

                    </tr>

                </thead>

                <tbody>

                <%
                    int studentNo = 1;

                    for (String enrollment : enrollments) {

                        String studentName = "";

                        /*
                         * Find student name from report rows.
                         */
                        for (String[] row : rows) {

                            if (row.length > 1 &&
                                enrollment.equals(row[0])) {

                                studentName =
                                    row[1];

                                break;
                            }
                        }
                %>

                    <tr>

                        <td>
                            <%=studentNo++%>
                        </td>

                        <td>
                            <strong>
                                <%=enrollment%>
                            </strong>
                        </td>

                        <td>
                            <%=studentName != null
                                ? studentName
                                : ""%>
                        </td>

                        <td>

                            <a
                                href="<%=request.getContextPath()%>/student-feedback-pdf?enrollment=<%=URLEncoder.encode(enrollment, StandardCharsets.UTF_8)%>"
                                class="btn btn-success btn-sm download-btn">

                                Download PDF

                            </a>

                        </td>

                    </tr>

                <%
                    }
                %>

                </tbody>

            </table>

        </div>

    </div>


    <!-- Detailed Feedback -->

    <div class="card p-4">

        <h5 class="fw-bold mb-3">

            Detailed Feedback

        </h5>

        <div class="table-responsive">

            <table class="table table-bordered
                          table-hover align-middle">

                <thead class="table-dark">

                    <tr>

                        <th>
                            Enrollment
                        </th>

                        <th>
                            Student
                        </th>

                        <th>
                            Sem
                        </th>

                        <th>
                            Code
                        </th>

                        <th>
                            Subject
                        </th>

                        <th>
                            Teacher
                        </th>

                        <th>
                            Q1
                        </th>

                        <th>
                            Q2
                        </th>

                        <th>
                            Q3
                        </th>

                        <th>
                            Average
                        </th>

                    </tr>

                </thead>

                <tbody>

                <%
                    for (String[] row : rows) {
                %>

                    <tr>

                        <td>
                            <%=row[0]%>
                        </td>

                        <td>
                            <%=row[1]%>
                        </td>

                        <td>
                            <%=row[2]%>
                        </td>

                        <td>
                            <%=row[3]%>
                        </td>

                        <td>
                            <%=row[4]%>
                        </td>

                        <td>
                            <%=row[5]%>
                        </td>

                        <td>
                            <%=row[6]%>
                        </td>

                        <td>
                            <%=row[7]%>
                        </td>

                        <td>
                            <%=row[8]%>
                        </td>

                        <td>
                            <strong>
                                <%=row[9]%>
                            </strong>
                        </td>

                    </tr>

                <%
                    }
                %>

                </tbody>

            </table>

        </div>

    </div>


    <%
        } else {
    %>

        <div class="alert alert-info">

            No feedback records found.

        </div>

    <%
        }
    %>

</div>


<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>

</body>

</html>