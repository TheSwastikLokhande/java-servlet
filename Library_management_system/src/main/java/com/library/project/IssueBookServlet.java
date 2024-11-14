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
 * Servlet implementation class IssueBookServlet
 */
@WebServlet("/IssueBookServlet")
public class IssueBookServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	response.setContentType("text/html");
		PrintWriter out = response.getWriter();
    	
		
    	out.println("<!doctype html>");
		out.println("<html> <head> <title> Issue Book </title>"
				+ "<link rel='stylesheet' href='style.css' media='all' />"
    			+"<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">"
				+ "</head>");
		
		out.println("<body class='bg'>");
		out.println("<center>");
		out.println("<h2 style='color: white;'>Issue Book</h2>");
		
		out.println("<form action='IssueBookServlet' method='post'>");
		out.println("<span style='color: white;'>Book Id:</span> <input type='text' name='book_id' id='book_id' required><br><br>");
		out.println("<span style='color: white;'>Member Id:</span> <input type='text' name='member_id' id='member_id' required><br><br>");
		
		
		out.println("<input type='submit' value='Submit' style='background-color: #1759f1; color: white; padding: 5px 40px; border: none; border-radius: 5px; cursor: pointer;'/>");
		
		out.println("<br>");
		out.println("<h2 style='color: white;'>Issued Books</h2>");
		
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM issued_books";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            out.println("<table border='1'><tr><th>Issue Id</th><th>Book ID</th><th>Member Id</th><th>Issue Date</th><th>Return Date</th><th>Status</th>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("issue_id") + "</td><td>" + rs.getInt("book_id") + "</td><td>" + rs.getInt("member_id") + "</td><td>" + rs.getDate("issue_date") +"</td><td>"  + rs.getDate("return_date")+"</td><td>" +rs.getString("status")+ "</tr>");
            }
            out.println("</table>");
	 
            out.println("</center>");
	 
	 
	 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		out.println("</form></body></html>");
		
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("book_id"));
        int memberId = Integer.parseInt(request.getParameter("member_id"));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String checkSql = "SELECT available_quantity FROM books WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt("available_quantity") > 0) {
                String issueSql = "INSERT INTO issued_books (book_id, member_id, issue_date, status) VALUES (?, ?, CURDATE(), 'Issued')";
                PreparedStatement issueStmt = conn.prepareStatement(issueSql);
                issueStmt.setInt(1, bookId);
                issueStmt.setInt(2, memberId);
                issueStmt.executeUpdate();

                String updateSql = "UPDATE books SET available_quantity = available_quantity - 1, issued_quantity = issued_quantity + 1 WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();

                response.sendRedirect("BooksServlet");
            } else {
                response.getWriter().println("Sorry, this book is currently unavailable.");
            }
        } catch (SQLException e) {
            throw new ServletException("Book issuance error", e);
        }
    }
}
