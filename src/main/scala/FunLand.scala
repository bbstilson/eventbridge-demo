import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.Message

import scala.language.implicitConversions
import scala.collection.JavaConverters._

class Funland(
  cls: String,
  sqs: AmazonSQS,
  sqsQueueUrl: String,
  eventBridge: EventBridge,
  eventSource: String
) {

  private val messages = mkMessageIterator()

  def run(): Unit = {
    val putEvent = eventBridge.putEvent(eventSource, "Fun Land Experiment")

    log(s"Listening to: ${sqsQueueUrl}")

    messages
      .foreach { m =>
        val msg = parseMessage(m.getBody)
        log(s"Handling message: ${msg}")

        val next = msg + 1

        log(s"Next count: ${next}")

        putEvent(mkDetail(next)) match {
          case Right(id) => {
            log(s"Put event on event bus with id: ${id}")
            sqs.deleteMessage(sqsQueueUrl, m.getReceiptHandle)
          }
          case Left((errorCode, errorMessage)) => log(s"ERROR $errorCode - $errorMessage")
        }
    }
  }

  // Use argonaut or spray IRL.
  private val numRegex = """(\d+)""".r
  private def parseMessage(msg: String): Int = numRegex
    .findFirstMatchIn(msg)
    .map(_.toString.toInt)
    .getOrElse(-1)

  // Detail messages must be valid JSON. Again, use argonaut or spray IRL.
  private def mkDetail(nextCount: Int): String = s"""{ "count": $nextCount }"""

  // Pretty logging.
  private def log(msg: String): Unit = println(cls + Console.RESET + s" ${msg}")

  private def mkMessageIterator() = Iterator.continually {
    sqs.receiveMessage(sqsQueueUrl).getMessages.asScala
  }.flatten
}
