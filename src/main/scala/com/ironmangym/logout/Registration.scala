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
import japgolly.scalajs.react.vdom.VdomElement
import org.scalajs.dom
import org.scalajs.dom.document

import scala.scalajs.js.UndefOr._

object Registration {
  import Logout.Props

  val keyCode_F7 = 118

  private class Backend($: BackendScope[Props, Boolean]) {

    def render(p: Props, hidesTrainerForm: Boolean): VdomElement =
      <.div(
        TraineeForm(p.router, p.proxy),
        Hidden(xsUp = hidesTrainerForm)()(
          Typography(
            variant   = Typography.Variant.headline,
            align     = Typography.Alignment.center,
            className = Styles.or
          )()("or"),

          TrainerForm(p.router, p.proxy)
        )

      )
  }

  private val component = ScalaComponent.builder[Props]("Registration")
    .initialState(true)
    .renderBackend[Backend]
    .componentWillMount($ => {
      document.addEventListener("keydown", (e: dom.KeyboardEvent) => {
        if (e.keyCode == keyCode_F7) $.modState(!_).runNow()
      })
      Callback.log("Added keydown listener")
    })
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[RootModel]): VdomElement = component(Props(router, proxy))
}
