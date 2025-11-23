package com.example.assurity.stepDefinitions;

import com.example.assurity.hooks.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.restassured.response.Response;
import static org.junit.Assert.*;

public class CategoriesStepDefinitions {

    private static final Logger logger = LogManager.getLogger(CategoriesStepDefinitions.class);
    private Response response;

    @When("The user sends API request with category {string} and   catalogue {string}")
    public void WhenTheUserSendsAPIRequestWithCategoryAndCatalogue(String id, String catalogue) {
        logger.info("Building endpoint for ID={} and Catalogue={}", id, catalogue);
        String endpoint = Hooks.helper.buildEndpoint(id, catalogue);
        logger.info("Calling API endpoint: {}", endpoint);
        response = Hooks.helper.getRequest(endpoint);
        logger.info("API call completed. Status Code: {}", response.getStatusCode());
        logger.info("Full API Response: {}", response.asString());
    }

    @Then("status code should be {int}")
    public void ThenStatusCodeShouldBe(Integer expectedStatus) {
        int actualStatus = response.getStatusCode();
        logger.info("Validating status code. Expected={}, Actual={}", expectedStatus, actualStatus);
        //verifying the correct status code is received, when request is successful
        assertEquals((int) expectedStatus, actualStatus);
    }

    @And("the response should have category name as {string}")
    public void ThenTheResponseShouldHaveCategoryNameAs(String expectedName) {
        //get Category Details from Json Response
        String actualName = response.jsonPath().getString("Name");
        logger.info("Validating category name. Expected='{}', Actual='{}'", expectedName, actualName);
        assertEquals(expectedName, actualName);
    }

    @And("the response should have CanRelist value  as {string}")
    public void ThenTheResponseShouldHaveCanRelistValueAs(String expectedValue) {
        boolean actual = response.jsonPath().getBoolean("CanRelist");
        boolean expected = Boolean.parseBoolean(expectedValue);
        logger.info("Validating CanRelist value. Expected={}, Actual={}", expected, actual);
        assertEquals(expected, actual);
    }

    @And("the promotion {string} should contain text {string}")
    public void ThenThePromotionShouldContainText(String promotionName, String expectedDescription) {
        logger.info("Searching for promotion '{}' containing text '{}'", promotionName, expectedDescription);
        var promotions = response.jsonPath().getList("Promotions");

        //Find Description for the given promotion name from Category Details
        boolean found = promotions.stream().anyMatch(promo ->
                promo.toString().contains("Name=" + promotionName)
                        && promo.toString().contains(expectedDescription)
        );
        logger.info("Promotion validation result: {}", found ? "FOUND" : "NOT FOUND");
        assertTrue("Promotion " + promotionName + " does not contain expected description.", found);
    }
}
