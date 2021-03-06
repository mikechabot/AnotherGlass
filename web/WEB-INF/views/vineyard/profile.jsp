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
			<h2>${vineyard.name}</h2>
			<hr/>
			<div class="row">
				<div class="col-xs-2">Member Since</div>
				<div class="col-xs-10"><i><fmt:formatDate pattern="MM/dd/yyyy" value="${vineyard.created}" /></i></div>
			</div>			
			<div class="row">
				<div class="col-xs-2">Appellation</div>
				<div class="col-xs-10"><i><a href="/appellation/${vineyard.appellation.id}">${vineyard.appellation.name}</a></i></div>
			</div>
			<hr/>	
			<h4>Wines</h4>
			<table id="results" class="tablesorter table">
			<thead>
			<tr>
     			<th>Name</th>
     			<th>Type</th>
     			<th>Vintage</th>
     			<th>Appellation</th>
     			<th>Region</th>
     			<th>Starred</th>	
			</tr>
			</thead>
     		<tbody>
		    <c:forEach  var="wine" items="${vineyard.wines}">
				<tr>
				<td><a href="/wine/${wine.id}">${wine.name}</a></td>
				<td>${wine.type}</td>
				<td>${wine.vintage}</td>
				<td><a href="/appellation/${wine.appellation.id}"><c:out value="${wine.appellation.name}">Unknown</c:out></a></td>
				<td><a href="/region/${wine.region.id}"><c:out value="${wine.region.name}">Unknown</c:out></a></td>
				<td>
					<button type="button" class="btn btn-default btn-xs">
								<span class="glyphicon glyphicon-star"></span>
					</button>
				</td>
				</tr>
			</c:forEach>
		 	</tbody>			
			</table>
		</div>					
		<div class="col-md-4 sidebar"></div>
	</div>
</div>
<c:import url="/WEB-INF/views/footer.jsp"/>