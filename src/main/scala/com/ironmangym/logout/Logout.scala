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
          Grid(item = true, xs = 12, md = 8)()(
            Card()()(
              CardMedia(
                image     = "./target/scala-2.12/classes/ironmangym_inside.jpg",
                className = Styles.logoutCardMedia
              )("title" -> "Iron Man Gym")(),
              CardContent()()(
                Typography(variant   = Typography.Variant.title, className = marginBottom12)()("Get swole here, dude"),
                Typography()()(
                  "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus dignissim sodales nulla, id laoreet quam. Sed euismod nisl id massa feugiat congue. Proin quis metus arcu. Sed condimentum efficitur nisi, in maximus libero consectetur at. Donec ornare arcu nec pretium auctor. Curabitur sodales purus sed massa varius, sit amet consectetur neque pulvinar. Suspendisse dapibus hendrerit vestibulum. Maecenas nec sem non diam porttitor feugiat. Nulla facilisi. Quisque blandit leo condimentum, malesuada ligula eu, consectetur ex."
                ),
                Typography()()(
                  "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Pellentesque ac metus a urna ultricies efficitur at sed sapien. Donec vel lacus sed metus venenatis pharetra. Vivamus ornare mattis felis vel scelerisque. Proin ac posuere enim, at ullamcorper mauris. Sed erat odio, ornare tincidunt ullamcorper at, pharetra at enim. In aliquet mi porttitor egestas euismod. Donec laoreet nibh eget ex fermentum porttitor."
                )
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
