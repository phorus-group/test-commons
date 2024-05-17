package group.phorus.test.commons.bdd.steps

import group.phorus.test.commons.bdd.BaseRequestScenarioScope
import group.phorus.test.commons.bdd.BaseResponseScenarioScope
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap


class BasicStepsDefinition(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val requestScenarioScope: BaseRequestScenarioScope,
    @Autowired private val responseScenarioScope: BaseResponseScenarioScope,
) {
    @When("^the (.*) \"(.*)\" endpoint is called$")
    fun `the endpoint is called`(method: String, endpoint: String) {
        webTestClient.method(HttpMethod.valueOf(method))
            .uri { it.path(endpoint).build() }
            .bodyValue(requestScenarioScope.request!!)
            .exchange()
            .let { responseScenarioScope.responseSpec = it }
    }

    @When("^the (.*) \"(.*)\" endpoint is called with request params:$")
    fun `the endpoint is called with the request params`(method: String, endpoint: String, requestParams: DataTable) {
        val params = requestParams.asMap()
            .map { it.key to listOf(it.value) }.toMap()
            .let { LinkedMultiValueMap(it) }

        webTestClient.method(HttpMethod.valueOf(method))
            .uri { it.path(endpoint).queryParams(params).build() }
            .bodyValue(requestScenarioScope.request!!)
            .exchange()
            .let { responseScenarioScope.responseSpec = it }
    }


    @Then("the service returns HTTP {int}")
    fun `the service returns HTTP`(httpCode: Int) {
        responseScenarioScope.responseSpec!!
            .expectStatus().isEqualTo(httpCode)
    }
}