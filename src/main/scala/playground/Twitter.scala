import akka.{NotUsed, Done}
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.Future

final case class Author(handle: String)
final case class Hashtag(name: String)

final case class Tweet(account: Author, timestamp: Long, body: String) {
  def hashtags: Set[Hashtag] = {
    body
      .split(" ")
      .collect {
        case t if t.startsWith("#") =>
          Hashtag(t.replaceAll("[^#\\w]", ""))
      }
      .toSet
  }
}
val rugbyTag = Hashtag("#RWC2023")

object main extends App {

  val tweets: Source[Tweet, NotUsed] = Source(
    List(
      Tweet(
        Author("PlanetRugby"),
        System.currentTimeMillis,
        "The warm-ups are done. Bring on the Rugby World Cup! #RWC2023"
      ),
      Tweet(
        Author("RugbyPass"),
        System.currentTimeMillis,
        "Fiji make their Twickenham entrance just weeks out from potentially becoming everyone's favorite second team at the Rugby World Cup... #FijiRugby #ENGvFIJ #SummerNationsSeries #RWC2023"
      ),
      Tweet(
        Author("rugbyworldcup"),
        System.currentTimeMillis,
        "Ice in the veins. The kick at RWC 2015 against England that secured Dan Biggar's place in Welsh rugby folklore #RWC2023"
      ),
      Tweet(
        Author("AllBlacks"),
        System.currentTimeMillis,
        "The commentary on this try couldn't be more perfect #RWC2023"
      ),
      Tweet(
        Author("SVNSeries"),
        System.currentTimeMillis,
        "@CheslinKolbe was born to skip past defenders! #HSBCSVNS #RWC2023"
      )
    )
  )

  implicit val system: ActorSystem = ActorSystem("RWC")

  val done: Future[Done] = tweets
    .filterNot(_.hashtags.contains(rugbyTag))
    .map(_.hashtags)
    .reduce(_ ++ _)
    .mapConcat(identity)
    .map(_.name.toUpperCase)
    .runWith(Sink.foreach(println))

  implicit val ec: ExecutionContextExecutor = system.dispatcher
  Thread.sleep(2000)
  done.onComplete(_ => system.terminate())

}
