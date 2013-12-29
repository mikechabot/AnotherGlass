<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:import url="/WEB-INF/views/header.jsp"/>
<c:import url="/WEB-INF/views/navigation.jsp"/>

<div id="container-login" class="container">
	<h2>Login <small>AnotherGlass</small></h2>
	<form role="form" name="loginform" action="" method="post">
		<div class="form-group">
			<label for="username">Username</label>
			<input type="text" class="form-control" name="username" placeholder="Username">
		</div>
		<div class="form-group">
			<label for="password">Password</label>
			<input type="password" class="form-control" name="password" placeholder="Password">
		</div>
		<div class="checkbox">
			<label><input type="checkbox" name="rememberMe"> Remember me</label>
		</div>
		<button type="submit" class="btn btn-default">Login</button>
		<button type="button" class="btn btn-default" onclick="window.location='/user/signup';">Sign up</button>
	</form>
	<c:if test="${not empty loginFailure}">
 	<p class="text-danger error">The credentials you entered were incorrect.</p>
	</c:if>
</div>
<c:import url="/WEB-INF/views/footer.jsp"/>