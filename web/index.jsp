<%@ page language="java"
	import="java.util.List"
	import="java.util.ArrayList"
	import="java.sql.Connection"
	import="com.anotherglass.postgres.PostgresConnection"
	import="com.anotherglass.postgres.CreateWinesTable"
%>
<% boolean running = false; 

   String testConnection = request.getParameter("testConnection");
   String createTable = request.getParameter("createTable");
   
   if(testConnection != null && testConnection.length() > 1) {
	   Connection connection = PostgresConnection.getPostgresConnection();
	   if(connection != null) {
		   running = true;
	   }
   }
   if(createTable != null && createTable.length() > 1) {
	   if(CreateWinesTable.create()) {
		   running = true;
	   }
   }   
%>
<!DOCTYPE HTML>
<html>
<title>Test</title>
<body>
<h2>RUNNING: <%=running%></h2>
<small>Let's do it</small><br>
<form method="POST">
<input type="hidden" name="testConnection" value="testConnection">
<input type="submit" value="Test Connection">
</form>
<form method="POST">
<input type="hidden" name="createTable" value="createTable">
<input type="submit" value="Create Table">
</form>
</body>
</html>