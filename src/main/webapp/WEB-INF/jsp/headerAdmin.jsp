
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/home">TrainTicket</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="/admin/home">Station</a></li>
            <li><a href="/admin/train">Train</a></li>
            <li><a href="/home">Home</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li ><a href="${pageContext.request.contextPath}/logout"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>

        </ul>

    </div>
</nav>
</body>
</html>