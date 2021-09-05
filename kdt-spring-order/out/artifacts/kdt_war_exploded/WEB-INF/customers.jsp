<!--jsp 태그 라이브러리 추가-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Home</title>
</head>
<body>
    <h1>KDT Spring App</h1>
    <p>The time on the server is ${serverTime}</>p>

<%--<% //자바 코드가능--%>
<%--    for (int i = 0; i <10 ; i++) {--%>
<%--%>--%>
<%--    <!--중간에 태그도 가능-->--%>
<%--    <%=i %> <br>--%>
<%--    <%--%>
<%--    }--%>
<%--%>--%>

<!--이런 식으로도 가능-->
<c:forEach var="i" begin="1" end="10" step="1">${i} <br> </c:forEach>







</body>
</html>
