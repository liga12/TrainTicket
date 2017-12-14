<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.js"></script>
    <link href="/static/css/station2.css" rel="stylesheet"/>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-1">
        </div>
        <div class="col-md-10">
            <form action="/home/searchTrain" method="get">
                <div class="col-md-12">
                    <h4>Search Of Train Ticket</h4>
                    <div style="height: 50px"></div>

                    <div class="row">
                        <div class="col-md-3">
                            <h5>Max value transplant</h5>
                        </div>
                        <div class="col-md-3">
                            <select data-live-search="true" data-live-search-style="startsWith"
                                    class="selectpicker form-control" name="valueTransplant">
                                <c:forEach var="i" begin="0" end="3" varStatus="loop">
                                    <option>${loop.index}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <h5>Max time waiting on station</h5>
                        </div>
                        <div class="col-md-3">
                            <select data-live-search="true" data-live-search-style="startsWith"
                                    class="selectpicker form-control" name="waiting">
                                <c:forEach var="i" begin="1" end="12" varStatus="loop">
                                    <option>${loop.index}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <h4>Source Station</h4>
                    <div class="row">
                        <div class="col-md-8">
                            <select data-live-search="true" data-live-search-style="startsWith"
                                    class="selectpicker form-control" name="sourceStation">
                                <c:forEach items="${requestScope.stations}" var="sCities">
                                    <option>${sCities.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <input class="form-control" type="datetime-local" name="sourceDateTime"
                                   value="${sourceDateTime}">
                        </div>
                    </div>
                    <h4>Destination Station</h4>
                    <div class="row">
                        <div class="col-md-8">
                            <select data-live-search="true" data-live-search-style="startsWith"
                                    class="selectpicker form-control" name="destinationStation">
                                <c:forEach items="${requestScope.stations}" var="sCities">
                                    <option>${sCities.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <input class="form-control" type="datetime-local" name="destinationDateTime"
                                   value="${destinationDateTime}"/>
                        </div>
                    </div>
                    <input class="form-control button-main btn btn-primary" type="submit" name="save" value="Search">
                    <div class="error">
                        <h4><c:out value="${requestScope.error}"/></h4>
                    </div>

                    <c:forEach items="${requestScope.way.getWays()}" var="ways">
                        <div style="background: white">
                            <div class="row">
                                <div class="col-md-6"><h5>Cost ${ways.getCost()}</h5></div>
                                <div class="col-md-6"><h5>Transplantants  ${ways.getTransplant()}</h5></div>
                            </div>

                            <div class="row f" style="background: white">
                                <div class="col-md-3">
                                    <h5>Train</h5>
                                </div>
                                <div class="col-md-3">
                                    <h5>Station</h5>
                                </div>
                                <div class="col-md-3">
                                    <h5>Departure</h5>
                                </div>
                                <div class="col-md-3">
                                    <h5>StoppingTime</h5>
                                </div>
                            </div>
                            <c:forEach items="${ways.getWays()}" var="currentWay">

                                <div class="row" style="background: white">
                                    <div class="col-md-3">
                                        <h5>${currentWay.getTrain().getName()}</h5>
                                    </div>
                                    <div class="col-md-3">
                                        <h5>${currentWay.getStation().getName()}</h5>
                                    </div>
                                    <div class="col-md-3">
                                        <h5>${currentWay.getDepartureTime()}</h5>
                                    </div>
                                    <div class="col-md-3">
                                        <h5>${currentWay.getStoppingTime()}</h5>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="indent"></div>
                    </c:forEach>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="col-md-1">
</div>
</div>
</div>


</body>
</html>
