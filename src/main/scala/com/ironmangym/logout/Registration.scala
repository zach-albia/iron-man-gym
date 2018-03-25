package com.ironmangym.logout

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.domain._
import com.pangwarta.sjrmui._
import diode.react.{ ModelProxy, ReactConnectProxy }
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js
import scala.scalajs.js.UndefOr._

object Registration {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users])

  private class Backend($: BackendScope[Props, Unit]) {

    def render(p: Props): VdomElement =
      <.div(
        ^.margin := "0 auto",
        ^.marginTop := 40.px,
        ^.maxWidth := 900.px,
        ^.paddingLeft := 24.px,
        ^.paddingRight := 24.px,
        Grid(container = true)()(
          Grid(item = true, xs = 12, md = 5)()(TraineeForm(p.router, p.proxy)),
          Grid(item    = true, xs = 12, md = 2, classes = Map())("style" -> js.Dynamic.literal(position = "relative"))(
            Typography(
              variant   = Typography.Variant.headline,
              align     = Typography.Alignment.center,
              className = Styles.verticalCenter
            )()("or")
          ),
          Grid(item = true, xs = 12, md = 5)()(TrainerForm(p.router, p.proxy))
        )
      )
  }

  private val component = ScalaComponent.builder[Props]("Registration")
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]): VdomElement = component(Props(router, proxy))
}

object Common {
  def getValue(e: ReactEvent): String = e.currentTarget.asInstanceOf[HTMLInputElement].value

  def fieldChanged[Props, State]($: BackendScope[Props, State], copyFunc: State => State): CallbackTo[Unit] =
    $.modState(copyFunc)
}
