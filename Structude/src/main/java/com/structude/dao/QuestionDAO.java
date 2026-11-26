package com.structude.dao;

import com.structude.database.Database;
import com.structude.model.Question;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    public List<Question> getAllQuestions() throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT qno, title FROM question ORDER BY qno";
        try (Connection con = Database.connectDatabase();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                questions.add(new Question(rs.getInt("qno"), rs.getString("title")));
            }
        }
        return questions;
    }
}
