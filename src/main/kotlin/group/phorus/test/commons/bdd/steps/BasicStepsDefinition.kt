package group.phorus.test.commons.bdd.steps

import group.phorus.test.commons.bdd.BaseRequestScenarioScope
import group.phorus.test.commons.bdd.BaseResponseScenarioScope
import group.phorus.test.commons.bdd.BaseScenarioScope
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.lang.reflect.Type
import java.util.function.Consumer


class BasicStepsDefinition(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val baseScenarioScope: BaseScenarioScope,
    @Autowired private val requestScenarioScope: BaseRequestScenarioScope,
    @Autowired private val responseScenarioScope: BaseResponseScenarioScope,
) {
    @When("^the (.*) \"(.*)\" endpoint is called$")
    fun `the endpoint is called`(method: String, endpoint: String) {
        responseScenarioScope.responseSpec = callEndpoint(
            endpoint.process(baseScenarioScope.objects),
            HttpMethod.valueOf(method),
            requestScenarioScope.request
        )
    }

    @When("^the (.*) \"(.*)\" endpoint is called:$")
    fun `the endpoint is called with params`(method: String, endpoint: String, requestParams: DataTable) {
        val params = requestParams.entries().mapNotNull {
            val type = it["type"]!!
            val key = it["key"]!!
            val value = it["value"]!!

            if (type == "param") {
                key to value.process(baseScenarioScope.objects)
            } else null
        }.let {
            val collector = mutableMapOf<String, List<String>>()
            it.forEach { (key, value) ->
                collector[key] = collector[key]?.plus(value) ?: listOf(value)
            }
            collector
        }.toMap().let { LinkedMultiValueMap(it) }

        val headers = requestParams.entries().mapNotNull {
            val type = it["type"]!!
            val key = it["key"]!!
            val value = it["value"]!!

            if (type == "header") {
                key to value.process(baseScenarioScope.objects)
            } else null
        }.toMap()

        responseScenarioScope.responseSpec = callEndpoint(
            endpoint.process(baseScenarioScope.objects),
            HttpMethod.valueOf(method),
            requestScenarioScope.request,
            params,
            headers,
        )
    }

    @Then("the service returns HTTP {int}")
    fun `the service returns HTTP`(httpCode: Int) {
        responseScenarioScope.responseSpec!!
            .expectStatus().isEqualTo(httpCode)
    }


    private fun String.process(objects: MutableMap<String, Any>): String {
        return if (this.contains("\\{.*}".toRegex())) {
            val matches = "\\{(.*?)}".toRegex().findAll(this).map { match ->
                match.destructured.toList().mapNotNull { key ->
                    objects[key]?.let { key to it }
                }
            }.flatten()

            matches.fold(this) { acc, pair ->
                acc.replace("{${pair.first}}", pair.second as String)
            }
        } else this
    }

    private fun callEndpoint(
        endpoint: String,
        method: HttpMethod,
        body: Any? = null,
        queryParams: MultiValueMap<String, String>? = null,
        headers: Map<String, String>? = null
    ): WebTestClient.ResponseSpec {
        return webTestClient.method(method)
            .uri { it.path(endpoint).let { path ->
                if (queryParams != null) {
                    path.queryParams(queryParams)
                } else path
            }.build() }
            .let {
                if (body != null) {
                    it.bodyValue(body)
                } else it
            }
            .let {
                if (headers != null) {
                    it.headers { h ->
                        headers.forEach { (key, value) ->
                            h.set(key, value)
                        }
                    }
                } else it
            }
            .exchange()
    }
}