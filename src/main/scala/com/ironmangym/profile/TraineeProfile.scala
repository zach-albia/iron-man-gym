package com.ironmangym.profile

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.domain.{ Trainee, Users }
import com.pangwarta.sjrmui.Typography
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

import scala.scalajs.js.UndefOr._

object TraineeProfile {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users], trainee: Trainee)

  private val component = ScalaComponent.builder[Props]("TraineeProfile")
    .render_P { p =>
      <.div(
        Styles.containerDiv,
        Typography(variant = Typography.Variant.title)()("TraineeProfile")
      )
    }
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users], trainee: Trainee): VdomElement =
    component(Props(router, proxy, trainee))
}
