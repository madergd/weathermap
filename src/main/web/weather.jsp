<%@page language="java" contentType="text/html" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="css/style.css">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/weather.js" defer="defer"></script>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Weatherapp</title>
</head>
<body>
<form id="form1">
    <div id="wrapper">
        <div id="header"> <h1>Current weather and forcast</h1></div>
        <div id="content">
            <div id="content-left">
                <b><label id="selectedcity"/></b>
            </div>
            <div id="content-main">
                <input type="radio" name="city" value="London"> London
                <input type="radio" name="city" value="Hong Kong"> Hong Kong  &nbsp; &nbsp; &nbsp; &nbsp;
            </div>
            <div id="content-right">
                <input type="radio" name="temperature" value="F"> Fahrenheit
                <input type="radio" name="temperature" value="C"> Celsius &nbsp; &nbsp; &nbsp; &nbsp;
            </div>
        </div>
        <div id="content-bottom">
            <label id="date"></label>
            <p id="description"></p>
            <div id="temp"></div>
            <p id="sunrise"/>  <p id="sunset"/>

        </div>
        <div id="bottom">
            <label id="error" />
        </div>
    </div>

</form>
</body>
</html>
