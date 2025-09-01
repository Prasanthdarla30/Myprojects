package com.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/AddexpenseServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
                 maxFileSize = 1024 * 1024 * 10,
                 maxRequestSize = 1024 * 1024 * 50)  
public class AddexpenseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            out.print("<p style='color:red;'>User not logged in.</p>");
            return;
        }

        String username = (String) session.getAttribute("loggedInUser");
        String itemName = request.getParameter("itemName");
        String category = request.getParameter("category");
        String newCategory = request.getParameter("newCategory");
        String amount = request.getParameter("amount");
        String dateStr = request.getParameter("date");
        String paymentMode = request.getParameter("paymentMode");
        String expenseFor = request.getParameter("expenseFor");
        String personName = request.getParameter("personName");
        String newPerson = request.getParameter("newPerson");
        String[] purchaseTypes = request.getParameterValues("purchaseType"); // Get all selected values
        String onlineStore = request.getParameter("onlineStore");
        String description = request.getParameter("description");

        // Handle category
        category = ("Other".equals(category) && newCategory != null && !newCategory.isEmpty()) ? newCategory : category;

        // Handle expenseFor and personName
        personName = ("Others".equals(expenseFor) && newPerson != null && !newPerson.isEmpty()) ? newPerson :
                     ("Others".equals(expenseFor) && personName != null && !personName.isEmpty()) ? personName : null;

        
        
        // Handle purchaseType (join multiple values with comma)
        String purchaseType = (purchaseTypes != null) ? String.join(",", purchaseTypes) : null;

        Date date = Date.valueOf(dateStr);

        Part receiptPart = request.getPart("receipt");
        Part imagePart = request.getPart("itemImage");

        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        String receiptFileName = (receiptPart != null && receiptPart.getSize() > 0) ?
            Paths.get(receiptPart.getSubmittedFileName()).getFileName().toString() : null;
        String imageFileName = (imagePart != null && imagePart.getSize() > 0) ?
            Paths.get(imagePart.getSubmittedFileName()).getFileName().toString() : null;

        if (receiptFileName != null) receiptPart.write(uploadPath + File.separator + receiptFileName);
        if (imageFileName != null) imagePart.write(uploadPath + File.separator + imageFileName);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense", "root", "root")) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String sql = "INSERT INTO addexpense (username, item_name, category, amount, date, payment_mode, expense_for, purchase_type, description, receipt_path, item_image_path";
           
            if (personName != null && !personName.isEmpty()) {
                sql += ", person_name";
            }
            if (onlineStore != null && !onlineStore.isEmpty() && purchaseType != null && purchaseType.contains("Online")) {
                sql += ", online_store";
            }
           
            sql += ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
           
            if (personName != null && !personName.isEmpty()) {
                sql += ", ?";
            }
            if (onlineStore != null && !onlineStore.isEmpty() && purchaseType != null && purchaseType.contains("Online")) {
                sql += ", ?";
            }
           
            sql += ")";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, itemName);
            stmt.setString(3, category);
            stmt.setString(4, amount);
            stmt.setDate(5, date);
            stmt.setString(6, paymentMode);
            stmt.setString(7, expenseFor);
            stmt.setString(8, purchaseType);
            stmt.setString(9, description);
            stmt.setString(10, receiptFileName);
            stmt.setString(11, imageFileName);
           
            int paramIndex = 12;
            if (personName != null && !personName.isEmpty()) {
                stmt.setString(paramIndex++, personName);
            }
            if (onlineStore != null && !onlineStore.isEmpty() && purchaseType != null && purchaseType.contains("Online")) {
                stmt.setString(paramIndex++, onlineStore);
            }

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                response.sendRedirect("viewexpenses.jsp");
            } else {
                out.print("<p style='color:red;'>Error adding expense.</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("<p style='color:red;'>Error connecting to database.</p>");
        }
    }
}