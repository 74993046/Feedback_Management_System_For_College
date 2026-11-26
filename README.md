# 🎓 STRUCTUDE – Student Feedback Management System

**STRUCTUDE** is a web-based Student Feedback Management System designed to simplify and digitize the process of collecting, managing, and analyzing student feedback about teachers and subjects.

The system provides a centralized platform where students can submit feedback, while administrators can manage student records, subjects, feedback questions, and generate reports.

## 🚀 Features

### 👨‍🎓 Student Module
- Student login using enrollment number.
- Semester-wise subject selection.
- View assigned subjects.
- Submit feedback for teachers.
- Simple and user-friendly interface.

### 🔐 Admin Module
- Secure admin login.
- Add and manage student records.
- Add and manage feedback questions.
- Assign teachers to subjects.
- Manage subjects and semesters.
- View and analyze student feedback.
- Sort feedback reports by semester and enrollment number.

### 📊 Report Generation
- Generate individual student feedback reports in PDF format.
- Generate teacher-wise overall feedback reports in Excel format.
- Organized feedback records for easy analysis.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java 17+ | Backend Development |
| Java Servlets | Server-side Logic |
| JSP | Dynamic Web Pages |
| MySQL | Database Management |
| HTML5 | Page Structure |
| CSS3 | Styling |
| Bootstrap 5 | Responsive UI |
| Apache Tomcat 11 | Web Server |
| JDBC | Database Connectivity |
| Apache POI | Excel Report Generation |

## 🏗️ System Architecture

```text
          STRUCTUDE
              |
      -------------------
      |                 |
  Student Module    Admin Module
      |                 |
  Student Login     Admin Login
      |                 |
  Select Semester   Manage Students
      |              Manage Subjects
  Select Subject    Manage Questions
      |                 |
  Submit Feedback   View Feedback
      |                 |
      -------+-----------
             |
        MySQL Database
             |
       Report Generation
          /       \
        PDF       Excel
```

## 🗄️ Database Structure

Database Name: `college`

Main Tables:

- `students` – Stores student information.
- `subjects` – Stores subject and teacher information.
- `question` – Stores feedback questions.
- `responce` – Stores student feedback responses.

## ⚙️ Installation & Setup

### 1. Prerequisites

Install the following:
- JDK 17 or later
- Eclipse IDE for Enterprise Java and Web Developers
- Apache Tomcat 11
- MySQL Server
- MySQL JDBC Driver

### 2. Clone the Repository

```bash
git clone YOUR_REPOSITORY_URL
```

### 3. Database Setup

1. Open MySQL.
2. Create a database named `college`.
3. Create the required tables.
4. Update database connection details in the `Database` Java class.

### 4. Configure the Project

1. Import the project into Eclipse.
2. Configure Apache Tomcat 11.
3. Add the MySQL JDBC driver.
4. Verify the database connection.
5. Run the project on the Tomcat server.

### 5. Open in Browser

```text
http://localhost:8080/Structude/
```

## 🎯 Objectives

- Digitize the traditional student feedback process.
- Reduce paperwork and manual work.
- Provide centralized feedback management.
- Improve feedback accessibility and organization.
- Generate structured reports for academic analysis.

## 🔮 Future Enhancements

- Graphical feedback analytics dashboard.
- Automated feedback analysis.
- AI-based sentiment analysis.
- Student performance and feedback insights.
- Online deployment and cloud database integration.

## 👨‍💻 Developed By

**Varad Patil**

Computer Engineering Student

Interested in Java Full Stack Development, AI/ML, and Cybersecurity.

---

⭐ If you find this project useful, consider giving this repository a star!

**STRUCTUDE – Making Student Feedback Management Smarter.**
