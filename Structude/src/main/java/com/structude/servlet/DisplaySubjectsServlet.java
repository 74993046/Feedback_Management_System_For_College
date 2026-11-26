package com.structude.servlet;

import com.structude.dao.SubjectDAO;
import com.structude.model.Subject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/display-subjects")
public class DisplaySubjectsServlet extends HttpServlet {
    private final SubjectDAO subjectDAO = new SubjectDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("student") == null) {
            response.sendRedirect(request.getContextPath() + "/student-login.html");
            return;
        }

        String semParam = request.getParameter("sem");
        try {
            int semester = Integer.parseInt(semParam);
            if (semester < 1 || semester > 12) throw new NumberFormatException();

            List<Subject> subjects = subjectDAO.getSubjectsBySemester(semester);
            session.setAttribute("semester", semester);
            request.setAttribute("subjects", subjects);
            request.setAttribute("semester", semester);
            request.getRequestDispatcher("/subject-list.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid semester.");
            request.getRequestDispatcher("/semester-select.jsp").forward(request, response);
        } catch (Exception e) {
            getServletContext().log("Loading subjects failed", e);
            request.setAttribute("error", "Unable to load subjects.");
            request.getRequestDispatcher("/semester-select.jsp").forward(request, response);
        }
    }
}
