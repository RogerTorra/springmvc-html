<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<body>

<p><a href="/actes">Actes</a></p>

<c:if test="${not empty acte}">
    <h2>Acte id ${acte.getId()}</h2>
    <h3>${acte.getName()}</h3>
    <p>Lloc: ${fn:escapeXml(acte.getLocalization())}</p>
    <p>${fn:escapeXml(acte.getInit_date())}</p>
    <p>${fn:escapeXml(acte.getType())}</p>
    <p>${fn:escapeXml(acte.getStreet())}</p>
    <p>${fn:escapeXml(acte.getCP())}</p>

</c:if>

</body>
</html>
