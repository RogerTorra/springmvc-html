<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<html>
<body>
<h2>Favorites List</h2>

<a href="/actes/"><h3>List</h3></a>

<ul>
    <c:if test="${not empty favorites}">
        <c:forEach var="favorites" items="${favorites}">
            <li><a href="/favorite/${favorites.getId()}">${favorites.getId()}</a>: ${fn:escapeXml(favorites.toString())}</li>
        </c:forEach>
    </c:if>

    <c:if test="${empty favorites}">

        <H1>No tens actes favorits</H1>

    </c:if>

</ul>
</body>
</html>