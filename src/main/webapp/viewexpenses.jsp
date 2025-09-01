<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>View Expenses</title>
<link rel="stylesheet" href="stylesheet.css">
<style>
.project-name {
    position: absolute;
    top: 10px;
    left: 10px;
    font-size:20px;
    font-weight: bold;
    color: #273746;
}
.container {
    max-width: 1200px;
    margin: 20px auto;
    padding: 0 15px;
}
.table-wrapper {
    max-height: 500px;
    overflow-y: auto;
    position: relative;
}
table {
    width: 100%;
    border-collapse: collapse;
}
thead th {
    background-color: #273746;
    color: white;
    padding: 12px;
    text-align: left;
    position: sticky;
    top: 0;
    z-index: 1;
}
tbody td {
    padding: 10px;
    vertical-align: top;
}
.link-container {
    text-align: center;
    margin-top: 15px;
}
.link-container a, .edit-btn, .delete-btn, .view-btn {
    padding: 8px 12px;
    text-decoration: none;
    color: white;
    background-color: #273746;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-weight: bold;
    margin-right: 8px;
    display: inline-block;
}
.edit-btn:hover, .delete-btn:hover, .view-btn:hover {
    background-color: #1c2833;
}
td img {
    max-width: 50px;
    max-height: 50px;
    vertical-align: middle;
}
.filter-container {
    margin-bottom: 15px;
}
.popup {
    display: none;
    position: fixed;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0 0 10px rgba(0,0,0,0.3);
    z-index: 1000;
    max-width: 500px;
    max-height: 80vh;
    overflow-y: auto;
}
.popup-content {
    margin-bottom: 15px;
    word-wrap: break-word;
}
.close-btn {
    position: absolute;
    top: 10px;
    right: 10px;
    cursor: pointer;
    font-size: 20px;
    color: #273746;
}
.overlay {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.5);
    z-index: 999;
}
.person-name {
    display: none; /* Default hidden */
}
</style>
</head>
<body>
<div class="project-name">Track & Save</div>

<div id="overlay" class="overlay" onclick="closePopup()"></div>
<div id="descriptionPopup" class="popup">
    <span class="close-btn" onclick="closePopup()">×</span>
    <div id="popupContent" class="popup-content"></div>
</div>

<div class="container">
<h2>View Expenses</h2>

<div class="filter-container">
    <select id="expenseFilter" onchange="showExpenses()">
        <option value="myself">My Expenses</option>
        <option value="others">Expenses for others<option>
    </select>
</div>

<div class="table-wrapper">
<table>
<thead>
<tr>
<th>Item Name</th>
<th>Category</th>
<th>Amount</th>
<th>Date</th>
<th>Payment Mode</th>
<th>Expense For</th>
<th>Person Name</th>
<th>Purchase Type</th>
<th>Online Store</th>
<th>Description</th>
<th>Image</th>
<th>Receipt</th>
<th>Actions</th>
</tr>
</thead>
<tbody id="expenseTableBody">
<%
HttpSession sessionUser = request.getSession(false);
if (sessionUser == null || sessionUser.getAttribute("loggedInUser") == null) {
    out.println("<tr><td colspan='13'>User not logged in.</td></tr>");
} else {
    String username = (String) sessionUser.getAttribute("loggedInUser");
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense", "root", "root");
        String sql = "SELECT * FROM addexpense WHERE username = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        rs = stmt.executeQuery();
        boolean hasData = false;
        while (rs.next()) {
            hasData = true;
            out.println("<tr class='expense-row' data-expense-for='" + rs.getString("expense_for") + "'>");
            out.println("<td>" + rs.getString("item_name") + "</td>");
            out.println("<td>" + rs.getString("category") + "</td>");
            out.println("<td>" + rs.getDouble("amount") + "</td>");
            out.println("<td>" + rs.getString("date") + "</td>");
            out.println("<td>" + rs.getString("payment_mode") + "</td>");
            out.println("<td>" + rs.getString("expense_for") + "</td>");
            out.println("<td class='person-name'>" + (rs.getString("person_name") != null ? rs.getString("person_name") : "N/A") + "</td>");
            out.println("<td>" + rs.getString("purchase_type") + "</td>");
            out.println("<td>" + (rs.getString("online_store") != null ? rs.getString("online_store") : "N/A") + "</td>");
            out.println("<td>");
            String description = rs.getString("description");
            if (description != null && !description.isEmpty()) {
                out.println("<button class='view-btn' onclick='showPopup(\"" + description.replace("\"", "\\\"") + "\")'>View</button>");
            } else {
                out.println("N/A");
            }
            out.println("</td>");

            String itemImagePath = rs.getString("item_image_path");
            out.println("<td>");
            if (itemImagePath != null && !itemImagePath.isEmpty()) {
                out.println("<img src='uploads/" + itemImagePath + "' alt='Item Image'>");
                out.println("<br><a href='uploads/" + itemImagePath + "' target='_blank' class='view-btn'>View</a>");
            } else {
                out.println("No Image");
            }
            out.println("</td>");

            String receiptPath = rs.getString("receipt_path");
            out.println("<td>");
            if (receiptPath != null && !receiptPath.isEmpty()) {
                out.println("<a href='uploads/" + receiptPath + "' target='_blank' class='view-btn'>View</a>");
            } else {
                out.println("No Receipt");
            }
            out.println("</td>");

            out.println("<td>");
            out.println("<a href='EditExpenseServlet?id=" + rs.getInt("id") + "' class='edit-btn'>Edit</a>");
            out.println("<a href='deleteexpense.jsp?id=" + rs.getInt("id") + " ' class='delete-btn' onclick='return confirm(\"Are you sure you want to delete:\\nItem Name: " + rs.getString("item_name") + "\\nAmount: " + rs.getDouble("amount") + "\\nDate: " + rs.getString("date") + "?\")'>Delete</a>");
            out.println("</td>");
            out.println("</tr>");
        }
        if (!hasData) {
            out.println("<tr><td colspan='13'>No expenses found.</td></tr>");
        }
    } catch (Exception e) {
        e.printStackTrace();
        out.println("<tr><td colspan='13'>Error loading expenses: " + e.getMessage() + "</td></tr>");
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
}
%>
</tbody>
</table>
</div>
<div class="link-container">
<a href="Addexpense.html">Add Expense</a>
<a href="dashboard.jsp">Dashboard</a>
</div>
</div>

<script>
function showPopup(description) {
    document.getElementById("popupContent").textContent = description;
    document.getElementById("descriptionPopup").style.display = "block";
    document.getElementById("overlay").style.display = "block";
}

function closePopup() {
    document.getElementById("descriptionPopup").style.display = "none";
    document.getElementById("overlay").style.display = "none";
}

function showExpenses() {
    var selectedValue = document.getElementById("expenseFilter").value;
    var rows = document.getElementsByClassName("expense-row");
    var headerCells = document.querySelectorAll("thead th");
    
    for (var i = 0; i < rows.length; i++) {
        var expenseFor = rows[i].getAttribute("data-expense-for");
        var personNameCell = rows[i].getElementsByClassName("person-name")[0];
        
        if (selectedValue === "myself") {
            if (expenseFor.toLowerCase() === "myself") {                  
                rows[i].style.display = "table-row";
                personNameCell.style.display = "none";
                headerCells[6].style.display = "none";
            } else {
                rows[i].style.display = "none";
            }
        } else {
            if (expenseFor.toLowerCase() !== "myself") {
                rows[i].style.display = "table-row";
                personNameCell.style.display = "table-cell";
                headerCells[6].style.display = "table-cell";
            } else {
                rows[i].style.display = "none";
            }
        }
    }
}

window.onload = function() {
    var rows = document.getElementsByClassName("expense-row");
    var headerCells = document.querySelectorAll("thead th");
    
    for (var i = 0; i < rows.length; i++) {
        var expenseFor = rows[i].getAttribute("data-expense-for");
        var personNameCell = rows[i].getElementsByClassName("person-name")[0];
        if (expenseFor.toLowerCase() === "myself") {
            rows[i].style.display = "table-row";
            personNameCell.style.display = "none";
            headerCells[6].style.display = "none";
        } else {
            rows[i].style.display = "none";
        }
    }
};
</script>

</body>
</html>