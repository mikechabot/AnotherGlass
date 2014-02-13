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
			<h2>${appellation.name}</h2>
			<hr/>
			<div class="row">
				<div class="col-xs-2">Member Since</div>
				<div class="col-xs-10"><i><fmt:formatDate pattern="MM/dd/yyyy" value="${appellation.created}" /></i></div>
			</div>			
			<div class="row">
				<div class="col-xs-2">Region</div>
				<div class="col-xs-10"><i><a href="/region/${appellation.region.id}">${appellation.region.name}</a></i></div>
			</div>
			<div class="row">
				<div class="col-xs-2">Wine Count</div>
				<div class="col-xs-10"><i>${appellation.wineCount}</i></div>
			</div>
			<hr/>
			<h4>Wines</h4>
			<table id="results" class="tablesorter table">
			<thead>
			<tr>
     			<th>Name</th>
     			<th>Vineyard</th>
     			<th>Starred</th>	
			</tr>
			</thead>
     		<tbody>
		    <c:forEach  var="wine" items="${appellation.wines}">
				<tr>
				<td><a href="/wine/${wine.id}">${wine.name}</a></td>
				<td><a href="/vineyard/${wine.vineyard.id}"><c:out value="${wine.vineyard.name}">Unknown</c:out></a></td>
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
	</div>
</div>
<c:import url="/WEB-INF/views/footer.jsp"/>