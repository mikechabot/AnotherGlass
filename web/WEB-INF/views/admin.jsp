<%@ page language="java"
	import="com.anotherglass.vino.Job"
	import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/css/bootstrap-responsive.min.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/css/custom.css" media="screen"/>
<script src="/js/bootstrap.min.js"></script>
<title>Admin Tools</title>
</head>
<body class="admin">
<div class="container">
	<h1>Admin Tools</h1>
	<c:if test="${fn:length(jobs) > 0}">
   		<table class="table table-nonfluid pull-right">
   			<tr>
   				<td>${fn:length(jobs)}</td>
   				<th>Job Count</th>
   			</tr>
   			<tr>
   				<td>${running}</td>
   				<th>Running</th>
   			</tr>
   		</table>
   		<table class="table">
   			<tr>
	   			<th>Job Name</th>
	   			<th>Description</th>
	   			<th>Running?</th>
	   			<th>Action</th>
   			</tr>
   			<c:forEach var="entry" items="${jobs}">
   			<tr>
  				<td><c:out value="${entry.key}"/></td>
 				<td><c:out value="${entry.value.getJobDescription()}"/></td>
 				<c:choose>
        			<c:when test="${entry.value.isRunning() == true}">
            			<td>Yes</td>
        			</c:when>
        			<c:otherwise>
							<td>No</td>
        			</c:otherwise>
    			</c:choose>
 				<td><form method="post" action="${entry.value.getJobServletUrl()}"><button type="submit" class="btn btn-default">Run</button></form></td>
 			</tr>
			</c:forEach>		
		</table>
   	
   	</c:if>
   	<c:if test="${fn:length(jobs) == 0}">
		No jobs found!
   	</c:if>
</div>
</body>
</html>