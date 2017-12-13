<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="../../static/css/station2.css" rel="stylesheet"/>
<c:set var="station" value="station"/>
<c:set var="departure" value="departure"/>
<c:set var="stopping" value="stopping"/>
<script>
    function addTrainWay() {
        $(".add-train:last").after('<div class="row">\n' +
            '    <div class="col-md-8">\n' +
            '        <select class="form-control" name="${station}">\n' +
            '            <option>None</option>\n' +
            '            <c:forEach items="${requestScope.stations}" var="sCities">\n' +
            '                <option>${sCities.name}</option>\n' +
            '            </c:forEach>\n' +
            '        </select>\n' +
            '    </div>\n' +
            '    <div class="col-md-4">\n' +
            '        <input class="form-control" type="number" placeholder="Cost" name="cost" value="0">\n' +
            '    </div>\n' +
            '</div>\n' +
            '<c:forEach var="j" begin="0" end="1" varStatus="mainLoop">\n' +
            '    <div class="row add-train">\n' +
            '        <div class="col-md-4">\n' +
            '            <c:choose><c:when test="${mainLoop.index==0}">\n' +
            '                    <h5>Departure</h5>\n' +
            '                    <c:set var="status" value="${departure}"/>\n' +
            '                </c:when><c:otherwise>\n' +
            '                    <h5>Stopping Time</h5>\n' +
            '                    <c:set var="status" value="${stopping}"/>\n' +
            '                </c:otherwise></c:choose>\n' +
            '        </div>\n' +
            '        <c:forEach var="k" begin="0" end="1" varStatus="loopStatus">\n' +
            '            <c:choose><c:when test="${loopStatus.index==0}">\n' +
            '                    <c:set var="time" value="Hour"/>\n' +
            '                    <c:set var="endLoop" value="24"/>\n' +
            '                </c:when><c:otherwise>\n' +
            '                    <c:set var="time" value="Minute"/>\n' +
            '                    <c:set var="endLoop" value="60"/>\n' +
            '                </c:otherwise></c:choose>\n' +
            '            <div class="col-md-4">\n' +
            '                <select class="form-control" name="${status}${time}">\n' +
            '                    <c:forEach var="l" begin="0" end="${endLoop}" varStatus="loop">\n' +
            '                        <option>${loop.index}</option>\n' +
            '                    </c:forEach>\n' +
            '                </select>\n' +
            '            </div>\n' +
            '        </c:forEach>\n' +
            '    </div>\n' +
            '</c:forEach>');
    }
</script>
<form action="/admin/addTrain" method="post" >
    <div class="error">
        <h4><c:out value="${requestScope.error}"/></h4>
    </div>
    <h4>Add Train</h4>
    <input class="form-control button-main" type="submit" name="save" value="Save">
    <div><input class="form-control" type="text" placeholder="Train" name="train"></div>
    <div class="row">
        <div class="col-md-8">
            <select class="form-control" name="${station}">
                <option>None</option>
                <c:forEach items="${requestScope.stations}" var="sCities">
                    <option>${sCities.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-4">
            <input class="form-control" type="number" placeholder="Cost" name="cost" value="0">
        </div>
    </div>
    <c:forEach var="j" begin="0" end="1" varStatus="mainLoop">
        <div class="row add-train">
            <div class="col-md-4">
                <c:choose>
                    <c:when test="${mainLoop.index==0}">
                        <h5>Departure</h5>
                        <c:set var="status" value="${departure}"/>
                    </c:when>
                    <c:otherwise>
                        <h5>Stopping Time</h5>
                        <c:set var="status" value="${stopping}"/>
                    </c:otherwise>
                </c:choose>
            </div>
            <c:forEach var="k" begin="0" end="1" varStatus="loopStatus">
                <c:choose>
                    <c:when test="${loopStatus.index==0}">
                        <c:set var="time" value="Hour"/>
                        <c:set var="endLoop" value="24"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="time" value="Minute"/>
                        <c:set var="endLoop" value="60"/>
                    </c:otherwise>
                </c:choose>
                <div class="col-md-4">
                    <select class="form-control" name="${status}${time}">
                        <c:forEach var="l" begin="0" end="${endLoop}" varStatus="loop">
                            <option>${loop.index}</option>
                        </c:forEach>
                    </select>
                </div>
            </c:forEach>
        </div>
    </c:forEach>
    <input class="form-control button-option" type="button" value="Add" onclick="addTrainWay()">
    <input class="form-control button-main" type="submit" name="save" value="Save">
</form>

