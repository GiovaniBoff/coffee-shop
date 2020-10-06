import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

object JettyLauncher {
  println("Running!")

  def main(args: Array[String]): Unit = {
    val port = sys.props.getOrElse("http.port", "8080").toInt
    println(port)

    val server = new Server(port)
    val context = new WebAppContext

    context.setContextPath("/")
    context.setResourceBase("src/main/webapp")
    context.setEventListeners(Array(new ScalatraListener))
    server.setHandler(context)

    server.start()
    server.join()
  }
}
