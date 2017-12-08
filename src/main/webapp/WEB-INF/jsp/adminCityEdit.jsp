<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="neighborCityEdit" value="neighborCityEdit"/>
<script>
    function addSelectEditCity() {
        $(".edit-city select:last").after('<select class="form-control search-city-name" name="${neighborCityEdit}">\n' +
            '            <option>None</option>\n' +
            '            <c:forEach items="${requestScope.cities}" var="sCities">\n' +
            '                <option>${sCities.name}</option>\n' +
            '            </c:forEach>\n' +
            '        </select>');
    }
</script>
<form action="/admin/saveEditCity" method="post" class="edit-city">
    <div class="error">
        <h4><c:out value="${requestScope.errorEdit}"/></h4>
    </div>
    <h4>Edit City</h4>
    <input class="form-control search-city-name" type="text" name="city" value="${requestScope.cityName}">

    <c:forEach items="${requestScope.neighbors}" var="neighbors" varStatus="loop">
        <select class="form-control search-city-name" name="${neighborCityEdit}">
            <option>${neighbors.getNeighborCity().getName()}</option>
            <option>None</option>
            <c:forEach items="${requestScope.cities}" var="sCities">
                <option>${sCities.name}</option>
            </c:forEach>
        </select>
    </c:forEach>
    <c:if test="${fn:length(requestScope.neighbors)<1}">
        <select class="form-control search-city-name" name="${neighborCityEdit}">
            <option>None</option>
            <c:forEach items="${requestScope.cities}" var="sCities">
                <option>${sCities.name}</option>
            </c:forEach>
        </select>
    </c:if>
    <input class="form-control button-option" type="button" name="save" value="Add" onclick="addSelectEditCity()">
    <input class="form-control button-main" type="submit" name="save" value="Save">
    <input hidden type="text" name="cityOriginal" value="${requestScope.cityName}">
</form>
<div class="indent"></div>


