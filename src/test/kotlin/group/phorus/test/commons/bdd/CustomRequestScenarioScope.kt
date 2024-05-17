package group.phorus.test.commons.bdd

import io.cucumber.spring.ScenarioScope
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@ScenarioScope
@Primary
class CustomRequestScenarioScope(
    var obj: Any? = null,
) : BaseRequestScenarioScope()