package com.library.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReturnBookServlet
 */
@WebServlet("/ReturnBookServlet")
public class ReturnBookServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	response.setContentType("text/html");
		PrintWriter out = response.getWriter();
    	
    	out.println("<!doctype html>");
		out.println("<html> <head> <title> Return A Book </title></head>");
		out.println("<h2>Return A Book</h2>");
		out.println("<form action='ReturnBookServlet' method='post'>");
		out.println("Issue Id: <input type='text' name='issue_id' id='issue_id' required><br><br>");
		out.println("Book Id: <input type='text' name='book_id' id='book_id' required><br><br>");
		
		out.println("<input type='submit' value='Submit'/>");
		out.println("</form></body></html>");
		out.println("<body>");
		
	}

    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int issueId = Integer.parseInt(request.getParameter("issue_id"));
        int bookId = Integer.parseInt(request.getParameter("book_id"));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String returnSql = "UPDATE issued_books SET return_date = CURDATE(), status = 'Returned' WHERE issue_id = ?";
            PreparedStatement returnStmt = conn.prepareStatement(returnSql);
            returnStmt.setInt(1, issueId);
            returnStmt.executeUpdate();

            String updateSql = "UPDATE books SET available_quantity = available_quantity + 1, issued_quantity = issued_quantity - 1 WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, bookId);
            updateStmt.executeUpdate();

            response.sendRedirect("BooksServlet");
        } catch (SQLException e) {
            throw new ServletException("Book return error", e);
        }
    }
}
