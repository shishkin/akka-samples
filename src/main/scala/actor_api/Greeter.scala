package actor_api

import akka.actor._
import akka.pattern._
import scala.concurrent.Future
import akka.util.Timeout

case class Greet(name: String)
case class Greeting(greeting: String)

/**
 * Strongly typed public API for the greeting functionality, implemented using
 * actors.
 */
trait Greeter {
  
  def greet(greet: Greet): Future[Greeting]
}

object Greeter {

  /**
   * Actor factory with an anonymous actor, implementing functionality.
   */
  val props = Props(new Actor {
    def receive = {
      case Greet(name) =>
        sender ! Greeting(s"Hello $name!")
    }
  })

  /**
   * Create a Greeter object hiding the actor reference.
   */
  def apply()(implicit factory: ActorRefFactory, timeout: Timeout) =
    new Greeter {
      lazy val ref = factory.actorOf(props)

      def greet(greet: Greet) =
        (ref ? greet).mapTo[Greeting]
    }
}
