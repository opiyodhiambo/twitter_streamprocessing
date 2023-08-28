import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl._

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
