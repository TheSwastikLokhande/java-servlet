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
 * Servlet implementation class AddBookServlet
 */
@WebServlet("/AddBookServlet")
public class AddBookServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");
		PrintWriter out = response.getWriter();
    	
    	out.println("<!doctype html>");
		out.println("<html> <head> <title> Add New Book </title></head>");
		out.println("<body>");
		
		out.println("<h2>Add new Book</h2>");
		out.println("<form action='AddBookServlet' method='post'>");
		out.println("Name: <input type='text' name='title' id='title' required><br><br>");
		out.println("Author: <input type='text' name='author' id='author' required><br><br>");
		out.println("Publisher: <input type='text' name='publisher' id='publisher' required><br><br>");
		out.println("Year: <input type='text' name='year' required><br><br>");
		out.println("Total Quantity: <input type='text' name='total_quantity' required><br><br>");
		out.println("<input type='submit' value='Submit'/>");
		out.println("</form></body></html>");
		
		
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       System.out.println("in post");
		String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        int year = Integer.parseInt(request.getParameter("year"));
        int totalQuantity = Integer.parseInt(request.getParameter("total_quantity"));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
        	
        	System.out.println(title+","+ author+","+ publisher+","+ year+","+totalQuantity+","+ totalQuantity);
            String sql = "INSERT INTO books (title, author, publisher, year, total_quantity, available_quantity) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, publisher);
            stmt.setInt(4, year);
            stmt.setInt(5, totalQuantity);
            stmt.setInt(6, totalQuantity);
            
            stmt.executeUpdate();
            response.sendRedirect("BooksServlet"); 
        } catch (SQLException e) {
            throw new ServletException("Error adding book", e);
        }
    }
}
