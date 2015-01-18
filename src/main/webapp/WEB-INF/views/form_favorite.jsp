<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title>Greeting Form</title>
</head>
<body>

<c:choose>
    <c:when test="${favorite==null}">
        <h3>Update favorite</h3>
        <c:set var="method" value="PUT"/>
        <c:set var="action" value="/favorite/${favorite.getId()}"/>
    </c:when>
    <c:otherwise>
        <h3>Create favorite</h3>
        <c:set var="method" value="POST"/>
        <c:set var="action" value="/favorite"/>
    </c:otherwise>
</c:choose>

<form:form method="${method}" action="${action}" modelAttribute="favorite">
    <table>

        <tr>
            <td><input type="submit" value="Submit" /></td>
        </tr>
    </table>
</form:form>

</body>
</html>
