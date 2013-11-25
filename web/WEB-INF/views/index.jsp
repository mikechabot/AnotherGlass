<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:import url="/WEB-INF/views/header.jsp"/>
<c:set var="results" value="${query.results}"/>

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
          <a class="navbar-brand" href="/">AnotherGlass</a>
        </div>
		<!-- Collect the nav links, forms, and other content for toggling -->
	      <ul class="nav navbar-nav">
	        <li class="active"><a href="/">Home</a></li>
	        <li><a href="#">The Cellar</a></li>
	        <li><a href="#">Top Rated</a></li>
	      </ul>
        <div class="navbar-collapse collapse">
          <form class="navbar-form navbar-right" action="/search" method="get">
			<c:choose>
				<c:when test="${query.type == 'vineyards'}">
					<input type="hidden" name="type" value="vineyards">
				</c:when>
				<c:when test="${query.type == 'regions'}">
					<input type="hidden" name="type" value="regions">
				</c:when>
			</c:choose>
            <div class="form-group">
              <input type="text" name="q" placeholder="What are you sipping?" class="form-control">
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
				<div class="col-xs-12 col-md-9">
				<br>
				<div class="panel panel-default">
					<c:if test="${not empty results}">
						<div class="panel-heading"><h2>Search Results</h2>
						<span class="text-muted pull-right">Total Results: ${results.size()}</span>	
						</div>
					</c:if>		
					<!-- Search Toggle -->
					<table id="search-toggle" class="table text-center">
						<thead>
						<tr>
						<c:choose>
							<c:when test="${empty query.type}">
								<th class="active-toggle border-right"><a href="/search?q=${query.query}">Wines</a></th>
								<th class="border-right"><a href="/search?q=${query.query}&type=vineyards">Vineyards</a></th>
								<th><a href="/search?q=${query.query}&type=regions">Regions</a></th>
							</c:when>
							<c:when test="${query.type == 'vineyards'}">
								<th class="border-right"><a href="/search?q=${query.query}">Wines</a></th>
								<th class="active-toggle border-right"><a href="/search?q=${query.query}&type=vineyards">Vineyards</a></th>
								<th><a href="/search?q=${query.query}&type=regions">Regions</a></th>
							</c:when>
							<c:when test="${query.type == 'regions'}">
								<th class="border-right"><a href="/search?q=${query.query}">Wines</a></th>
								<th class="border-right"><a href="/search?q=${query.query}&type=vineyards">Vineyards</a></th>
								<th class="active-toggle"><a href="/search?q=${query.query}&type=regions">Regions</a></th>
							</c:when>
						</c:choose>
						</tr>
						</thead>
					</table>
					
					<div class="panel-body">
					<!-- Search Results -->
					<c:choose>
						<c:when test="${fn:length(results) > 0}">
							<c:choose>
							<c:when test="${empty query.type}">
								<table id="results" class="tablesorter table">
								<thead>
								<tr>
					     			<th>Name</th>
					     			<th>Vineyard</th>
					     			<th>Appellation</th>
					     			<th>Region</th>
					     			<th>Starred</th>
					     		</tr>
					     		</thead>
					     		<tbody>
							    <c:forEach  var="wine" items="${results}">
									<c:if test="${not empty wine}">
									<tr>
									<td><a href="${wine.url}">${wine.name}</a></td>
									<td><c:out value="${wine.vineyard.name}">Unknown</c:out></td>
									<td><c:out value="${wine.appellation.name}">Unknown</c:out></td>
									<td><c:out value="${wine.appellation.region.name}">Unknown</c:out></td>
									<td>
										<button type="button" class="btn btn-default btn-xs">
	 											<span class="glyphicon glyphicon-star"></span>
										</button>
									</td>
									</tr>
									</c:if>
								</c:forEach>
							 	</tbody>
							</table>
							</c:when>
							<c:when test="${query.type == 'vineyards'}">
								<table id="results" class="tablesorter table">
								<thead>
								<tr>					     			
					     			<th>Vineyard</th>
					     			<th>Appellation</th>
					     			<th>Starred</th>
					     		</tr>
					     		</thead>
					     		<tbody>
							    <c:forEach  var="vineyard" items="${results}">
									<c:if test="${not empty vineyard}">
									<tr>
									<td><a href="${vineyard.url}">${vineyard.name}</a></td>
									<td><c:out value="${vineyard.appellation.name}">Unknown</c:out></td>
									<td>
										<button type="button" class="btn btn-default btn-xs">
	 											<span class="glyphicon glyphicon-star"></span>
										</button>
									</td>
									</tr>
									</c:if>
								</c:forEach>
							 	</tbody>
							</table>
							</c:when>
							<c:when test="${query.type == 'regions'}">
								<table id="results" class="tablesorter table">
								<thead>
								<tr>					     			
					     			<th>Appellation</th>
					     			<th>Region</th>
					     			<th>Wine Count</th>
					     			<th>Starred</th>
					     		</tr>
					     		</thead>
					     		<tbody>
							    <c:forEach  var="appellation" items="${results}">
									<c:if test="${not empty appellation}">
									<tr>
									<td><a href="${appellation.region.url}">${appellation.name}</a></td>
									<td>${appellation.region.name}</td>
									<td>${appellation.wineCount}</td>
									<td>
										<button type="button" class="btn btn-default btn-xs">
	 											<span class="glyphicon glyphicon-star"></span>
										</button>
									</td>
									</tr>
									</c:if>
								</c:forEach>
							 	</tbody>
							</table>
							</c:when>
							</c:choose>
						</c:when>						
						<c:otherwise>
						<!-- No Search Results -->
						<br>
						<div class="container container-half">
							<div class="panel panel-default">
								<div class="panel-body">
								 	<table id="no-results" class="table">
								 		<tr>
								 		<c:choose>
											<c:when test="${empty query.type}">
												<th colspan="2">Wine Search</th>
											</c:when>
											<c:when test="${query.type == 'vineyards'}">
												<th colspan="2">Vineyard Search</th>
											</c:when>
											<c:when test="${query.type == 'regions'}">
												<th colspan="2">Region Search</th>
											</c:when>
										</c:choose>
								 		</tr>
										<c:choose>								 		
									 		<c:when test="${not empty query.query}">
									 		<tr>
									 			<td><span class="glyphicon glyphicon-search big-icon text-muted"></span></td>
									 			<td>Sorry, no results found for "${query.query}"</td>
									 		</tr>
									 		</c:when>
									 		<c:otherwise>
										 	<tr>
									 			<td><span class="glyphicon glyphicon-search big-icon text-muted"></span></td>
									 			<td>
												<form role="form" action="/search" method="get">
										 		<c:choose>
													<c:when test="${query.type == 'vineyards'}">
														<input type="hidden" name="type" value="vineyards">
													</c:when>
													<c:when test="${query.type == 'regions'}">
														<input type="hidden" name="type" value="regions">
													</c:when>
												</c:choose>
													<div class="form-group">
														<input type="text" name="q" class="form-control" placeholder="Enter search">
													</div>
													<button type="submit" class="btn btn-default">Submit</button>
												</form>
												</td>
									 		</tr>
									 		</c:otherwise>
								 		</c:choose>
								 	</table>
								</div>
							</div>
						</div>
						</c:otherwise>
					</c:choose>
					</div>
				</div>	<!-- /panel -->
				</div>	<!-- /col-xs-12 col-md-9 -->
				<div class="col-xs-6 col-md-3">
				<br>
				<div class="panel panel-default">
					<div class="panel-heading"><h4>Trending</h4>
						<table class="table"><tr><td>Content for days!</td></tr></table>
					</div>
				</div>		<!-- /panel -->
				</div>		<!-- /col-xs-6 col-md-3 -->
			</div> 	<!-- /row -->
	    </div> 	<!-- /container -->
    </div> 		<!-- /container-thin -->
    
<c:import url="/WEB-INF/views/footer.jsp"/>