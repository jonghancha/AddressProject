<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	String userid = request.getParameter("id");
	String userpw = request.getParameter("pw");
	String username = request.getParameter("name");
	String userphone = request.getParameter("phone");	
	String useremail = request.getParameter("email");	
		
//------
	String url_mysql = "jdbc:mysql://localhost/AddressBook?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";

	int result2 = 0; // 입력 확인 

	PreparedStatement ps = null;
	try{
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
		Statement stmt_mysql = conn_mysql.createStatement();
	
	    String A = "insert into user (userId, userPw, userName, userPhone, userEmail";
	    String B = ") values (?,?,?,?,?)";
	
	    ps = conn_mysql.prepareStatement(A+B);
	    ps.setString(1, userid);
	    ps.setString(2, userpw);
	    ps.setString(3, username);
		ps.setString(4, userphone);
		ps.setString(5, useremail);
		
		result2 = ps.executeUpdate();
%>
		{
			"result2" : "<%=result2%>"
		}

<%		
	    conn_mysql.close();
	} 
	catch (Exception e){
%>
		{
			"result2" : "<%=result2%>"
		}
<%		
	    e.printStackTrace();
	} 
	
%>

