package group.phorus.test.commons.bdd.steps

import group.phorus.test.commons.bdd.BaseResponseScenarioScope
import group.phorus.test.commons.bdd.CustomRequestScenarioScope
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.expectBody


class ExtraStepsDefinition(
    @Autowired private val requestScenarioScope: CustomRequestScenarioScope,
    @Autowired private val responseScenarioScope: BaseResponseScenarioScope,
) {
    @Given("the caller has a request object")
    fun `the endpoint is called`() {
        requestScenarioScope.request = "test"
    }


    @Then("the service returns the request param with value {string}")
    fun `the service returns the request param`(value: String) {
        val param = responseScenarioScope.responseSpec!!
            .expectBody<String>().returnResult().responseBody

        assertEquals(value, param)
    }
}