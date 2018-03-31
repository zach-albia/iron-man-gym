package com.ironmangym.logout

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.domain._
import com.pangwarta.sjrmui._
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.scalajs.js.UndefOr._

object Registration {
  import Logout.Props

  private class Backend($: BackendScope[Props, Unit]) {

    def render(p: Props): VdomElement =
      <.div(
        TraineeForm(p.router, p.proxy),
        Typography(
          variant   = Typography.Variant.headline,
          align     = Typography.Alignment.center,
          className = Styles.or
        )()("or"),
        TrainerForm(p.router, p.proxy)
      )
  }

  private val component = ScalaComponent.builder[Props]("Registration")
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[RootModel]): VdomElement = component(Props(router, proxy))
}
