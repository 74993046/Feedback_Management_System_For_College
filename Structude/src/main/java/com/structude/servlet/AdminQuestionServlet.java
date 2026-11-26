package com.structude.servlet;

import com.structude.database.Database;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/admin-questions")
public class AdminQuestionServlet extends HttpServlet {
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
        List<String[]> questions = new ArrayList<>();
        try (Connection con = Database.connectDatabase();
             PreparedStatement ps = con.prepareStatement("SELECT qno,title FROM question ORDER BY qno");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                questions.add(new String[]{String.valueOf(rs.getInt("qno")), rs.getString("title")});
            request.setAttribute("questions", questions);
        } catch (Exception e) {
            getServletContext().log("Loading questions failed", e);
            request.setAttribute("error", "Unable to load questions.");
        }
        request.getRequestDispatcher("/admin-questions.jsp").forward(request, response);
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
                        "INSERT INTO question(qno,title) VALUES(?,?)")) {
                    ps.setInt(1, Integer.parseInt(request.getParameter("qno")));
                    ps.setString(2, request.getParameter("title").trim());
                    ps.executeUpdate();
                }
            } else if ("delete".equals(action)) {
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM question WHERE qno=?")) {
                    ps.setInt(1, Integer.parseInt(request.getParameter("qno")));
                    ps.executeUpdate();
                }
            }
            response.sendRedirect(request.getContextPath() + "/admin-questions?success=1");
        } catch (SQLIntegrityConstraintViolationException e) {
            request.setAttribute("error", "Question number already exists.");
            doGet(request, response);
        } catch (Exception e) {
            getServletContext().log("Question operation failed", e);
            request.setAttribute("error", "Question operation failed.");
            doGet(request, response);
        }
    }
}
