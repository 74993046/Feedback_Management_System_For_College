package com.structude.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.structude.database.Database;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/teacher-feedback-excel")
public class TeacherFeedbackExcelServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Admin login check
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect("admin-login.jsp");
            return;
        }

        String teacher = request.getParameter("teacher");

        if (teacher == null || teacher.trim().isEmpty()) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Teacher name is required."
            );
            return;
        }

        teacher = teacher.trim();

        String sql =
            "SELECT s.teacher, s.code, s.name AS subject_name, s.semester, " +
            "COUNT(r.enrollment) AS feedback_count, " +
            "ROUND(AVG((r.q1 + r.q2 + r.q3) / 3.0), 2) AS average " +
            "FROM subjects s " +
            "LEFT JOIN responce r ON r.code = s.code " +
            "WHERE s.teacher = ? " +
            "GROUP BY s.teacher, s.code, s.name, s.semester " +
            "ORDER BY s.semester, s.code";

        try (
            Connection con = Database.connectDatabase();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, teacher);

            try (ResultSet rs = ps.executeQuery();
                 Workbook workbook = new XSSFWorkbook()) {

                Sheet sheet = workbook.createSheet("Feedback Report");

                // =========================
                // Header Row
                // =========================

                Row headerRow = sheet.createRow(0);

                String[] headers = {
                    "Teacher",
                    "Subject Code",
                    "Subject Name",
                    "Semester",
                    "Feedback Count",
                    "Average Feedback"
                };

                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }

                // =========================
                // Data Rows
                // =========================

                int rowNumber = 1;

                while (rs.next()) {

                    Row row = sheet.createRow(rowNumber++);

                    // Teacher
                    row.createCell(0)
                       .setCellValue(rs.getString("teacher"));

                    // Subject Code
                    row.createCell(1)
                       .setCellValue(rs.getInt("code"));

                    // Subject Name
                    row.createCell(2)
                       .setCellValue(rs.getString("subject_name"));

                    // Semester
                    row.createCell(3)
                       .setCellValue(rs.getInt("semester"));

                    // Feedback Count
                    row.createCell(4)
                       .setCellValue(rs.getInt("feedback_count"));

                    // Average
                    Double average = null;

                    if (rs.getObject("average") != null) {
                        average = rs.getDouble("average");
                    }

                    if (average == null) {
                        row.createCell(5)
                           .setCellValue("No Feedback");
                    } else {
                        row.createCell(5)
                           .setCellValue(average);
                    }
                }

                // =========================
                // Freeze Header
                // =========================

                sheet.createFreezePane(0, 1);

                // =========================
                // Adjust Column Width
                // =========================

                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // =========================
                // File Name
                // =========================

                String safeTeacher = teacher.replaceAll(
                    "[^a-zA-Z0-9_-]",
                    "_"
                );

                String fileName =
                    safeTeacher + "_Feedback_Report.xlsx";

                // =========================
                // Response
                // =========================

                response.setContentType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                );

                response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=\"" + fileName + "\""
                );

                response.setHeader(
                    "Cache-Control",
                    "no-cache, no-store, must-revalidate"
                );

                response.setHeader(
                    "Pragma",
                    "no-cache"
                );

                response.setDateHeader(
                    "Expires",
                    0
                );

                try (OutputStream outputStream =
                         response.getOutputStream()) {

                    workbook.write(outputStream);
                    outputStream.flush();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            if (!response.isCommitted()) {
                response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to generate Excel report: "
                    + e.getMessage()
                );
            }
        }
    }
}