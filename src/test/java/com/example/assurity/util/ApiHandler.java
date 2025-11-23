package com.example.assurity.util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApiHandler {
    private static final Logger logger = LogManager.getLogger(ApiHandler.class);
    private Properties properties = new Properties();
    private RequestSpecification request;


    public ApiHandler() {
        logger.info("Initializing RequestHelper");
        request = RestAssured.given().header("Content-Type", "application/json");
    }

    /** Load config automatically */
    public void loadConfig() {
        logger.info("Loading configuration from api.properties");

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("config/api.properties");
            if (is == null) {
                throw new RuntimeException("Cannot find config/api.properties");
            }

            properties.load(is);

            String env = properties.getProperty("env", "test");
            String baseURL = properties.getProperty("base.url." + env);

            if (baseURL == null || baseURL.isEmpty()) {
                throw new RuntimeException("Base URL for environment '" + env + "' is not defined");
            }

            RestAssured.baseURI = baseURL;

            //logger.info("Config loaded successfully. ENV={}, BASE={}", env, base);
            logger.info("Config loaded successfully. ENV={}, BASE={}",env, baseURL);

        } catch (Exception e) {
            logger.error("Failed to load configuration", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    /** Build API URL */
    public String buildEndpoint(String id, String catalogue) {
        logger.info("Entering method: buildEndpoint(id={}, catalogue={})", id, catalogue);

        String endpoint = properties.getProperty("categoryEndpoint");
        if (endpoint == null) {
            throw new RuntimeException("Category endpoint is not defined in properties");
        }

        // Build path
        String path = endpoint.replace("{id}", id).replace("{catalogue}", catalogue);

        // Get base URL from properties
        String env = properties.getProperty("env", "test");
        String baseURL = properties.getProperty("base.url." + env);
        if (baseURL == null || baseURL.isEmpty()) {
            throw new RuntimeException("Base URL for environment '" + env + "' is not defined");
        }

        // Return full URL
        String fullUrl = baseURL + path;
        logger.info("Full URL built: {}", fullUrl);
        return fullUrl;
    }

    public Response getRequest (String fullUrl) {
        logger.info("Entering method: get(path={})", fullUrl);
        try {
            Response response = request.when().get(fullUrl).andReturn();
            logger.info("Received response with status code {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            logger.error("GET request to {} failed", fullUrl, e);
            throw e;
        }
    }
}