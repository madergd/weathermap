$(document).ready(function() {

    $("input:radio[name='city']").change(function(event) {
        getWeather();
    });
    $("input:radio[name='temperature']").change(function(event) {
        getWeather();
    });
});

function getWeather(){
    var cityname = $("input:radio[name='city']:checked").val();
    var temp = $("input:radio[name='temperature']:checked").val();
    if(cityname && temp) {
        $.get('./ActionServlet', {temperature: temp, city: cityname}, function (responseText) {
            $('#selectedcity').text(responseText.name);
            $('#date').text(responseText.date);
            $('#sunrise').text("Sunrise: " + responseText.sunrise + " a.m.");
            $('#sunset').text("Sunset: " + responseText.sunset + " p.m.");
            $('#temp').text("Temperature: " + responseText.temp + " ");
            $('#temp').append("&deg;" + responseText.symbol);
            $('#description').text(responseText.description);
        });
    }
}
