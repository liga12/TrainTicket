<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>

    <link rel='stylesheet prefetch' href='http://netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css'>

    <link rel="stylesheet" href="/static/css/style.css">

</head>
<body>
<div class="wrapper">
    <form class="form-signin" action="/login" method="post">
        <h2 class="form-signin-heading">Please login</h2>
        <input type="text" class="form-control" name="login" placeholder="Login" required="" autofocus="" />
        <input type="password" class="form-control" name="password" placeholder="Password" required=""/>
        <c:out value='${error}'/>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sing In</button>
    </form>
</div>



</body>



</html>
