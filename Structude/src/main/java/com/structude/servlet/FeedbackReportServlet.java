package com.structude.servlet;

import com.structude.database.Database;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/feedback-report")
public class FeedbackReportServlet extends HttpServlet {
    private boolean isAdmin(HttpServletRequest r) {
        HttpSession s = r.getSession(false);
        return s != null && Boolean.TRUE.equals(s.getAttribute("admin"));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/admin-login.jsp");
            return;
        }

        String sem = request.getParameter("sem");
        String enrollment = request.getParameter("enrollment");
        List<String[]> rows = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT r.enrollment, st.name AS student_name, r.sem, " +
            "r.code, s.name AS subject_name, s.teacher, r.q1, r.q2, r.q3, " +
            "ROUND((r.q1+r.q2+r.q3)/3.0,2) AS average " +
            "FROM responce r " +
            "LEFT JOIN students st ON st.enrollment=r.enrollment " +
            "LEFT JOIN subjects s ON s.code=r.code WHERE 1=1");
        List<Object> params = new ArrayList<>();

        try {
            if (sem != null && !sem.isBlank()) {
                sql.append(" AND r.sem=?");
                params.add(Integer.parseInt(sem));
            }
            if (enrollment != null && !enrollment.isBlank()) {
                sql.append(" AND r.enrollment LIKE ?");
                params.add("%" + enrollment.trim() + "%");
            }
            sql.append(" ORDER BY r.sem, r.enrollment, r.code");

            try (Connection con = Database.connectDatabase();
                 PreparedStatement ps = con.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    Object p = params.get(i);
                    if (p instanceof Integer) ps.setInt(i + 1, (Integer)p);
                    else ps.setString(i + 1, p.toString());
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        rows.add(new String[]{
                            rs.getString("enrollment"),
                            rs.getString("student_name"),
                            String.valueOf(rs.getInt("sem")),
                            String.valueOf(rs.getInt("code")),
                            rs.getString("subject_name"),
                            rs.getString("teacher"),
                            String.valueOf(rs.getInt("q1")),
                            String.valueOf(rs.getInt("q2")),
                            String.valueOf(rs.getInt("q3")),
                            String.valueOf(rs.getDouble("average"))
                        });
                    }
                }
            }
            request.setAttribute("rows", rows);
        } catch (Exception e) {
            getServletContext().log("Feedback report failed", e);
            request.setAttribute("error", "Unable to generate feedback report.");
            request.setAttribute("rows", rows);
        }
        request.getRequestDispatcher("/feedback-report.jsp").forward(request, response);
    }
}
