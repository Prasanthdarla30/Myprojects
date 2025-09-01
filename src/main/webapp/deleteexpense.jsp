<%@ page import="java.sql.*" %>


<%
    int id = Integer.parseInt(request.getParameter("id"));

    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense", "root", "root");
    String sql = "DELETE FROM addexpense WHERE id=?";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setInt(1, id);
    stmt.executeUpdate();
    conn.close();

    response.sendRedirect("viewexpenses.jsp");
%>