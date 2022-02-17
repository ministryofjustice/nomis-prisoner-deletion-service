package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.integration

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.PurgeQueueRequest
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.JwtAuthHelper
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.MessageHelper
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.integration.testcontainer.LocalStackContainer
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.integration.testcontainer.LocalStackContainer.setLocalStackProperties
import uk.gov.justice.hmpps.sqs.HmppsQueue
import uk.gov.justice.hmpps.sqs.HmppsQueueService

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthHelper

  @Autowired
  protected lateinit var hmppsQueueService: HmppsQueueService

  @Autowired
  lateinit var messageHelper: MessageHelper

  internal val requestQueue by lazy { hmppsQueueService.findByQueueId("datacompliancerequest") as HmppsQueue }
  internal val responseQueue by lazy { hmppsQueueService.findByQueueId("datacomplianceresponse") as HmppsQueue }

  internal val requestAwsSqsClient by lazy { requestQueue.sqsClient }
  internal val requestQueueUrl by lazy { requestQueue.queueUrl }

  internal val requestAwsSqsDlqClient by lazy { requestQueue.sqsDlqClient as AmazonSQS }
  internal val requestDlqUrl by lazy { requestQueue.dlqUrl as String }

  internal val responseAwsSqsClient by lazy { responseQueue.sqsClient }
  internal val responseQueueUrl by lazy { responseQueue.queueUrl }

  internal val responseAwsSqsDlqClient by lazy { responseQueue.sqsDlqClient as AmazonSQS }
  internal val responseDlqUrl by lazy { responseQueue.dlqUrl as String }

  @LocalServerPort
  protected var port: Int = 0

  @BeforeEach
  fun purgeQueues() {
    responseAwsSqsClient.purgeQueue(PurgeQueueRequest(responseQueueUrl))
    requestAwsSqsClient.purgeQueue(PurgeQueueRequest(requestQueueUrl))
    responseAwsSqsDlqClient.purgeQueue(PurgeQueueRequest(responseDlqUrl))
    requestAwsSqsDlqClient.purgeQueue(PurgeQueueRequest(requestDlqUrl))
    messageHelper.clearMessages()
  }

  internal fun setAuthorisation(
    user: String = "AUTH_ADM",
    roles: List<String> = listOf()
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisation(user, roles)

  fun getNumberOfMessagesCurrentlyOnRequestQueue(): Int? {
    val queueAttributes = requestAwsSqsClient.getQueueAttributes(requestQueueUrl, listOf("ApproximateNumberOfMessages"))
    return queueAttributes.attributes["ApproximateNumberOfMessages"]?.toInt()
  }

  companion object {
    private val localStackContainer = LocalStackContainer.instance

    @JvmStatic
    @DynamicPropertySource
    fun testcontainers(registry: DynamicPropertyRegistry) {
      localStackContainer?.also { setLocalStackProperties(it, registry) }
    }
  }
}
