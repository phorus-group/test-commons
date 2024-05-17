Feature: Basic steps work as intended

  Scenario: Caller with a request without params
    Given the caller has a request object
    When the POST "/test" endpoint is called
    Then the service returns HTTP 200

  Scenario: Caller with a path variable
    Given a theoretical User entity is created
    When the GET "/test/{userID}" endpoint is called
    Then the service returns HTTP 200
    And the service returns the request param with value "testUserID"

  Scenario: Caller with a request with params
    Given the caller has a request object
    When the POST "/test" endpoint is called with request params:
      | param | testParam |
    Then the service returns HTTP 200
    And the service returns the request param with value "testParam"
