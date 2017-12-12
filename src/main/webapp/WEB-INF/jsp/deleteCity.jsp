<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Delete City</title>
    <link href="/static/css/city2.css" rel="stylesheet"/>
</head>
<body>
<jsp:include page="headerAdmin.jsp"/>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-4">
        </div>
        <div class="col-md-4">
            <div class="col-md-12">
                <h4>If you remove the city ${requestScope.city} you remove trains:</h4>
                <h4> you remove trains:</h4>
                <c:forEach items="${requestScope.trains}" var="train">
                    <h5>${train.getName()}</h5>
                </c:forEach>
            </div>
            <a class="col-md-6" href="/admin/home">
                <input class="button-main" type="button" value="Cancel" >
            </a>
            <a class="col-md-6" href="/admin/deleteCity?name=${requestScope.city}&delete=true">
                <input class="button-main" type="button" value="Delete">
            </a>
        </div>
        <div class="col-md-4">
        </div>
    </div>
</div>
</body>
</html>
