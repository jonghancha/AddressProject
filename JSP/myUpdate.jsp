<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	String user_userId = request.getParameter("user_userId");
	String userName = request.getParameter("userName");
	String userPhone = request.getParameter("userPhone");
	String userEmail = request.getParameter("userEmail");	
		
//------
	String url_mysql = "jdbc:mysql://localhost/AddressBook?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";

	PreparedStatement ps = null;
	try{
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
	    Statement stmt_mysql = conn_mysql.createStatement();
	
	String A = "update user set userName = ?, userPhone = ?, userEmail = ?";
        String B = " where userId = '" + user_userId + "'";
	
	    ps = conn_mysql.prepareStatement(A+B);
	    
	    ps.setString(1, userName);
	    ps.setString(2, userPhone);
	    ps.setString(3, userEmail);
	    
	    ps.executeUpdate();
	
	    conn_mysql.close();
	} 
	
	catch (Exception e){
	    e.printStackTrace();
	}
	
%>

