package com.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/EditExpenseServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class EditExpenseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database Connection Details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/expense";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    // Handle GET request - Show Expense Details
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int id = Integer.parseInt(request.getParameter("id"));
        String itemName = "", category = "", date = "", paymentMode = "", expenseFor = "", 
               purchaseType = "", onlineStore = "", description = "", receiptPath = "", itemImagePath = "", personName = "";
        double amount = 0.0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "SELECT * FROM addexpense WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                itemName = rs.getString("item_name");
                category = rs.getString("category");
                amount = rs.getDouble("amount");
                date = rs.getString("date");
                paymentMode = rs.getString("payment_mode");
                expenseFor = rs.getString("expense_for");
                personName = rs.getString("person_name");
                purchaseType = rs.getString("purchase_type");
                onlineStore = rs.getString("online_store");
                description = rs.getString("description");
                receiptPath = rs.getString("receipt_path");
                itemImagePath = rs.getString("item_image_path");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error retrieving expense details: " + e.getMessage() + "</p>");
            return;
        }

        // Display Form to Edit Expense
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'><head><title>Edit Expense</title>");
        out.println("<link rel='stylesheet' type='text/css' href='editexpense.css'>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }");
        out.println(".container { background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1); max-width: 600px; margin: auto; }");
        out.println("label { display: block; margin-top: 10px; font-weight: bold; }");
        out.println("input, select, textarea { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }");
        out.println(".radio-group, .checkbox-group { margin-top: 5px; }");
        out.println("input[type='radio'], input[type='checkbox'] { width: auto; margin-right: 5px; }");
        out.println("button { background-color: #273746; color: white; padding: 10px; border: none; border-radius: 5px; cursor: pointer; margin-top: 15px; width: 100%; }");
        out.println("button:hover { background-color: #1c2833; }");
        out.println(".file-preview { margin-top: 5px; }");
        out.println(".add-button { padding: 5px 10px; background-color: #273746; color: white; border: none; border-radius: 5px; cursor: pointer; margin-top: 5px; }");
        out.println(".add-button:hover { background-color: #1c2833; }");
        out.println("#personDropdown, #addPerson { display: " + ("Others".equals(expenseFor) ? "block" : "none") + "; }");
        out.println("#onlineStore { display: " + ("Online".equals(purchaseType) ? "block" : "none") + "; }");
        out.println("</style>");
        out.println("<script>");
        out.println("function togglePersonDropdown() {");
        out.println("  var expenseOthers = document.getElementById('expenseOthers');");
        out.println("  var personDropdown = document.getElementById('personDropdown');");
        out.println("  var addPerson = document.getElementById('addPerson');");
        out.println("  var addPersonBtn = document.getElementById('addPersonBtn');");
        out.println("  if (expenseOthers.checked) {");
        out.println("    personDropdown.style.display = 'block';");
        out.println("    addPerson.style.display = 'block';");
        out.println("    addPersonBtn.style.display = 'block';");
        out.println("  } else {");
        out.println("    personDropdown.style.display = 'none';");
        out.println("    addPerson.style.display = 'none';");
        out.println("    addPersonBtn.style.display = 'none';");
        out.println("  }");
        out.println("}");
        out.println("function addNewPerson() {");
        out.println("  var newPerson = document.getElementById('addPerson').value;");
        out.println("  if (newPerson) {");
        out.println("    var select = document.getElementById('personDropdown');");
        out.println("    var option = document.createElement('option');");
        out.println("    option.value = newPerson;");
        out.println("    option.text = newPerson;");
        out.println("    select.add(option);");
        out.println("    document.getElementById('addPerson').value = '';");
        out.println("  }");
        out.println("}");
        out.println("function restrictCheckbox(checkbox) {");
        out.println("  var checkboxes = document.querySelectorAll('input[name=\"purchaseType\"]');");
        out.println("  checkboxes.forEach(function(cb) {");
        out.println("    if (cb !== checkbox) cb.checked = false;");
        out.println("  });");
        out.println("  updatePurchaseType();");
        out.println("}");
        out.println("function updatePurchaseType() {");
        out.println("  var onlineCheckbox = document.querySelector('input[name=\"purchaseType\"][value=\"Online\"]');");
        out.println("  var onlineStore = document.getElementById('onlineStore');");
        out.println("  onlineStore.style.display = onlineCheckbox.checked ? 'block' : 'none';");
        out.println("}");
        out.println("</script>");
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h2>Edit Expense</h2>");
        out.println("<form action='EditExpenseServlet' method='post' enctype='multipart/form-data'>");
        out.println("<input type='hidden' name='id' value='" + id + "'>");

        out.println("<label>Item Name:</label>");
        out.println("<input type='text' name='itemName' value='" + itemName + "' required>");

        out.println("<label>Category:</label>");
        out.println("<input type='text' name='category' value='" + category + "' required>");

        out.println("<label>Amount:</label>");
        out.println("<input type='number' name='amount' value='" + amount + "' step='0.01' required>");

        out.println("<label>Date:</label>");
        out.println("<input type='date' name='date' value='" + date + "' required>");

        out.println("<label>Payment Mode:</label>");
        out.println("<select name='paymentMode' required>");
        out.println("<option value='Cash'" + (paymentMode.equals("Cash") ? " selected" : "") + ">Cash</option>");
        out.println("<option value='Google Pay'" + (paymentMode.equals("Google Pay") ? " selected" : "") + ">Google Pay</option>");
        out.println("<option value='PhonePe'" + (paymentMode.equals("PhonePe") ? " selected" : "") + ">PhonePe</option>");
        out.println("<option value='Paytm'" + (paymentMode.equals("Paytm") ? " selected" : "") + ">Paytm</option>");
        out.println("</select>");

        out.println("<label>Expense For:</label>");
        out.println("<div class='radio-group'>");
        out.println("<label><input type='radio' name='expenseFor' value='Myself' " + (expenseFor.equals("Myself") ? "checked" : "") + " onclick='togglePersonDropdown()'> Myself</label>");
        out.println("<label><input type='radio' name='expenseFor' id='expenseOthers' value='Others' " + (expenseFor.equals("Others") ? "checked" : "") + " onclick='togglePersonDropdown()'> Others</label>");
        out.println("</div>");
        out.println("<select id='personDropdown' name='personName'>");
        out.println("<option value=''" + (personName == null || personName.isEmpty() ? " selected" : "") + ">Select Person</option>");
        out.println("<option value='Mother'" + ("Mother".equals(personName) ? " selected" : "") + ">Mother</option>");
        out.println("<option value='Father'" + ("Father".equals(personName) ? " selected" : "") + ">Father</option>");
        out.println("<option value='Sister'" + ("Sister".equals(personName) ? " selected" : "") + ">Sister</option>");
        if (personName != null && !personName.isEmpty() && !("Mother".equals(personName) || "Father".equals(personName) || "Sister".equals(personName))) {
            out.println("<option value='" + personName + "' selected>" + personName + "</option>");
        }
        out.println("</select>");
        out.println("<input type='text' id='addPerson' name='newPerson' placeholder='Enter new name'>");
        out.println("<button type='button' id='addPersonBtn' class='add-button' onclick='addNewPerson()'>Add</button>");

        out.println("<label>Purchase Type:</label>");
        out.println("<div class='checkbox-group'>");
        out.println("<label><input type='checkbox' name='purchaseType' value='Online' " + ("Online".equals(purchaseType) ? "checked" : "") + " onclick='restrictCheckbox(this)'> Online</label>");
        out.println("<label><input type='checkbox' name='purchaseType' value='Offline' " + ("Offline".equals(purchaseType) ? "checked" : "") + " onclick='restrictCheckbox(this)'> Offline</label>");
        out.println("</div>");
        out.println("<select id='onlineStore' name='onlineStore'>");
        out.println("<option value=''" + (onlineStore == null || onlineStore.isEmpty() ? " selected" : "") + ">None</option>");
        out.println("<option value='Amazon'" + ("Amazon".equals(onlineStore) ? " selected" : "") + ">Amazon</option>");
        out.println("<option value='Flipkart'" + ("Flipkart".equals(onlineStore) ? " selected" : "") + ">Flipkart</option>");
        out.println("<option value='Myntra'" + ("Myntra".equals(onlineStore) ? " selected" : "") + ">Myntra</option>");
        out.println("</select>");

        out.println("<label>Description:</label>");
        out.println("<textarea name='description'>" + (description != null ? description : "") + "</textarea>");

        out.println("<label>Current Receipt:</label>");
        out.println("<div class='file-preview'>" + (receiptPath != null && !receiptPath.isEmpty() ? "<a href='uploads/" + receiptPath + "' target='_blank'>View Current Receipt</a>" : "No Receipt") + "</div>");
        out.println("<label>Upload New Receipt:</label>");
        out.println("<input type='file' name='receipt' accept='image/*,application/pdf'>");

        out.println("<label>Current Item Image:</label>");
        out.println("<div class='file-preview'>" + (itemImagePath != null && !itemImagePath.isEmpty() ? "<img src='uploads/" + itemImagePath + "' width='100'><br><a href='uploads/" + itemImagePath + "' target='_blank'>View Current Image</a>" : "No Image") + "</div>");
        out.println("<label>Upload New Item Image:</label>");
        out.println("<input type='file' name='itemImage' accept='image/*'>");

        out.println("<button type='submit'>Update Expense</button>");
        out.println("</form>");
        out.println("</div>");
        out.println("</body></html>");
    }

    // Handle POST request - Update Expense
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String itemName = request.getParameter("itemName");
        String category = request.getParameter("category");
        double amount = Double.parseDouble(request.getParameter("amount"));
        String date = request.getParameter("date");
        String paymentMode = request.getParameter("paymentMode");
        String expenseFor = request.getParameter("expenseFor");
        String personName = request.getParameter("personName");
        String newPerson = request.getParameter("newPerson");
        String purchaseType = request.getParameter("purchaseType"); // Single value due to restriction
        String onlineStore = request.getParameter("onlineStore");
        String description = request.getParameter("description");

        // Handle personName: use newPerson if provided, otherwise use personName
        personName = (newPerson != null && !newPerson.isEmpty()) ? newPerson : 
                     ("Others".equals(expenseFor) && personName != null && !personName.isEmpty()) ? personName : null;

        // Ensure purchaseType is null if empty or not provided
        if (purchaseType != null && purchaseType.isEmpty()) {
            purchaseType = null;
        }

        Part receiptPart = request.getPart("receipt");
        Part imagePart = request.getPart("itemImage");

        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        String receiptFileName = null, itemImageFileName = null;

        // Fetch existing file paths to keep them if no new file is uploaded
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String fetchSql = "SELECT receipt_path, item_image_path FROM addexpense WHERE id=?";
            PreparedStatement fetchStmt = conn.prepareStatement(fetchSql);
            fetchStmt.setInt(1, id);
            ResultSet rs = fetchStmt.executeQuery();
            if (rs.next()) {
                receiptFileName = rs.getString("receipt_path");
                itemImageFileName = rs.getString("item_image_path");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Handle new file uploads
        if (receiptPart != null && receiptPart.getSize() > 0) {
            receiptFileName = Paths.get(receiptPart.getSubmittedFileName()).getFileName().toString();
            receiptPart.write(uploadPath + File.separator + receiptFileName);
        }
        if (imagePart != null && imagePart.getSize() > 0) {
            itemImageFileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
            imagePart.write(uploadPath + File.separator + itemImageFileName);
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String sql = "UPDATE addexpense SET item_name=?, category=?, amount=?, date=?, payment_mode=?, expense_for=?, purchase_type=?, description=?, receipt_path=?, item_image_path=?" +
                        (personName != null && !personName.isEmpty() ? ", person_name=?" : "") +
                        (onlineStore != null && !onlineStore.isEmpty() && "Online".equals(purchaseType) ? ", online_store=?" : "") +
                        " WHERE id=?";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, itemName);
            stmt.setString(2, category);
            stmt.setDouble(3, amount);
            stmt.setString(4, date);
            stmt.setString(5, paymentMode);
            stmt.setString(6, expenseFor);
            stmt.setString(7, purchaseType);
            stmt.setString(8, description);
            stmt.setString(9, receiptFileName);
            stmt.setString(10, itemImageFileName);

            int paramIndex = 11;
            if (personName != null && !personName.isEmpty()){
                stmt.setString(paramIndex++, personName);
            }
            if (onlineStore != null && !onlineStore.isEmpty() && "Online".equals(purchaseType)){
                stmt.setString(paramIndex++, onlineStore);
            }
            stmt.setInt(paramIndex, id);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                response.sendRedirect("viewexpenses.jsp");
            } else {
                response.getWriter().println("<p>Error updating expense.</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error updating expense: " + e.getMessage() + "</p>");
        }
    }
}