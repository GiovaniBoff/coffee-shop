
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

object JettyLauncher {
  def main(args: Array[String]) {
    val port = sys.env.getOrElse("PORT", 8080).asInstanceOf[Int]

    val server = new Server(port)
    val context = new WebAppContext()

    context setContextPath "/"
    context setResourceBase "src/main/webapp"
    context setEventListeners Array(new ScalatraListener)
    context.setInitParameter(ScalatraListener.LifeCycleKey, "com.thiago_dev.ScalatraBootstrap")
    server setHandler context

    server.start()
    server.join()
  }
}