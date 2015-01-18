<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<body>

<p><a href="/favorite">Favorite</a></p>

<c:if test="${not empty favorite}">
    <h2>Acte id ${favorite.getId()}</h2>
    <h3>${favorite.getActe().getName()}</h3>
    <p>Lloc: ${fn:escapeXml(favorite.getActe().getLocalization())}</p>
    <p>${fn:escapeXml(favorite.getActe().getInit_date())}</p>
    <p>${fn:escapeXml(favorite.getActe().getType())}</p>
    <p>${fn:escapeXml(favorite.getActe().getStreet())}</p>
    <p>${fn:escapeXml(favorite.getActe().getCP())}</p>

    <h2>User</h2>

    <p>${fn:escapeXml(favorite.getUser().getUsername())}</p>
    <p>${fn:escapeXml(favorite.getUser().getEmail())}</p>

    <h2>Date</h2>

    <p>${fn:escapeXml(favorite.getRecordHour())}</p>
</c:if>

</body>
</html>
