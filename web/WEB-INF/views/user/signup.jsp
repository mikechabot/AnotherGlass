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
	<h2>Sign Up</h2>
	<form role="form" name="signup" action="/user/create" method="post">
		<div class="form-group">
			<label for="username">Username</label>
			<input type="text" class="form-control" name="username" placeholder="Username" value="${username}">
		</div>
		<div class="form-group">
			<label for="password">Password</label>
			<input type="password" class="form-control" name="password_1" placeholder="Password" value="${password_1}">
		</div>
		<div class="form-group">
			<label for="password">Verify Password</label>
			<input type="password" class="form-control" name="password_2" placeholder="Password">
		</div>	
		<div class="form-group">
			<label for="email">E-mail</label>
			<input type="email" class="form-control" name="email" placeholder="E-mail" value="${email}">
		</div>	
		<button type="submit" class="btn btn-default">Submit</button>
	</form>
	<c:if test="${not empty loginFailure}">
 	<p class="text-danger error">The credentials you entered were incorrect.</p>
	</c:if>
</div>
<c:import url="/WEB-INF/views/footer.jsp"/>