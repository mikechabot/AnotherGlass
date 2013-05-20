<%@ page language="java"
	import="java.util.List"
	import="java.util.ArrayList"
	import="java.sql.Connection"
	import="com.anotherglass.postgres.Manager"
%>
<%  String runImport = request.getParameter("runImport"); 
	 
   Manager manager = Manager.getInstance();
   boolean running = manager.getRunning();
   boolean success = manager.getSuccess();
   
   if(runImport != null && runImport.length() > 1) {
	   manager.runImport();
   }
   
%>
<!DOCTYPE HTML>
<html>
<title>Test</title>
<body>
<h3>RUNNING: <%=running%></h3>
<h3>SUCCESS: <%=success%></h3>
<small>Let's do it</small><br>
<form method="POST">
<input type="hidden" name="runImport" value="runImport">
<input type="submit" value="Run Import">
</form>
</body>
</html>