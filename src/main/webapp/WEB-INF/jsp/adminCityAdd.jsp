<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="neighborCity" value="neighborCity"/>
<script>
    function addSelectAddCity() {
        $(".search-city select:last").after('<select class="form-control" name="${neighborCity}">\n' +
            '            <option>None</option>\n' +
            '            <c:forEach items="${requestScope.cities}" var="sCities">\n' +
            '                <option>${sCities.name}</option>\n' +
            '            </c:forEach>\n' +
            '        </select>');
    }
</script>
<form action="/admin/addCity" method="post" class="search-city">
    <div class="error">
        <h4><c:out value="${requestScope.error}"/></h4>
    </div>
    <h4>Add City</h4>
    <div><input class="form-control" type="text" placeholder="City" name="city"></div>
        <select class="form-control" name="neighborCity">
            <option>None</option>
            <c:forEach items="${requestScope.cities}" var="sCities">
                <option>${sCities.name}</option>
            </c:forEach>
        </select>
    <input class="form-control button-option" type="button" value="Add" onclick="addSelectAddCity()">
    <input class="form-control button-main" type="submit" name="save" value="Save">
    <div class="indent"></div>
</form>
