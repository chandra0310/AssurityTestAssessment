Feature: Verify category API details

Scenario Outline: Verify the category name and other details from API response
    When The user sends API request with category "<id>" and   catalogue "<catalogue>" 
    Then status code should be 200
    And the response should have category name as "<name>"
    And the response should have CanRelist value  as "<canRelist>"
    And the promotion "<promotionName>" should contain text "<promotionDescription>"

Examples: 
| id   | catalogue | name           | canRelist | promotionName | promotionDescription      |
| 6327 | true      | Carbon credits | true      | Gallery       | Good position in category |
