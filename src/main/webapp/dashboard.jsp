<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    HttpSession userSession = request.getSession(false);
    String username = null;

    if (userSession != null) {
        username = (String) userSession.getAttribute("loggedInUser");
    }

    if (username == null) {
        response.sendRedirect("index.html");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <style>
     .project-name {
            position: absolute;
            top: 10px;
            left: 10px; /* Adjusted to align with page edge */
            font-size:25px;
            font-weight: bold;
            color: #273746;
        }
    
        body {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: #f9f9f9;
            font-family: Arial, sans-serif;
        }
        
        .dashboard-container {
            text-align: center;
            padding: 20px;
            background: white;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            width: 300px;
        }
        
        h1 {
            margin-bottom: 15px;
            color: #333;
        }

        a {
            display: block;
            text-decoration: none;
            color: #007bff;
            font-size: 16px;
            margin: 10px 0;
        }

        button {
            background-color: #273746;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            width: 100%;
            margin-top: 10px;
        }

        button:hover {
            background-color: #273746;
        }
    </style>
</head>
<body>
    <div class="project-name">Track & Save</div>

    <div class="dashboard-container">
        <h1>Welcome, <%= username %>!</h1>

        <a href="Addexpense.html">Add Expense</a>
        <a href="viewexpenses.jsp">View/edit Expenses</a>

          <button onclick="location.href='index.html'">Logout</button>
          
    </div>

</body>
</html>
