<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>City</title>
    <jsp:include page="headerAdmin.jsp"/>
    <link href="../../static/css/city2.css" rel="stylesheet"/>
    <script src="/static/js/jquery-3.1.1.min.js"></script>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-4">
            <div class="col-md-12">
                <jsp:include page="adminAddTrain.jsp"/>
            </div>
        </div>
        <%--<div class="col-md-4">--%>
            <%--<div class="col-md-12">--%>
                <%--<jsp:include page="adminCitySeach.jsp"/>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="col-md-4">--%>
            <%--<div class="col-md-12">--%>
                <%--<c:if test="${requestScope.cityName!=null}">--%>
                    <%--<jsp:include page="adminCityEdit.jsp"/>--%>
                <%--</c:if>--%>
            <%--</div>--%>
        <%--</div>--%>
    </div>
</div>
</body>
</html>

