package group.phorus.test.commons.bdd

import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestApp::class])
@CucumberContextConfiguration
@AutoConfigureWebTestClient
class CucumberSpringConfiguration

@SpringBootApplication(scanBasePackages = ["group.phorus"])
class TestApp