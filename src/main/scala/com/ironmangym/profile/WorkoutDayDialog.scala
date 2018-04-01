package com.ironmangym.profile

import com.ironmangym.domain.{ Users, WorkoutDay }
import com.pangwarta.sjrmui.{ Dialog, DialogTitle, ReactHandler1 }
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import moment.Moment

import scala.scalajs.js

object WorkoutDayDialog {

  case class Props(
      workoutDay: WorkoutDay,
      proxy:      ModelProxy[Users],
      open:       Boolean,
      onClose:    ReactHandler1[ReactEvent]
  )

  case class State()

  private class Backend($: BackendScope[Props, State]) {
    def render(p: Props, s: State): VdomElement =
      Dialog(
        open    = p.open,
        onClose = p.onClose
      )()(
        DialogTitle()()(format(p.workoutDay.date))
      )

    def format(date: js.Date): String = {
      val monthNames = List(
        "January", "February", "March",
        "April", "May", "June", "July",
        "August", "September", "October",
        "November", "December"
      )

      val day = date.getDate()
      val monthIndex = date.getMonth()

      s"${monthNames(monthIndex)} $day"
    }
  }

  private val component = ScalaComponent.builder[Props]("Workout Day Dialog")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(workoutDay: WorkoutDay, proxy: ModelProxy[Users], open: Boolean = false, onClose: ReactHandler1[ReactEvent] = js.undefined): VdomElement =
    component(Props(workoutDay, proxy, open, onClose))
}
