package com.ironmangym.profile

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.common._
import com.ironmangym.domain.{ Trainee, Users }
import com.pangwarta.sjrmui.{ Grid, Paper, Typography }
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._
import Styles._

import scala.scalajs.js.UndefOr._

object TraineeProfile {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users], trainee: Trainee)

  case class State()

  private class Backend($: BackendScope[Props, State]) {
    def render(p: Props, s: State): VdomElement = {
      val birthday = p.trainee.birthday
      <.div(
        Styles.containerDiv,
        Grid(container = true)()(
          Grid(item = true, md = 3, sm = 12, xs = 12)()(
            Paper(className = Styles.paperPadding)()(
              Typography(variant = Typography.Variant.title)()(p.trainee.name),
              Typography(className = Styles.marginTop12)()(
                s"Contact Number: ${p.trainee.phoneNumber.getOrElse("None")}"
              ),
              Typography()()(s"Birthday: ${monthNames(birthday.month - 1)} ${birthday.date}, ${birthday.year}"),
              Typography()()(s"Height: ${p.trainee.heightInCm} cm"),
              Typography()()(s"Weight: ${p.trainee.latestWeight.map(w => s"$w kg").getOrElse("N/A")}"),
              Typography()()(s"BMI: ${p.trainee.latestBMI.map(_.toString).getOrElse("N/A")}"),
              Typography()()(s"Body Fat Percentage: ${p.trainee.latestBFP.map(v => s"$v%").getOrElse("N/A")}"),
              <.div(
                Typography(variant   = Typography.Variant.subheading, className = Styles.marginTop24)()(
                  "Current Training Program"
                ),
                Typography()()()
              )
            )
          ),
          Grid(item = true, md = 9, sm = 12, xs = 12)()(
            Paper(className = Styles.paperPadding)()("Bar")
          )
        )
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("TraineeProfile")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users], trainee: Trainee): VdomElement =
    component(Props(router, proxy, trainee))
}
