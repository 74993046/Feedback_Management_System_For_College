package com.structude.dao;

import com.structude.database.Database;
import com.structude.model.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    public List<Subject> getSubjectsBySemester(int semester) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT code, name, teacher, semester FROM subjects WHERE semester = ? ORDER BY code";
        try (Connection con = Database.connectDatabase();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, semester);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    subjects.add(new Subject(
                        rs.getInt("code"), rs.getString("name"),
                        rs.getString("teacher"), rs.getInt("semester")));
                }
            }
        }
        return subjects;
    }
}
