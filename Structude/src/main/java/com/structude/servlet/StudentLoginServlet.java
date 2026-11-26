package com.structude.servlet;

import com.structude.dao.StudentDAO;
import com.structude.model.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/student-login")
public class StudentLoginServlet extends HttpServlet {
    private final StudentDAO studentDAO = new StudentDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String enrollment = request.getParameter("enrollment");
        if (enrollment == null || enrollment.isBlank()) {
            request.setAttribute("error", "Please enter your enrollment number.");
            request.getRequestDispatcher("/student-login.html").forward(request, response);
            return;
        }

        try {
            Student student = studentDAO.findByEnrollment(enrollment.trim());
            if (student != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("student", student);
                session.setAttribute("enrollment", student.getEnrollment());
                response.sendRedirect(request.getContextPath() + "/semester-select.jsp");
            } else {
                request.setAttribute("error", "Invalid enrollment number.");
                request.getRequestDispatcher("/student-login.html").forward(request, response);
            }
        } catch (Exception e) {
            getServletContext().log("Student login failed", e);
            request.setAttribute("error", "Unable to connect to the database.");
            request.getRequestDispatcher("/student-login.html").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/student-login.html");
    }
}
