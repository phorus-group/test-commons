Feature: Basic steps work as intended

  Scenario: Caller with a request without params
    Given the caller has a request object
    When the POST "/test" endpoint is called
    Then the service returns HTTP 200
    And the service returns "test.null"

  Scenario: Caller with a path variable
    Given a theoretical User entity is created
    When the GET "/test/{userID}" endpoint is called
    Then the service returns HTTP 200
    And the service returns "testUserID"

  Scenario: Caller with a request with params
    Given the caller has a request object
    When the POST "/test" endpoint is called:
      | type  | key       | value     |
      | param | test      | testParam |
    Then the service returns HTTP 200
    And the service returns "test.testParam"

  Scenario: Caller with a request with multiple of the same params
    Given the caller has a request object
    When the POST "/test" endpoint is called:
      | type  | key       | value      |
      | param | test      | testParam1 |
      | param | test      | testParam2 |
    Then the service returns HTTP 200
    And the service returns "test.testParam1.testParam2"

  Scenario: Caller with a dynamic request with params
    Given a theoretical User entity is created
    And the caller has a request object
    When the POST "/test" endpoint is called:
      | type  | key       | value     |
      | param | test      | {userID}  |
    Then the service returns HTTP 200
    And the service returns "test.testUserID"

  Scenario: Caller with a path variable and a dynamic request with params
    Given a theoretical User entity is created
    And the caller has a request object
    When the POST "/test/{userID}" endpoint is called:
      | type  | key       | value     |
      | param | test      | {userID}  |
    Then the service returns HTTP 200
    And the service returns "test.testUserID.testUserID.null"

  Scenario: Caller with a path variable and a dynamic request with params and headers
    Given a theoretical User entity is created
    And the caller has a request object
    When the POST "/test/{userID}" endpoint is called:
      | type   | key                | value     |
      | param  | test               | {userID}  |
      | header | Authorization      | testAuth  |
    Then the service returns HTTP 200
    And the service returns "test.testUserID.testUserID.testAuth"
