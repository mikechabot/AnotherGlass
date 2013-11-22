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
    <div class="container">
    <c:choose>
    	<c:when test="${fn:length(wines) > 0}">
		<h4>Found <c:out value="${wines.size()}"/> wines</h4>
		<table class="table table-striped">
       		<tr>
       			<th>Name</th>
       			<th>Description</th>
       			<th>Origin</th>
       			<th>Retail</th>
       			<th>Type</th>
       			<th>Vintage</th>
       			<th>Retail Price</th>
       		</tr>
	        <c:forEach  var="wine" items="${wines}">
				<c:if test="${not empty wine}">
					<tr>
	        			<td><a href="${wine.getUrl()}">${wine.getName()}</a></td>
	        			<td><c:out value="${wine.getDescription()}"/></td>
	        			<td><c:out value="${wine.getAppellation().getName()}"/></td>
	        			<td><c:out value="${wine.getRetail()}"/></td>
	        			<td><c:out value="${wine.getType()}"/></td>
	        			<td><c:out value="${wine.getVintage()}"/></td>
	        			<fmt:setLocale value="en_US"/>
	        			<td><fmt:formatNumber type="currency" value="${wine.getPriceRetail()}" /></td>
	        		</tr>
				</c:if>
	        </c:forEach>
        </table>     		
    	</c:when>
    	<c:otherwise>
          	<h4>No wines found</h4>
    	</c:otherwise>
    </c:choose>
    </div> <!-- /container -->
<c:import url="/WEB-INF/views/footer.jsp"/>