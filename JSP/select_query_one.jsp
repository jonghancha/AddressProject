<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    String modifyNo = request.getParameter("modifyNo");

	String url_mysql = "jdbc:mysql://localhost/addressbook?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
    String WhereDefault = "select addressName, addressPhone, addressGroup, addressEmail, addressText, addressBirth, addressImage from address where addressNo = '" + modifyNo +"'";
    int count = 0;
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();

        ResultSet rs = stmt_mysql.executeQuery(WhereDefault); // 
%>
		{ 
  			"address_info"  : [ 
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
			"addressName" : "<%=rs.getString(1) %>", 
			"addressPhone" : "<%=rs.getString(2) %>",   
			"addressGroup" : "<%=rs.getString(3) %>",  
			"addressEmail" : "<%=rs.getString(4) %>",
			"addressText" : "<%=rs.getString(5) %>",
			"addressBirth" : "<%=rs.getString(6) %>",
			"addressImage" : "<%=rs.getString(7) %>"
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
