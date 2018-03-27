package com.ironmangym.logout

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.domain.Users
import com.pangwarta.sjrmui.{ Card, CardContent, CardMedia, Grid, Typography }
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

import scala.scalajs.js.UndefOr._

object Logout {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users])

  private class Backend($: BackendScope[Props, Unit]) {

    def render(p: Props): VdomElement =
      <.div(
        Styles.containerDiv,
        Grid(container = true)()(
          Grid(item = true, xs = 12, lg = 8)()(
            Card()()(
              CardMedia(
                image     = "./target/scala-2.12/classes/ironmangym_inside.jpg",
                className = Styles.logoutCardMedia
              )("title" -> "Iron Man Gym")(),
              CardContent()()(
                Typography(variant = Typography.Variant.title)()("Get swole here, dude")
              )
            )
          ),
          Grid(item = true, xs = 12, lg = 4)()(
            Registration(p.router, p.proxy)
          )
        )
      )
  }

  private val component = ScalaComponent.builder[Props]("Logout")
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]): VdomElement =
    component(Logout.Props(router, proxy))
}
