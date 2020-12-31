<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- test 폴더에 넣어주세요  --%>
<%
    request.setCharacterEncoding("utf-8");
    String searchText = request.getParameter("search_text");
String user_userId = request.getParameter("user_userId");

	String url_mysql = "jdbc:mysql://localhost/AddressBook?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
<<<<<<< HEAD
    String WhereDefault = "select addressName, addressPhone, addressGroup, addressEmail, addressText, addressBirth, addressImage, addressStar, addressNo 				from address";
=======
    String WhereDefault = "select addressName, addressPhone, addressGroup, addressEmail, addressText, addressBirth, addressImage, addressStar, addressNo from address";
>>>>>>> 97aeacd0b79c63be177a224db7cbf74a1e3bf642

    String Condition = " where (addressName LIKE '%" + searchText + "%' OR " +
                        "addressPhone LIKE '%" + searchText + "%' OR " +
                        "addressGroup LIKE '%" + searchText + "%' OR " +
			"addressEmail LIKE '%" + searchText + "%') AND " +
			"addressName is NOT NULL AND user_userId = '" + user_userId + "'";
    int count = 0;
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();

        ResultSet rs = stmt_mysql.executeQuery(WhereDefault + Condition); // 
%>
		{ 
  			"address_select"  : [ 
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
			"addressImage" : "<%=rs.getString(7) %>",  
			"addressStar" : "<%=rs.getString(8) %>",
<<<<<<< HEAD
			"addressNo" : "<%=rs.getString(9) %>"
=======
            "addressNo" : "<%=rs.getString(9) %>"
>>>>>>> 97aeacd0b79c63be177a224db7cbf74a1e3bf642
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
