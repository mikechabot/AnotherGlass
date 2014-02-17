<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:import url="/WEB-INF/views/header.jsp"/>
<c:import url="/WEB-INF/views/navigation.jsp"/>
<div class="container">
	<div class="row">
		<div class="col-md-4">
			<img src="${user.avatarUrl}" width="220" height="220" alt="avatar image">
			<div class="row">
				<h1>Change Your Avatar</h1>
				<div class="row">
					<div class="col-sm-4"><img src="${user.gravatarAvatarUrl}" width="60" height="60" alt="Gravatar"></div>
					<div class="col-sm-8"><a href="/user/avatar-source/gravatar">Gravatar</a></div>
				</div>
				<c:if test="${not empty user.userAvatar}">
				<div class="row">
					<div class="col-sm-4"><img src="${user.localAvatarUrl}" width="60" height="60" alt="Uploaded"></div>
					<div class="col-sm-8"><a href="/user/avatar-source/local">Uploaded</a></div>
				</div>
				</c:if>
				<button onclick="window.location='/user/avatar-upload'">Upload a new avatar</button>
				<button onclick="window.location='/user/change-password'">Change password</button>
			</div>
		</div>
		<div class="col-md-8">
			<h2>${user.username}</h2>
			<hr/>
			<div class="row">
				<div class="col-xs-2">Member Since</div>
				<div class="col-xs-10"><i><fmt:formatDate pattern="MM/dd/yyyy" value="${user.created}" /></i></div>
			</div>			
			<div class="row">
				<div class="col-xs-2">Email</div>
				<div class="col-xs-10"><i>${user.email}</i></div>
			</div>			
		</div>
	</div>
</div>
<c:import url="/WEB-INF/views/footer.jsp"/>