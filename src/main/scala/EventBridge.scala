import com.amazonaws.services.eventbridge.AmazonEventBridgeClientBuilder
import com.amazonaws.services.eventbridge.model.{ PutEventsRequest, PutEventsRequestEntry }

import java.util.Date

import scala.collection.JavaConverters._
import scala.language.implicitConversions

case class EventBridge(eventBusName: String) {
  val client = AmazonEventBridgeClientBuilder.defaultClient()

  // Puts an event on the bus and returns the message id if successful.
  def putEvent(source: String, detailType: String)(detail: String): Either[(String, String), String] = {
    val req = {
      val reqE = new PutEventsRequestEntry()
        .withEventBusName(eventBusName)
        .withSource(source)
        .withTime(new Date())
        .withDetailType(detailType)
        .withDetail(detail)

      new PutEventsRequest().withEntries(reqE)
    }

    client
      .putEvents(req)
      .getEntries()
      .asScala
      .map { e =>
        e.getEventId() match {
          case null => Left((e.getErrorCode(), e.getErrorMessage()))
          case id: String => Right(id)
        }
      }
      .head
  }
}
