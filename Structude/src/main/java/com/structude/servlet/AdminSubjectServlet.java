package com.structude.servlet;

import com.structude.database.Database;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/admin-subjects")
public class AdminSubjectServlet extends HttpServlet {
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
        List<String[]> subjects = new ArrayList<>();
        try (Connection con = Database.connectDatabase();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT code,name,teacher,semester FROM subjects ORDER BY semester,code");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                subjects.add(new String[]{
                    String.valueOf(rs.getInt("code")), rs.getString("name"),
                    rs.getString("teacher"), String.valueOf(rs.getInt("semester"))
                });
            request.setAttribute("subjects", subjects);
        } catch (Exception e) {
            getServletContext().log("Loading subjects failed", e);
            request.setAttribute("error", "Unable to load subjects.");
        }
        request.getRequestDispatcher("/admin-subjects.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/admin-login.jsp");
            return;
        }
        try (Connection con = Database.connectDatabase()) {
            String action = request.getParameter("action");
            if ("add".equals(action)) {
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO subjects(code,name,teacher,semester) VALUES(?,?,?,?)")) {
                    ps.setInt(1, Integer.parseInt(request.getParameter("code")));
                    ps.setString(2, request.getParameter("name").trim());
                    ps.setString(3, request.getParameter("teacher").trim());
                    ps.setInt(4, Integer.parseInt(request.getParameter("semester")));
                    ps.executeUpdate();
                }
            } else if ("update".equals(action)) {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE subjects SET name=?,teacher=?,semester=? WHERE code=?")) {
                    ps.setString(1, request.getParameter("name").trim());
                    ps.setString(2, request.getParameter("teacher").trim());
                    ps.setInt(3, Integer.parseInt(request.getParameter("semester")));
                    ps.setInt(4, Integer.parseInt(request.getParameter("code")));
                    ps.executeUpdate();
                }
            } else if ("delete".equals(action)) {
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM subjects WHERE code=?")) {
                    ps.setInt(1, Integer.parseInt(request.getParameter("code")));
                    ps.executeUpdate();
                }
            }
            response.sendRedirect(request.getContextPath() + "/admin-subjects?success=1");
        } catch (SQLIntegrityConstraintViolationException e) {
            request.setAttribute("error", "Subject code already exists.");
            doGet(request, response);
        } catch (Exception e) {
            getServletContext().log("Subject operation failed", e);
            request.setAttribute("error", "Subject operation failed.");
            doGet(request, response);
        }
    }
}
