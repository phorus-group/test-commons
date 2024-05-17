package group.phorus.test.commons.bdd

import io.cucumber.spring.ScenarioScope
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient


@Component
@ScenarioScope
class BaseRequestScenarioScope(
    var request: Any? = null,
)

@Component
@ScenarioScope
class BaseResponseScenarioScope(
    var responseSpec: WebTestClient.ResponseSpec? = null,
)
