package com.ironmangym.logout

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.domain.RootModel
import com.pangwarta.sjrmui.{ Card, CardContent, CardMedia, Grid, Typography }
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

import scala.scalajs.js.UndefOr._

object Logout {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel])

  private class Backend($: BackendScope[Props, Unit]) {

    def render(p: Props): VdomElement =
      <.div(
        Styles.containerDiv,
        Grid(container = true, spacing = 24)()(
          Grid(item = true, xs = 12, sm = 3, md = 8)()(
            Card()()(
              CardMedia(
                image     = "./target/scala-2.12/classes/ironmangym_inside.jpg",
                className = Styles.logoutCardMedia
              )("title" -> "Iron Man Gym")(),
              CardContent()()(
                Typography(variant   = Typography.Variant.title, className = marginBottom12)()("Dare to be great"),
                Typography()()("Turn your body into a lean, mean, awesome machine with Iron Man Gym Fitness Web Application"),
                Typography()()("Designed by trainers, the web app provides heart-pumping training programs dedicated to keep fitness enthusiast reinvigorated. Each goal and achievement will be recorded to keep you inline with your dreams of having the perfect physique. ")
              )
            ),
            Card()()(
              CardContent()()(
                IronmanMap(),
                Typography(variant   = Typography.Variant.body2, className = marginTop12)()("Visit us at: "),
                Typography(variant = Typography.Variant.body2)()("Sitra, Kingdom of Bahrain"),
                Typography(variant = Typography.Variant.body2)()("Call Us: 3989 0780"),
                Typography(variant = Typography.Variant.body2)()("Opens: 6 am to 11 pm")
              )
            )
          ),
          Grid(item = true, xs = 12, md = 4)()(
            Registration(p.router, p.proxy)
          )
        )
      )
  }

  private val component = ScalaComponent.builder[Props]("Logout")
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[RootModel]): VdomElement =
    component(Logout.Props(router, proxy))
}
