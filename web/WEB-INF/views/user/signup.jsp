<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:import url="/WEB-INF/views/header.jsp"/>
<c:if test="${not empty errors}">
<h2>Errors Found</h2>
<ul>
        <c:forEach var="each" items="${errors}">
                <li>${each}</li>
        </c:forEach>
</ul>
</c:if>
<form method="POST" action="/user/create">
        <fieldset>
        <label>Username</label>
        <input type="text" name="username" value="${username}" />
        <br/>
        <label>Password</label>
        <input type="password" name="password_1" value="${password_1}" />
        <br/>
        <label>Password Again</label>
        <input type="password" name="password_2" value="${password_2}" />
        <br/>
        <label>Email Address</label>
        <input type="email" name="email" value="${email}" />
        <input type="submit" value="Submit" />
        </fieldset>
</form>
<c:import url="/WEB-INF/views/footer.jsp"/>