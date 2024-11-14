package com.library.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BooksServlet
 */
@WebServlet("/BooksServlet")
public class BooksServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM books";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
        	out.println("<html> <head> <title> Add New Book </title>"
        			+ "<link rel='stylesheet' href='style.css' media='all' />"
        			+"<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">"
        			+ "</head>");
    		out.println("<body class='bg'>");
    		
            out.println("<center><h1>Library Management System!!</h1>");
            out.println("<table><tr><th>ID</th><th>Title</th><th>Author</th><th>Total Quantity</th><th>Available Quantity</th><th>Issued Quantity</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("title") + "</td><td>" + rs.getString("author") + "</td><td>" + rs.getInt("total_quantity") + "</td><td>" + rs.getInt("available_quantity") + "</td><td>" + rs.getInt("issued_quantity") + "</td></tr>");
            }
            out.println("</table>");
            
            out.println("<a class='btn btn-primary' href='AddBookServlet'>ADD BOOK</a>");
            out.println("<a class='btn btn-primary' href='AddMember'>ADD MEMBER</a>");
            out.println("<a class='btn btn-primary' href='IssueBookServlet'>Issue BOOK</a>");
            out.println("<a class='btn btn-primary' href='ReturnBookServlet'>Return BOOK</a>");
            out.println("</center>");
    		out.println("</body></html>");

           // response.sendRedirect("BooksServlet");
        } catch (SQLException e) {
            throw new ServletException("Error displaying books", e);
        }
    }
}
