<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<html lang="en">
<head>
    <title>AdminHome</title>
</head>
<jsp:include page="headerAdmin.jsp"/>
<link href="/static/css/station2.css" rel="stylesheet"/>
<script src="/static/js/jquery-3.1.1.min.js"></script>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-4">
            <div class="col-md-12">
                    <jsp:include page="adminStationAdd.jsp"/>
            </div>
        </div>
        <div class="col-md-4">
            <div class="col-md-12">
                <jsp:include page="adminStationSeach.jsp"/>
            </div>
        </div>
        <div class="col-md-4">
            <div class="col-md-12">
                <c:if test="${requestScope.stationName!=null}">
                    <jsp:include page="adminStationEdit.jsp"/>
                </c:if>
                <c:if test="${requestScope.errorEdit!=null}">
                    <div class="error">
                        <h4><c:out value="${requestScope.errorEdit}"/></h4>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>
