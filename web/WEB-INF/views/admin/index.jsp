<%@ page language="java"
	import="vino.job.Job"
	import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Admin Tools</title>
    <meta http-equiv="Refresh" content="15">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <!-- styles -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/custom.css" rel="stylesheet">
    
    <!-- fonts -->
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
  </head>
  <body class="strapless">
	<div class="container">
	<div class="page-header">
  		<h1>Admin Tools <small>AnotherGlass</small></h1>
	</div>
		<c:if test="${fn:length(jobs) > 0}">
	   		<table class="table table-bordered table-nonfluid pull-right">
	   			<tr>
	   				<td>${fn:length(jobs)}</td>
	   				<th>Job Count</th>
	   			</tr>
	   			<tr>
	   				<td>${running}</td>
	   				<th>Running</th>
	   			</tr>
	   		</table>
	   		<table class="table table-striped">
	   			<tr>
		   			<th>Job Name</th>
		   			<th>Description</th>
		   			<th>Status</th>
		   			<th>Last Run</th>
		   			<th>Last Run Duration</th>
		   			<th>Action</th>
	   			</tr>
	   			<c:forEach var="entry" items="${jobs}">
				<c:choose>
	       			<c:when test="${entry.value.isRunning() == true}">
		       			<tr class="success">
			  				<td><c:out value="${entry.key}"/></td>
			 				<td><c:out value="${entry.value.getJobDescription()}"/></td>
	            			<td class="text-success">Running</td>
			    			<c:choose>
			        			<c:when test="${empty entry.value.lastRunDate}">
			            			<td>None</td>
			            			<td>None</td>
			        			</c:when>
			        			<c:otherwise>
									<td><c:out value="${entry.value.lastRunDate}"/></td>
									<td><c:out value="${entry.value.lastRunDuration}"/></td>
			        			</c:otherwise>
			    			</c:choose>
	            			<td><button type="submit" class="btn btn-success btn-xs" disabled="disabled">Running</button></td>
		 				</tr>
	       			</c:when>
	       			<c:otherwise>
						<tr>
			  				<td><c:out value="${entry.key}"/></td>
			 				<td><c:out value="${entry.value.getJobDescription()}"/></td>
	            			<td>Idle</td>
			    			<c:choose>
			        			<c:when test="${empty entry.value.lastRunDate}">
			            			<td>None</td>
			            			<td>None</td>
			        			</c:when>
			        			<c:otherwise>
									<td><c:out value="${entry.value.lastRunDate}"/></td>
									<td><c:out value="${entry.value.lastRunDuration}"/></td>
			        			</c:otherwise>
			    			</c:choose>
	            			<td><form method="post" action="${entry.value.getJobServletUrl()}"><button type="submit" class="btn btn-primary btn-xs">Run Job</button></form></td>
		 				</tr>
	       			</c:otherwise>
	   			</c:choose>
				</c:forEach>
			</table>
			<span class="text-info"><strong>Notes:</strong></span>
			<ol class="text-info">
	  			<li>Last run time only accurate until the last application restart</li>
	  			<li>Page refreshes automatically every 15 seconds</li>
			</ol>
	   	</c:if>
	   	<c:if test="${fn:length(jobs) == 0}">
			No jobs found!
	   	</c:if>
	</div>
   </body>
</html>