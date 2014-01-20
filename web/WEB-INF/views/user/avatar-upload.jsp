<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:import url="/WEB-INF/views/header.jsp"/>
<c:import url="/WEB-INF/views/navigation.jsp"/>
<div class="container">
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
	<h2>Upload a new avatar</h2>
	<form role="form" action="/user/avatar-upload" method="post" enctype="multipart/form-data">
		<div class="form-group">
			<label for="file">File</label>
			<input type="file" class="form-control" name="file" placeholder="Username">
		</div>
		<button type="submit" class="btn btn-default">Submit</button>
	</form>
</div>
<c:import url="/WEB-INF/views/footer.jsp"/>