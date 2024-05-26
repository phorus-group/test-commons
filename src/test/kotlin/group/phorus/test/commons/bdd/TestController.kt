package group.phorus.test.commons.bdd

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TestController {
    @PostMapping(path = ["/test"])
    @ResponseStatus(HttpStatus.OK)
    suspend fun test(
        @RequestBody
        request: String,

        @RequestParam(required = false, defaultValue = "null")
        test: List<String>,
    ): String? = "${request}.${test!!.joinToString(".")}"

    @GetMapping(path = ["/test/{pathVariable}"])
    @ResponseStatus(HttpStatus.OK)
    suspend fun testPathVariable(
        @PathVariable
        pathVariable: String,
    ): String = pathVariable

    @PostMapping(path = ["/test/{pathVariable}"])
    @ResponseStatus(HttpStatus.OK)
    suspend fun testPathVariable(
        @PathVariable
        pathVariable: String,

        @RequestBody
        request: String,

        @RequestParam(required = false, defaultValue = "null")
        test: List<String>,

        @RequestHeader(HttpHeaders.AUTHORIZATION, required = false, defaultValue = "null")
        auth: String,
    ): String? = "${request}.${pathVariable}.${test.joinToString(".")}.${auth}"
}