<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
    <link href="/static/css/station2.css" rel="stylesheet">
</head>
<body>
<jsp:include page="header.jsp"/>


<c:forEach items="${requestScope.result}" var="way">
    <c:if test="${way!=null}">

    </c:if>

    <c:if test="${way==null}">
        <h5>No</h5>
    </c:if>
</c:forEach>
</body>
</html>
