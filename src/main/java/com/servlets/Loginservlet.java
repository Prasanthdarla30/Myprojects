package com.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/loginservlet")
public class Loginservlet extends HttpServlet{
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense", "root", "root");

            PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM users WHERE username=? AND password=?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Successful login - create session
                HttpSession session = request.getSession();
                session.setAttribute("userId", rs.getInt("id"));
                session.setAttribute("loggedInUser", username);
                
                response.sendRedirect("dashboard.jsp");
            } else {
                // If login fails, show an alert and go back to login page
                response.setContentType("text/html");
                response.getWriter().println("<script>alert('Invalid username or password! Please try again.');window.location='index.html';</script>");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
