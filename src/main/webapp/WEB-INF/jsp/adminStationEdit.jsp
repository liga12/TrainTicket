<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="neighborStationEdit" value="neighborStationEdit"/>
<script>
    function addSelectEditStation() {
        $(".edit-station select:last").after('<select class="form-control search-station-name" name="${neighborStationEdit}">\n' +
            '            <option>None</option>\n' +
            '            <c:forEach items="${requestScope.stations}" var="sCities">\n' +
            '                <option>${sCities.name}</option>\n' +
            '            </c:forEach>\n' +
            '        </select>');
    }
</script>
<form action="/admin/saveEditStation" method="post" class="edit-station">
    <div class="error">
        <h4><c:out value="${requestScope.errorEdit}"/></h4>
    </div>
    <h4>Edit station</h4>
    <input class="form-control search-station-name" type="text" name="station" value="${requestScope.stationName}">

    <c:forEach items="${requestScope.neighbors}" var="neighbors" varStatus="loop">
        <select class="form-control search-station-name" name="${neighborStationEdit}">
            <option>${neighbors.getNeighborStation().getName()}</option>
            <option>None</option>
            <c:forEach items="${requestScope.stations}" var="sCities">
                <option>${sCities.name}</option>
            </c:forEach>
        </select>
    </c:forEach>
    <c:if test="${fn:length(requestScope.neighbors)<1}">
        <select class="form-control search-station-name" name="${neighborStationEdit}">
            <option>None</option>
            <c:forEach items="${requestScope.stations}" var="sCities">
                <option>${sCities.name}</option>
            </c:forEach>
        </select>
    </c:if>
    <input class="form-control button-option" type="button" name="save" value="Add" onclick="addSelectEditStation()">
    <input class="form-control button-main" type="submit" name="save" value="Save">
    <input hidden type="text" name="stationOriginal" value="${requestScope.stationName}">
</form>
<div class="indent"></div>


