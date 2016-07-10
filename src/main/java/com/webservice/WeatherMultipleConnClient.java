package com.webservice;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class WeatherMultipleConnClient {

    private static final Log log = LogFactory.getLog(WeatherMultipleConnClient.class);

    public static final String WEB_SERVICE = "http://api.openweathermap.org/data/2.5/";
    public static final String APPID = "a6b2b040c53da6fd474c4613da07beaa";
    public static final String CURRENT_WEATHER = "weather?";
    private static final String PARAM_NAME = "q";
    private static final String PARAM_MODE = "mode";
    private static final String PARAM_UNITS = "units";
    private static final String PARAM_APPID = "appId";
    private static final String VALUE_UNITS = "metric";
    private static final String DEFAULT_CITY = "LONDON";
    private static final String DEFEAUL_TEMP = "C";

    private static final int DEFAULT_CONN_TIMEOUT = 5000; //5 sec
    private static final int DEFAULT_READ_TIMEOUT = 20000; //20 sec
    private static final int DEFAULT_CONN_POOL_SIZE = 10;


    private static AtomicReference<MultiThreadedHttpConnectionManager> connectionManager = new AtomicReference<>();
    private AtomicReference<HttpClient> httpClient = new AtomicReference<>();

    static {
        final HttpConnectionManagerParams connectionParams = new HttpConnectionManagerParams();
        connectionParams.setConnectionTimeout(DEFAULT_CONN_TIMEOUT);
        connectionParams.setDefaultMaxConnectionsPerHost(DEFAULT_CONN_POOL_SIZE);
        connectionParams.setMaxTotalConnections(DEFAULT_CONN_POOL_SIZE);

        connectionManager.set(new MultiThreadedHttpConnectionManager());
        connectionManager.get().setParams(connectionParams);
    }

    protected AtomicReference<HttpClient> getHttpClient() throws MalformedURLException {
        httpClient.set(new HttpClient(connectionManager.get()));
        HttpParams clientParams = httpClient.get().getParams();
        clientParams.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, DEFAULT_CONN_TIMEOUT);
        clientParams.setIntParameter(HttpConnectionParams.SO_TIMEOUT, DEFAULT_READ_TIMEOUT);
        clientParams.setParameter(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, (long) DEFAULT_CONN_TIMEOUT);
        clientParams.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        return httpClient;
    }

    public String getWeather(String city, String temperature) {

        String responseBody = null;
        GetMethod httpGet = null;

        try {
            List<NameValuePair> params = new LinkedList<>();
            params.add(new BasicNameValuePair(PARAM_NAME, getValidParam(city,DEFAULT_CITY)));
            params.add(new BasicNameValuePair(PARAM_APPID, APPID));
            if (StringUtils.isEmpty(temperature) || temperature.equalsIgnoreCase(DEFEAUL_TEMP)) {
                params.add(new BasicNameValuePair(PARAM_UNITS, VALUE_UNITS));
            }

            httpGet = new GetMethod(WEB_SERVICE + CURRENT_WEATHER);
            httpGet.setQueryString(URLEncodedUtils.format(params, StandardCharsets.UTF_8));
            httpGet.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

            /*
            //responseBody = processResponse(httpGet.getResponseBodyAsStream());
            It is more efficient to use a stream rather than getting the entire entity as a String because the latter
            means that the entire contents of the response need to be read before they can be returned to your code, and
            control cannot be returned to your code until the entire response has been sent by the server.
            If you processed the response as a stream, then what you are actually doing is processing it N bytes at a time.
            This means that you can begin processing the first response segment while the remote server is still sending
            back the next segment of data. Therefore this makes more sense as an access method if your use-case allows you
            to process the data as it is received.

            If however you need the entire response as a String for whatever reason, then all of the efficiencies of the
            stream method have no bearing to you whatsoever - because even if you read the response in pieces, you still
            need to wait for the entire response - and have it all contained in a single String - before you can process it.

            The efficiency of using a stream is only available to you if you have a use-case where you can begin processing
            the response before you have the entire response body.
            */
            int httpResponseCode = getHttpClient().get().executeMethod(httpGet);
            if (httpResponseCode == HttpStatus.SC_OK) {
                responseBody = httpGet.getResponseBodyAsString();
                log.info(responseBody);
            }
        } catch (IOException e) {
            log.error(e.getStackTrace());
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return responseBody;
    }

    private String getValidParam(String paramName, String defaultName){
        return StringUtils.isNotBlank(paramName) ? paramName : defaultName;
    }
}
