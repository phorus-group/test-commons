package group.phorus.test.commons.bdd.steps

import group.phorus.test.commons.bdd.BaseRequestScenarioScope
import group.phorus.test.commons.bdd.BaseResponseScenarioScope
import group.phorus.test.commons.bdd.BaseScenarioScope
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap


class BasicStepsDefinition(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val baseScenarioScope: BaseScenarioScope,
    @Autowired private val requestScenarioScope: BaseRequestScenarioScope,
    @Autowired private val responseScenarioScope: BaseResponseScenarioScope,
) {
    @When("^the (.*) \"(.*)\" endpoint is called$")
    fun `the endpoint is called`(method: String, endpoint: String) {
        val processedEndpoint = if (endpoint.contains("\\{.*}".toRegex())) {
            val matches = "\\{(.*?)}".toRegex().findAll(endpoint).map { match ->
                match.destructured.toList().mapNotNull { key ->
                    baseScenarioScope.objects[key]?.let { key to it }
                }
            }.flatten()

            matches.fold(endpoint) { acc, pair ->
                acc.replace("{${pair.first}}", pair.second as String)
            }
        } else endpoint

        webTestClient.method(HttpMethod.valueOf(method))
            .uri { it.path(processedEndpoint).build() }
            .let {
                if (requestScenarioScope.request != null) {
                    it.bodyValue(requestScenarioScope.request!!)
                } else it
            }
            .exchange()
            .let { responseScenarioScope.responseSpec = it }
    }

    @When("^the (.*) \"(.*)\" endpoint is called with request params:$")
    fun `the endpoint is called with the request params`(method: String, endpoint: String, requestParams: DataTable) {
        val processedEndpoint = if (endpoint.contains("\\{.*}".toRegex())) {
            val matches = "\\{(.*?)}".toRegex().findAll(endpoint).map { match ->
                match.destructured.toList().mapNotNull { key ->
                    baseScenarioScope.objects[key]?.let { key to it }
                }
            }.flatten()

            matches.fold(endpoint) { acc, pair ->
                acc.replace("{${pair.first}}", pair.second as String)
            }
        } else endpoint

        val params = requestParams.asMap()
            .map {
                val value = if (it.value.contains("\\{.*}".toRegex())) {
                    val matches = "\\{(.*?)}".toRegex().findAll(it.value).map { match ->
                        match.destructured.toList().mapNotNull { key ->
                            baseScenarioScope.objects[key]?.let { key to it }
                        }
                    }.flatten()

                    matches.fold(it.value) { acc, pair ->
                        acc.replace("{${pair.first}}", pair.second as String)
                    }
                } else it.value

                it.key to listOf(value)
            }.toMap()
            .let { LinkedMultiValueMap(it) }

        webTestClient.method(HttpMethod.valueOf(method))
            .uri { it.path(processedEndpoint).queryParams(params).build() }
            .let {
                if (requestScenarioScope.request != null) {
                    it.bodyValue(requestScenarioScope.request!!)
                } else it
            }
            .exchange()
            .let { responseScenarioScope.responseSpec = it }
    }


    @Then("the service returns HTTP {int}")
    fun `the service returns HTTP`(httpCode: Int) {
        responseScenarioScope.responseSpec!!
            .expectStatus().isEqualTo(httpCode)
    }
}