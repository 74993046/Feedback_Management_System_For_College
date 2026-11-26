package com.structude.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.structude.database.Database;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/student-feedback-pdf")
public class StudentFeedbackPDFServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Check admin login
        HttpSession session = request.getSession(false);

        if (session == null ||
            !Boolean.TRUE.equals(session.getAttribute("admin"))) {

            response.sendRedirect(
                request.getContextPath() + "/admin-login.jsp"
            );
            return;
        }

        String enrollment = request.getParameter("enrollment");

        if (enrollment == null || enrollment.trim().isEmpty()) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Enrollment number is required."
            );
            return;
        }

        enrollment = enrollment.trim();

        String sql =
            "SELECT st.enrollment, st.name AS student_name, " +
            "r.sem, r.code, s.name AS subject_name, s.teacher, " +
            "r.q1, r.q2, r.q3 " +
            "FROM responce r " +
            "LEFT JOIN students st ON st.enrollment = r.enrollment " +
            "LEFT JOIN subjects s ON s.code = r.code " +
            "WHERE r.enrollment = ? " +
            "ORDER BY r.sem, r.code";

        response.setContentType("application/pdf");

        String safeEnrollment =
            enrollment.replaceAll("[^a-zA-Z0-9_-]", "_");

        response.setHeader(
            "Content-Disposition",
            "attachment; filename=\"" +
            safeEnrollment + "_Feedback_Report.pdf\""
        );

        Document document =
            new Document(PageSize.A4.rotate());

        try {
            PdfWriter.getInstance(
                document,
                response.getOutputStream()
            );

            document.open();

            Font titleFont =
                new Font(
                    Font.HELVETICA,
                    20,
                    Font.BOLD
                );

            Font normalFont =
                new Font(
                    Font.HELVETICA,
                    11
                );

            Font boldFont =
                new Font(
                    Font.HELVETICA,
                    11,
                    Font.BOLD
                );

            // Title
            Paragraph title =
                new Paragraph(
                    "STRUCTUDE - STUDENT FEEDBACK REPORT",
                    titleFont
                );

            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            Connection con =
                Database.connectDatabase();

            if (con == null) {
                document.add(
                    new Paragraph(
                        "Database connection failed.",
                        boldFont
                    )
                );
                return;
            }

            try (PreparedStatement ps =
                    con.prepareStatement(sql)) {

                ps.setString(1, enrollment);

                try (ResultSet rs =
                        ps.executeQuery()) {

                    boolean found = false;

                    String studentName = "";
                    String studentEnrollment = "";

                    int subjectCount = 0;
                    double totalAverage = 0;

                    PdfPTable table =
                        new PdfPTable(9);

                    table.setWidthPercentage(100);

                    table.setWidths(
                        new float[]{
                            6, 9, 20, 18,
                            7, 7, 7, 10, 12
                        }
                    );

                    addHeader(table, "Sem");
                    addHeader(table, "Code");
                    addHeader(table, "Subject");
                    addHeader(table, "Teacher");
                    addHeader(table, "Q1");
                    addHeader(table, "Q2");
                    addHeader(table, "Q3");
                    addHeader(table, "Average");
                    addHeader(table, "Rating");

                    while (rs.next()) {

                        found = true;

                        studentEnrollment =
                            rs.getString("enrollment");

                        studentName =
                            rs.getString("student_name");

                        int q1 =
                            rs.getInt("q1");

                        int q2 =
                            rs.getInt("q2");

                        int q3 =
                            rs.getInt("q3");

                        double average =
                            (q1 + q2 + q3) / 3.0;

                        totalAverage += average;
                        subjectCount++;

                        addCell(
                            table,
                            String.valueOf(
                                rs.getInt("sem")
                            )
                        );

                        addCell(
                            table,
                            String.valueOf(
                                rs.getInt("code")
                            )
                        );

                        addCell(
                            table,
                            rs.getString("subject_name")
                        );

                        addCell(
                            table,
                            rs.getString("teacher")
                        );

                        addCell(
                            table,
                            String.valueOf(q1)
                        );

                        addCell(
                            table,
                            String.valueOf(q2)
                        );

                        addCell(
                            table,
                            String.valueOf(q3)
                        );

                        addCell(
                            table,
                            String.format(
                                "%.2f",
                                average
                            )
                        );

                        addCell(
                            table,
                            getRating(average)
                        );
                    }

                    if (!found) {

                        document.add(
                            new Paragraph(
                                "No feedback found for Enrollment No: "
                                + enrollment,
                                normalFont
                            )
                        );

                    } else {

                        // Student information
                        Paragraph studentInfo =
                            new Paragraph(
                                "Student Name: "
                                + studentName
                                + "\nEnrollment No: "
                                + studentEnrollment,
                                boldFont
                            );

                        document.add(studentInfo);

                        document.add(
                            new Paragraph(" ")
                        );

                        // Feedback table
                        document.add(table);

                        document.add(
                            new Paragraph(" ")
                        );

                        // Overall average
                        double overallAverage =
                            totalAverage / subjectCount;

                        Paragraph overall =
                            new Paragraph(
                                "Overall Average: "
                                + String.format(
                                    "%.2f / 5",
                                    overallAverage
                                )
                                + "\n"
                                + "Total Subjects: "
                                + subjectCount,
                                boldFont
                            );

                        document.add(overall);
                    }
                }

            } finally {
                con.close();
            }

        } catch (Exception e) {

            e.printStackTrace();

            if (!response.isCommitted()) {

                response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to generate PDF."
                );
            }

        } finally {

            if (document.isOpen()) {
                document.close();
            }
        }
    }

    private void addHeader(
            PdfPTable table,
            String text) {

        Font font =
            new Font(
                Font.HELVETICA,
                10,
                Font.BOLD
            );

        PdfPCell cell =
            new PdfPCell(
                new Phrase(text, font)
            );

        cell.setHorizontalAlignment(
            Element.ALIGN_CENTER
        );

        cell.setVerticalAlignment(
            Element.ALIGN_MIDDLE
        );

        table.addCell(cell);
    }

    private void addCell(
            PdfPTable table,
            String text) {

        Font font =
            new Font(
                Font.HELVETICA,
                9
            );

        PdfPCell cell =
            new PdfPCell(
                new Phrase(
                    text == null ? "" : text,
                    font
                )
            );

        cell.setHorizontalAlignment(
            Element.ALIGN_CENTER
        );

        cell.setVerticalAlignment(
            Element.ALIGN_MIDDLE
        );

        table.addCell(cell);
    }

    private String getRating(double average) {

        if (average >= 4.5) {
            return "Excellent";
        } else if (average >= 3.5) {
            return "Very Good";
        } else if (average >= 2.5) {
            return "Good";
        } else if (average >= 1.5) {
            return "Average";
        } else {
            return "Poor";
        }
    }
}