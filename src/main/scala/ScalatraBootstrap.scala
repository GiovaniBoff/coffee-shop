import com.thiago_dev.coffee.CoffeeServlet
import com.thiago_dev.shop.ShopServlet
import com.thiago_dev.stock.StockServlet
import com.thiago_dev.store.StoreServlet
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new CoffeeServlet, "/api/coffees/*")
    context.mount(new StockServlet, "/api/stock/*")
    context.mount(new StoreServlet, "/api/store/*")
    context.mount(new ShopServlet, "/*")
  }

  override def destroy(context: ServletContext): Unit = {
    super.destroy(context)
    System.exit(0)
  }
}
