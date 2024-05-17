package group.phorus.test.commons.bdd

import io.cucumber.spring.ScenarioScope
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient


@Component
@ScenarioScope
open class BaseRequestScenarioScope(
    var request: Any? = null,
)

@Component
@ScenarioScope
open class BaseResponseScenarioScope(
    var responseSpec: WebTestClient.ResponseSpec? = null,
)
