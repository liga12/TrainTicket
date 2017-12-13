<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<h4>Search Station</h4>
<form action="/admin/searchStation" method="post">
    <input type="text" class="form-control" placeholder="Station" name="station">
    <input type="submit" class="button-main form-control" name="search" value="Search">
    <input type="submit" class="button-main form-control" name="searchAll" value="SearchAll">
</form>
<c:set var="search" value="${requestScope.searchCities}"/>
<c:if test="${search!=null}">
    <c:if test="${fn:length(search)==0}">
        <h4 class="text-center error">
            <c:out value="Not Found"/>
        </h4>
    </c:if>
</c:if>
<div class="indent-small"></div>
<div class="indent-small"></div>
<c:forEach items="${requestScope.searchStations}" var="ss">
    <div >
        <input type="text" readonly class="form-control search-station-name" value="${ss.name}">
    </div>
    <div class="row">
        <a class="col-md-6" href="/admin/editStation?name=${ss.name}">
            <input class="button-main" type="button" value="Edit" style="width: 100%">
        </a>
        <a class="col-md-6" href="/admin/deleteStation?name=${ss.name}">
            <input class="button-main" type="button" value="Delete">
        </a>
    </div>
    <div class="indent-small"></div>
    <div class="indent-small"></div>
    <div class="indent-small"></div>
</c:forEach>
<div class="indent"></div>


