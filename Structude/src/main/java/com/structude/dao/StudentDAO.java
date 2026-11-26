package com.structude.dao;

import com.structude.database.*;
import com.structude.model.Student;
import java.sql.*;

public class StudentDAO {
    public Student findByEnrollment(String enrollment) throws SQLException {
        String sql = "SELECT enrollment, name, mono FROM students WHERE enrollment = ?";
        try (Connection con = Database.connectDatabase();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, enrollment);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(rs.getString("enrollment"),
                            rs.getString("name"), rs.getString("mono"));
                }
            }
        }
        return null;
    }

    public boolean validateStudent(String enrollment) throws SQLException {
        return findByEnrollment(enrollment) != null;
    }
}
