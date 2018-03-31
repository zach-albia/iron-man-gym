package com.ironmangym.profile

import java.time.temporal.ChronoUnit
import java.time.{ Duration, LocalDate, Year }

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.common._
import com.ironmangym.domain.{ RootModel, Trainee, Users }
import com.ironmangym.wrapper.BigCalendar
import com.pangwarta.sjrmui.{ Grid, MenuItem, Paper, Select, Typography }
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

import scala.scalajs.js
import scala.scalajs.js.UndefOr._

object TraineeProfile {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel], trainee: Trainee)

  case class State()

  private class Backend($: BackendScope[Props, State]) {
    def render(p: Props, s: State): VdomElement = {
      val trainingModules = p.proxy().trainingModules
      <.div(
        Styles.containerDiv,
        Grid(container = true, spacing = 24)()(
          Grid(item = true, md = 3, sm = 12, xs = 12)()(
            Paper(className = Styles.paperPadding)()(
              Typography(variant = Typography.Variant.title)()(p.trainee.name),
              Typography()()(s"Age: ${age(p.trainee.birthday)}"),
              Typography()()(s"Height: ${p.trainee.heightInCm} cm"),
              Typography(
                variant   = Typography.Variant.subheading,
                className = Styles.subheadingMargin
              )()(
                "Latest Metrics"
              ),
              Typography()()(s"Weight: ${p.trainee.latestWeight.map(w => s"$w kg").getOrElse("N/A")}"),
              Typography()()(s"BMI: ${p.trainee.latestBMI.map(_.toString).getOrElse("N/A")}"),
              Typography()()(s"Body Fat Percentage: ${p.trainee.latestBFP.map(v => s"$v%").getOrElse("N/A")}"),
              <.div(
                Typography(
                  variant   = Typography.Variant.subheading,
                  className = Styles.marginTop24
                )()("Training Program:"),
                Typography(
                  variant   = Typography.Variant.subheading,
                  className = Styles.marginBottom12
                )()(p.trainee.trainingProgram.map(_.name).getOrElse("none").toString),
                Select(
                  value     = p.trainee.trainingProgram.map(_.name).getOrElse("").toString,
                  autoWidth = true,
                  native    = true
                )()(trainingModules.map(v => <.option(^.value := v.name)(v.name).render): _*)
              )
            )
          ),
          Grid(item = true, md = 9, sm = 12, xs = 12)()(
            Paper(className = Styles.paperPadding)()(
              <.div(
                ^.height := 600.px,
                BigCalendar(
                  events        = js.Array(
                    BigCalendar.event("Foo", new js.Date(), new js.Date())
                  ),
                  drilldownView = "agenda"
                )
              )
            )
          )
        )
      )
    }

    def age(birthday: js.Date) = {
      val today = new js.Date()
      var age = today.getFullYear - birthday.getFullYear
      val m = today.getMonth - birthday.getMonth
      if (m < 0 || (m == 0 && today.getDate < birthday.getDate)) age -= 1
      age
    }
  }

  private val component = ScalaComponent.builder[Props]("TraineeProfile")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[RootModel], trainee: Trainee): VdomElement =
    component(Props(router, proxy, trainee))
}
