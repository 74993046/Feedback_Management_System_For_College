package com.structude.dao;

import com.structude.database.Database;
import com.structude.model.Feedback;
import java.sql.*;

public class FeedbackDAO {
    public void insertFeedback(Feedback feedback) throws SQLException {
        if (feedback.getQ1() < 1 || feedback.getQ1() > 5 ||
            feedback.getQ2() < 1 || feedback.getQ2() > 5 ||
            feedback.getQ3() < 1 || feedback.getQ3() > 5) {
            throw new IllegalArgumentException("Ratings must be between 1 and 5.");
        }

        String sql = "INSERT INTO responce (enrollment, sem, q1, q2, q3, code) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Database.connectDatabase();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, feedback.getEnrollment());
            ps.setInt(2, feedback.getSem());
            ps.setInt(3, feedback.getQ1());
            ps.setInt(4, feedback.getQ2());
            ps.setInt(5, feedback.getQ3());
            ps.setInt(6, feedback.getCode());
            ps.executeUpdate();
        }
    }
}
