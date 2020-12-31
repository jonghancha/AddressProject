<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	String userid = request.getParameter("id");
	String username = request.getParameter("name");
	String useremail = request.getParameter("email");
		
//------
	String url_mysql = "jdbc:mysql://localhost/AddressBook?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";
	String WhereDefault = "select userPw from user where userId='" + userid+ "' && userName ='" + username+ "' && userEmail ='" + useremail + "' && userDeletedata ='2020-01-01 00:00:00'";

	int count =0;
	int result = 0; 

	PreparedStatement ps = null;
	try{
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
		Statement stmt_mysql = conn_mysql.createStatement();
	
		ResultSet rs = stmt_mysql.executeQuery(WhereDefault); 
		%>
				{ 
					  "user_info"  : [ 
		<%
				while (rs.next()) {
					if (count == 0) {
		
					}else{
		%>
					, 
		<%
					}
		%>            
					{
					"result2" : "<%=rs.getString(1) %>" 

					}
		
		<%		
				count++;
				}
		%>
				  ] 
				} 
		<%		
				conn_mysql.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		%>
		