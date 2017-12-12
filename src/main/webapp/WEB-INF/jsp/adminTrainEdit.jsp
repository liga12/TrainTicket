<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="trainWayEdit" value="trainWayEdit"/>
<c:set var="city" value="city"/>
<c:set var="departure" value="departure"/>
<c:set var="stopping" value="stopping"/>
<script>
    function addSelectEditCity() {
        $(".edit-train:last").after('<div class="row">\n' +
            '    <div class="col-md-8">\n' +
            '        <select class="form-control" name="${city}">\n' +
            '            <option>None</option>\n' +
            '            <c:forEach items="${requestScope.cities}" var="sCities">\n' +
            '                <option>${sCities.name}</option>\n' +
            '            </c:forEach>\n' +
            '        </select>\n' +
            '    </div>\n' +
            '    <div class="col-md-4">\n' +
            '        <input class="form-control" type="text" placeholder="Cost" name="cost" value="0">\n' +
            '    </div>\n' +
            '</div>\n' +
            '<c:forEach var="j" begin="0" end="1" varStatus="mainLoop">\n' +
            '    <div class="row add-train edit-train">\n' +
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
<form action="/admin/saveEditTrainWay" method="post">
    <div class="error">
        <h4><c:out value="${requestScope.errorEdit}"/></h4>
    </div>
    <h4>Edit Train</h4>
    <input class="form-control search-city-name" type="text" name="train" value="${requestScope.trainName}">

    <c:forEach items="${requestScope.trainWays}" var="trainWays" varStatus="loop">
        <div class="row">
            <div class="col-md-8">
                <select class="form-control" name="${city}">
                    <option>${trainWays.getCity().getName()}</option>
                    <option>None</option>
                    <c:forEach items="${requestScope.cities}" var="sCities">
                        <option>${sCities.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <input class="form-control" type="text" placeholder="Cost" name="cost" value="${trainWays.getCost()}">
            </div>
        </div>
        <c:forEach var="j" begin="0" end="1" varStatus="mainLoop">
            <div class="row edit-train">
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
                            <c:if test="${status=='departure'}">
                                <c:if test="${time=='Hour'}">
                                    <option>${trainWays.getTrainDeparture().getHour()}</option>
                                </c:if>
                                <c:if test="${time=='Minute'}">
                                    <option>${trainWays.getTrainDeparture().getMinute()}</option>
                                </c:if>
                            </c:if>
                            <c:if test="${status=='stopping'}">
                                <c:if test="${time=='Hour'}">
                                    <option>${trainWays.getTrainStoppingTime().getHour()}</option>
                                </c:if>
                                <c:if test="${time=='Minute'}">
                                    <option>${trainWays.getTrainStoppingTime().getMinute()}</option>
                                </c:if>
                            </c:if>
                            <c:forEach var="l" begin="0" end="${endLoop}" varStatus="loop">
                                <option>${loop.index}</option>
                            </c:forEach>
                        </select>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
    </c:forEach>
    <c:if test="${fn:length(requestScope.trainWays)<1}">
        <div class="row">
            <div class="col-md-8">
                <select class="form-control" name="${city}">
                    <option>None</option>
                    <c:forEach items="${requestScope.cities}" var="sCities">
                        <option>${sCities.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <input class="form-control" type="text" placeholder="Cost" name="cost" value="0">
            </div>
        </div>
        <c:forEach var="j" begin="0" end="1" varStatus="mainLoop">
            <div class="row edit-train">
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
    </c:if>
    <input class="form-control button-option" type="button" name="save" value="Add" onclick="addSelectEditCity()">
    <input class="form-control button-main" type="submit" name="save" value="Save">
    <input hidden type="text" name="trainOriginal" value="${requestScope.trainName}">
</form>
<div class="indent"></div>


