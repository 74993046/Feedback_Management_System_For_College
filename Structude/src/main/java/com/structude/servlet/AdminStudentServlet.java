package com.structude.servlet;

import com.structude.database.Database;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/admin-students")
public class AdminStudentServlet extends HttpServlet {
    private boolean isAdmin(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && Boolean.TRUE.equals(s.getAttribute("admin"));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/admin-login.jsp");
            return;
        }
        try (Connection con = Database.connectDatabase();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT enrollment, name, mono FROM students ORDER BY enrollment");
             ResultSet rs = ps.executeQuery()) {
            request.setAttribute("students", rsToRows(rs));
            request.getRequestDispatcher("/admin-students.jsp").forward(request, response);
        } catch (Exception e) {
            getServletContext().log("Loading students failed", e);
            request.setAttribute("error", "Unable to load students.");
            request.getRequestDispatcher("/admin-students.jsp").forward(request, response);
        }
    }

    private java.util.List<String[]> rsToRows(ResultSet rs) throws SQLException {
        java.util.List<String[]> rows = new java.util.ArrayList<>();
        while (rs.next()) {
            rows.add(new String[] {
                rs.getString("enrollment"), rs.getString("name"), rs.getString("mono")
            });
        }
        return rows;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/admin-login.jsp");
            return;
        }
        String action = request.getParameter("action");
        try (Connection con = Database.connectDatabase()) {
            if ("add".equals(action)) {
                String sql = "INSERT INTO students(enrollment,name,mono) VALUES(?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, request.getParameter("enrollment").trim());
                    ps.setString(2, request.getParameter("name").trim());
                    ps.setString(3, request.getParameter("mono"));
                    ps.executeUpdate();
                }
            } else if ("delete".equals(action)) {
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM students WHERE enrollment=?")) {
                    ps.setString(1, request.getParameter("enrollment"));
                    ps.executeUpdate();
                }
            }
            response.sendRedirect(request.getContextPath() + "/admin-students?success=1");
        } catch (SQLIntegrityConstraintViolationException e) {
            request.setAttribute("error", "Enrollment already exists.");
            doGet(request, response);
        } catch (Exception e) {
            getServletContext().log("Student operation failed", e);
            request.setAttribute("error", "Student operation failed.");
            doGet(request, response);
        }
    }
}
