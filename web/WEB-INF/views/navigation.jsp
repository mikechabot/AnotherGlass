<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
	        <li><a href="/user/profile">Profile</a></li>
	      </ul>
        <div class="navbar-collapse collapse">
          <form class="navbar-form navbar-right" action="/search" method="get">
			<c:choose>
				<c:when test="${type == 'vineyards'}">
					<input type="hidden" name="type" value="vineyards">
				</c:when>
				<c:when test="${type == 'regions'}">
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