package com.json;

import com.webservice.WeatherMultipleConnClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class JSONGenerator {

    private static final Log log = LogFactory.getLog(WeatherMultipleConnClient.class);

    public JSONObject generate(String city, String temperature) {

        JSONObject result = new JSONObject();
        String responseBody = new WeatherMultipleConnClient().getWeather(city, temperature);
        if (responseBody != null) {
            try {
                if (StringUtils.isNotBlank(responseBody)) {
                    Calendar calendar = Calendar.getInstance();

                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONObject sys = jsonObject.getJSONObject("sys");
                    JSONObject main = jsonObject.getJSONObject("main");
                    JSONArray weatherMap = jsonObject.getJSONArray("weather");

                    if (weatherMap.length() > 0) {
                        result.put("description", weatherMap.getJSONObject(0).getString("description"));

                    }
                    result.put("temp", main.get("temp"));
                    result.put("date", new SimpleDateFormat("dd MMM yyyy").format(calendar.getTime()));
                    String cityName = jsonObject.get("name").toString();
                    result.put("name", cityName);
//                  Sunrise and sunset are coming back incorrect for Hong Kong from openweathermap,
//                  see http://openweathermap.org/city/1819729
//                  if ("Hong Kong".equalsIgnoreCase(cityName)) {
//                     calendar.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
//                  }
                    result.put("sunrise", getTime("sunrise", sys, calendar));
                    result.put("sunset", getTime("sunset", sys, calendar));
                    result.put("symbol", temperature);

                } else {
                    log.error(new JSONObject(responseBody));
                }
            } catch (JSONException e) {
                log.error(e.getMessage());
            }
        }
        return result;
    }

    private String getTime(String timeStr, JSONObject sys, Calendar calendar) throws JSONException {
        long time = Long.parseLong(sys.get(timeStr).toString());
        calendar.setTimeInMillis(time * 1000);
        return new SimpleDateFormat("hh:mm").format(calendar.getTime());
    }
}



