package group.phorus.test.commons.bdd

import io.cucumber.spring.ScenarioScope
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@RestController
class TestController {
    @PostMapping(path = ["/test"])
    @ResponseStatus(HttpStatus.OK)
    suspend fun test(
        @RequestBody
        request: String,

        @RequestParam(required = false)
        param: String?,
    ): String? = param

    @GetMapping(path = ["/test/{pathVariable}"])
    @ResponseStatus(HttpStatus.OK)
    suspend fun testPathVariable(
        @PathVariable
        pathVariable: String?,
    ): String? = pathVariable
}