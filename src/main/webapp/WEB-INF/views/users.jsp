<%--
  Created by IntelliJ IDEA.
  User: hellfish90
  Date: 19/01/15
  Time: 23:09
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<html>
<body>
<h2>Users List</h2>
<ul>
    <c:if test="${not empty users}">
        <c:forEach var="user" items="${users}">
            <li><a href="/users/${user.getId()}">${user.toString()}</a></li>
        </c:forEach>
    </c:if>
</ul>
</body>
</html>