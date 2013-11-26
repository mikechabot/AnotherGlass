<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:import url="/WEB-INF/views/header.jsp"/>
<c:import url="/WEB-INF/views/navigation.jsp"/>

<div id="container-error" class="container">
	<div class="jumbotron">
	  	<h1>Page Not Found</h1>
	  	<p>Sorry, but we couldn't find the page you requested.<br>(Error 404)</p>
	  	<p><a href="/index.jsp" class="btn btn-primary btn-lg" role="button">Return Home</a></p>
	</div>
</div>

<c:import url="/WEB-INF/views/footer.jsp"/>