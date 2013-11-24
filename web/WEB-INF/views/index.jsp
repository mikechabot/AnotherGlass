<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:import url="/WEB-INF/views/header.jsp"/>

	<!-- Navigation -->
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">AnotherGlass</a>
        </div>
		<!-- Collect the nav links, forms, and other content for toggling -->
	      <ul class="nav navbar-nav">
	        <li class="active"><a href="#">Home</a></li>
	        <li><a href="#">The Cellar</a></li>
	        <li><a href="#">Top Rated</a></li>
	      </ul>
        <div class="navbar-collapse collapse">
          <form class="navbar-form navbar-right" action="/search" method="get">
            <div class="form-group">
              <input type="text" name="query" placeholder="What are you sipping?" class="form-control">
            </div>
            <button type="submit" class="btn btn-default">Search</button>
          </form>
        </div><!--/.navbar-collapse -->
      </div>
    </div>
    
   	<!-- Body -->
   	<div class="container container-thin">
	    <div class="container">
	    <div class="row">
			<div class="col-xs-12 col-md-8">
			<br>
			<div class="panel panel-default">
			<c:choose>
				<c:when test="${fn:length(wines) > 0}">
				<div class="panel-heading"><h4>Found <c:out value="${wines.size()}"/> wines</h4></div>
				<table id="results" class="tablesorter table">
					<thead>
					<tr>
		     			<th class="header">Name</th>
		     			<th class="header">Appellation</th>
		     			<th class="header">Region</th>
		     		</tr>
		     		</thead>
		     		<tbody>
				    <c:forEach  var="wine" items="${wines}">
						<c:if test="${not empty wine}">
							<tr>
							<td><a href="${wine.url}">${wine.name}</a></td>
							<td><c:out value="${wine.appellation.name}">Unknown</c:out></td>
							<td><c:out value="${wine.appellation.region.name}">Unknown</c:out></td>
							</tr>
						</c:if>
					</c:forEach>
				 	</tbody>
				</table>
				</c:when>
				<c:otherwise>
				<h4>No wines found</h4>
				</c:otherwise>
			</c:choose>
		     </div>	
			</div>
			<div class="col-xs-6 col-md-4">Trending</div>
		</div>
	    </div> <!-- /container -->
    </div> <!-- /container-thin -->
    
<c:import url="/WEB-INF/views/footer.jsp"/>