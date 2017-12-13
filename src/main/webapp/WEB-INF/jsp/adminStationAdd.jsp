<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="neighborStation" value="neighborStation"/>
<script>
    function addSelectAddStation() {
        $(".search-station select:last").after('<select class="form-control" name="${neighborStation}">\n' +
            '            <option>None</option>\n' +
            '            <c:forEach items="${requestScope.stations}" var="sCities">\n' +
            '                <option>${sCities.name}</option>\n' +
            '            </c:forEach>\n' +
            '        </select>');
    }
</script>
<form action="/admin/addStation" method="post" class="search-station">
    <div class="error">
        <h4><c:out value="${requestScope.error}"/></h4>
    </div>
    <h4>Add station</h4>
    <div><input class="form-control" type="text" placeholder="Station" name="station"></div>
        <select class="form-control" name="neighborStation">
            <option>None</option>
            <c:forEach items="${requestScope.stations}" var="sCities">
                <option>${sCities.name}</option>
            </c:forEach>
        </select>
    <input class="form-control button-option" type="button" value="Add" onclick="addSelectAddStation()">
    <input class="form-control button-main" type="submit" name="save" value="Save">
    <div class="indent"></div>
</form>
