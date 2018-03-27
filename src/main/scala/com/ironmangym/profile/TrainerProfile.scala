package com.ironmangym.profile

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.domain.{ Trainer, Users }
import com.pangwarta.sjrmui.Typography
import diode.react.ModelProxy
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

object TrainerProfile {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users], trainer: Trainer)

  private val component = ScalaComponent.builder[Props]("TrainerProfile")
    .render_P { p =>
      <.div(
        Styles.containerDiv,
        Typography(variant = Typography.Variant.title)()("TrainerProfile")
      )
    }
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users], trainer: Trainer): VdomElement =
    component(Props(router, proxy, trainer))
}
