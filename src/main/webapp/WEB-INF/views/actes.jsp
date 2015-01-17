<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<html>
<body>
<h2>Actes List</h2>
<ul>
    <c:if test="${not empty actes}">
        <c:forEach var="acte" items="${actes}">
            <li><a href="/actes/${acte.getId()}">${acte.getId()}</a>: ${fn:escapeXml(acte.getName())}</li>
        </c:forEach>
    </c:if>
</ul>
</body>
</html>