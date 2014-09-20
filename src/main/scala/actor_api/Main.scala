package actor_api

import akka.actor._
import akka.pattern._
import akka.util.Timeout
import scala.concurrent.duration._

object Main {

  def main(args: Array[String]): Unit = {

    implicit val timeout = Timeout(1.second)
    implicit val system = ActorSystem("sample")
    implicit val dispatcher = system.dispatcher

    /**
     * Greeter is not an ActorRef, but rather a strongly typed object.
     */
    val greeter = Greeter()

    /**
     * Strongly typed future holding an eventual Greeting.
     */
    val future = greeter.greet(Greet("Bob"))
    
    future foreach {
      case Greeting(text) =>
        println(text)
        system.shutdown()
        
      /**
       * The following would not compile:
       * 
       * case foo: String => println(foo)
       */
    }

    system.awaitTermination()
  }
}