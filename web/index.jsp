<%@ page language="java"
	import="java.util.List"
	import="java.util.ArrayList"
	import="java.sql.Connection"
	import="com.anotherglass.utils.PostgresConnection"
%>
<% String doRun = request.getParameter("doRun");
   boolean running = false;
   if(doRun != null && doRun.length() > 1) {
	   Connection connection = PostgresConnection.getPostgresConnection();
	   if(connection != null) {
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
<input type="hidden" name="doRun" value="doRun">
<input type="submit">
</form>
</body>
</html>