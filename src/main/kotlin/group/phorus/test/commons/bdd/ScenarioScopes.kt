package group.phorus.test.commons.bdd

import io.cucumber.spring.ScenarioScope
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper


/** General-purpose key/value store shared across steps within a scenario. */
@Component
@ScenarioScope
class BaseScenarioScope(
    var objects: MutableMap<String, Any> = mutableMapOf(),
)

/**
 * Returns the value stored under [key] cast to [T], or null if absent or the wrong type.
 * Consolidates the unchecked cast in one place so call sites stay clean.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> BaseScenarioScope.get(key: String): T? = objects[key] as? T


/** Holds the outbound request body for the next HTTP call. */
@Component
@ScenarioScope
class BaseRequestScenarioScope(
    var request: Any? = null,
)

/**
 * Holds the captured HTTP response for the current scenario.
 *
 * [statusCode] and [responseBody] are populated by [group.phorus.test.commons.bdd.steps.BasicStepsDefinition]
 * immediately after each exchange so the body can be deserialized multiple times via [bodyAs].
 */
@Component
@ScenarioScope
class BaseResponseScenarioScope(
    var statusCode: Int? = null,
    var responseHeaders: HttpHeaders? = null,
    var responseBody: ByteArray? = null,
)

/**
 * Deserializes the buffered response body to [T].
 *
 * Preserves full generic type parameters via [TypeReference], making it safe with
 * parameterized types like `RestResponsePage<MyDto>`.
 * Returns null if no body was captured.
 */
inline fun <reified T> BaseResponseScenarioScope.bodyAs(objectMapper: ObjectMapper): T? {
    val bytes = responseBody?.takeIf { it.isNotEmpty() } ?: return null
    return objectMapper.readValue(bytes, object : TypeReference<T>() {})
}

/**
 * Shorthand for [bodyAs] when the expected type is a paginated [RestResponsePage].
 * Returns null if no body was captured.
 */
inline fun <reified T : Any> BaseResponseScenarioScope.pageBodyAs(objectMapper: ObjectMapper): RestResponsePage<T>? =
    bodyAs<RestResponsePage<T>>(objectMapper)
