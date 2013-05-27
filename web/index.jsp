<%@ page language="java"
	import="java.util.List"
	import="java.util.ArrayList"
	import="java.sql.Connection"
	import="com.anotherglass.utils.Manager"
%>
<%  String runDbImport = request.getParameter("dbImport");
	String runSolrImport = request.getParameter("solrImport"); 
	String runDropSolrIndex = request.getParameter("dropSolr");
	 
   Manager manager = Manager.getInstance();
   boolean running = manager.getRunning();
   boolean success = manager.getSuccess();
   
   if(runDbImport != null && runDbImport.length() > 1) {
	   manager.runDbImport();
   }
   
   if(runSolrImport != null && runSolrImport.length() > 1) {
	   manager.runSolrImport();
   }
   
   if(runDropSolrIndex != null && runDropSolrIndex.length() > 1) {
	   manager.dropSolr();
   }
   
   
%>
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/home.css" media="screen"/>
<title>AG Manager</title>
</head>
<body>
<div id="container">
  	<div id="header">
	   	<h1>AG Manager</h1>
	</div>
   	<div id="body">
		<h3>RUNNING: <%=running%></h3>
		<h3>SUCCESS: <%=success%></h3>
   		<table id="manager">
   			<tr>
	   			<th>Command</th>
	   			<th>Description</th>
	   			<th>&nbsp;</th>
   			</tr>
			<tr>
				<td>
				Populate Postgres<br>								
				</td>
				<td>Drop 'wines' table, repopulate from wines.com API</td>
				<td>
				<form method="POST" onsubmit="return confirm('Do you really want to drop & reopulate the 'wines' table?');">
				<input type="hidden" name="dbImport" value="dbImport">
				<input type="submit" value="Run">
				</form>
				</td>
			</tr>
			<tr>
				<td>Populate Solr</td>
				<td>Populate Solr index with data from Postgres ('wines' table)</td>
				<td>
				<form method="POST" onsubmit="return confirm('Do you really want to reindex Solr?');">
				<input type="hidden" name="solrImport" value="solrImport">
				<input type="submit" value="Run">
				</form>
				</td>
			</tr>
			<tr>
				<td>Drop Solr</td>
				<td>Delete all document from the Solr index</td>
				<td>
				<form method="POST" onsubmit="return confirm('Do you really want to drop the Solr index?');">
				<input type="hidden" name="dropSolr" value="dropSolr">
				<input type="submit" value="Run">
				</form>
				</td>
			</tr>
		</table>
   	</div>
   	<div id="footer">
   	<a href="/solr/admin.html">Solr Admin</a></small>
   	</div>
</div>
</body>
</html>