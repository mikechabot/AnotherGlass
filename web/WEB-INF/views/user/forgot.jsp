<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:import url="/WEB-INF/views/header.jsp"/>
<c:import url="/WEB-INF/views/navigation.jsp"/>
<div id="container-signup" class="container">
	<c:if test="${not empty errors}">
	<h2>Errors Found</h2>
	<span class="text-danger error">
	<ul>
	        <c:forEach var="each" items="${errors}">
	                <li>${each}</li>
	        </c:forEach>
	</ul>
	</span>
	</c:if>
	<h2>Forgot Password</h2>
	<form role="form" name="signup" action="/user/forgot" method="post">
		<div class="form-group">
			<label for="password">Username or Email</label>
			<input type="text" class="form-control" name="email" placeholder="Username or Email" value="${email}">
		</div>
		<button type="submit" class="btn btn-default">Submit</button>
	</form>
</div>
<c:import url="/WEB-INF/views/footer.jsp"/>