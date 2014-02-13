<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:import url="/WEB-INF/views/header.jsp"/>
<c:import url="/WEB-INF/views/navigation.jsp"/>
<div class="container">
	<div class="row">
		<div class="col-md-8">
			<h2>${wine.name}</h2>
			<hr/>
			<div class="row">${wine.description}</div>
			<div class="row">
				<div class="col-xs-2">Tracking Since</div>
				<div class="col-xs-10"><i><fmt:formatDate pattern="MM/dd/yyyy" value="${wine.created}" /></i></div>
			</div>			
			<div class="row">
				<div class="col-xs-2">Vineyard</div>
				<div class="col-xs-10"><i><a href="/vineyard/${wine.vineyard.id}">${wine.vineyard.name}</a></i></div>
			</div>
			<div class="row">
				<div class="col-xs-2">Vintage</div>
				<div class="col-xs-10"><i><c:out value="${wine.vintage}">Unknown</c:out></i></div>
			</div>
			<div class="row">
				<div class="col-xs-2">Type</div>
				<div class="col-xs-10"><i><c:out value="${wine.type}">Unknown</c:out></i></div>
			</div>			
		</div>
		<div class="col-md-4 sidebar"></div>
	</div>
</div>
<c:import url="/WEB-INF/views/footer.jsp"/>