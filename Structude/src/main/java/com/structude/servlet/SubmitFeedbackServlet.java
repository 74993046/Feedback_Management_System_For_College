package com.structude.servlet;

import com.structude.dao.FeedbackDAO;
import com.structude.model.Feedback;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/submit-feedback")
public class SubmitFeedbackServlet extends HttpServlet {
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("student") == null) {
            response.sendRedirect(request.getContextPath() + "/student-login.html");
            return;
        }

        try {
            String enrollment = (String) session.getAttribute("enrollment");
            int sem = Integer.parseInt(request.getParameter("sem"));
            int code = Integer.parseInt(request.getParameter("code"));
            int q1 = Integer.parseInt(request.getParameter("q1"));
            int q2 = Integer.parseInt(request.getParameter("q2"));
            int q3 = Integer.parseInt(request.getParameter("q3"));
            
            
            Feedback feedback = new Feedback(enrollment, sem, q1, q2, q3, code);
            feedbackDAO.insertFeedback(feedback);

            response.sendRedirect(request.getContextPath() +
                    "/display-subjects?sem=" + sem + "&success=1");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Please provide valid ratings and subject details.");
            request.getRequestDispatcher("/feedback-form.jsp").forward(request, response);
        } catch (Exception e) {
            getServletContext().log("Feedback submission failed", e);
            request.setAttribute("error", "Feedback could not be submitted. Please try again.");
            request.getRequestDispatcher("/feedback-form.jsp").forward(request, response);
        }
    }
}
