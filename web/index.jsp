<%@ page language="java"
	import="java.util.List"
	import="java.util.ArrayList"
	import="com.anotherglass.solr.worker.ImportCatalog"
%>
<% String doRun = request.getParameter("doRun");
   boolean running = false;
   if(doRun != null && doRun.length() > 1) {
	   ImportCatalog catalog = new ImportCatalog();
	   catalog.run();
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