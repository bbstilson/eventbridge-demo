import com.amazonaws.services.sqs.AmazonSQSClientBuilder

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, ExecutionContext, Future }

object Main {

  def main(args: Array[String]): Unit = {
    implicit val ec = ExecutionContext.global

    val sqs = AmazonSQSClientBuilder.defaultClient()
    val eventBridge = EventBridge("brandon-playground")

    val primary = {
      val eventSource = "funland-primary"
      val queueUrl = "https://sqs.us-west-2.amazonaws.com/FIX_ME/s2-BrandonsFunLand"
      new Funland(Console.MAGENTA + "primary", sqs, queueUrl, eventBridge, eventSource)
    }

    val secondary = {
      val eventSource = "funland-secondary"
      val queueUrl = "https://sqs.us-west-2.amazonaws.com/FIX_ME/s2-BrandonsFunLand-secondary"
      new Funland(Console.CYAN + "secondary", sqs, queueUrl, eventBridge, eventSource)
    }

    val futures = List(primary, secondary).map { land => Future(land.run()) }

    Await.result(Future.sequence(futures), Duration.Inf)
  }
}
