package com.servlets;

import com.json.JSONGenerator;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ActionServlet")
public class ActionServlet extends HttpServlet {
    private static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";
    private static final String UTF_8_ENCODING = "UTF-8";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String city = request.getParameter("city");
        String temperature = request.getParameter("temperature");
        JSONGenerator generator = new JSONGenerator();
        JSONObject json = generator.generate(city, temperature);
        response.setContentType(APPLICATION_JSON_CONTENT_TYPE);
        response.setCharacterEncoding(UTF_8_ENCODING);
        response.getWriter().write(json.toString());
    }
}
