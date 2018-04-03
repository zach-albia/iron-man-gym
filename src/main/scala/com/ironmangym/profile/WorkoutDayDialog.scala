package com.ironmangym.profile

import com.ironmangym.common._
import com.ironmangym.domain._
import com.pangwarta.sjrmui.{ Checkbox, Dialog, DialogContent, DialogContentText, DialogTitle, FormControl, FormControlLabel, ReactHandler1, Typography }
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._

import scala.scalajs.js

object WorkoutDayDialog {

  case class Props(
      proxy:           ModelProxy[RootModel],
      traineeUsername: String,
      open:            Boolean,
      workoutDate:     Option[js.Date],
      onClose:         ReactHandler1[ReactEvent]
  ) {
    def trainee: Trainee =
      proxy().users.findTrainee(traineeUsername).get

    def workoutDay: WorkoutDay = (for {
      tp <- trainee.trainingProgram
      wd <- workoutDate
      cwd <- tp.workoutDays.find(v => v.date == toPersistentDate(wd))
    } yield cwd).getOrElse(WorkoutDay())
  }

  private val componentName = "WorkoutDayDialog"

  private class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props): VdomElement =
      Dialog(
        open    = p.open,
        onClose = p.onClose
      )("key" -> componentName)(
        DialogTitle()()(s"${format(p.workoutDay.date)} - ${p.workoutDay.name}"),
        DialogContent()()(Typography(variant = Typography.Variant.subheading)()(
          s"Exercises: ${if (p.workoutDay.exercises.isEmpty) "None" else ""}"
        )),
        DialogContent()()(
          p.workoutDay.exercises.map(v => Typography()()(v).vdomElement): _*
        ),
        DialogContent()()(
          DialogContentText()()("You can record today's stats."),
          FormControl()()(
            FitnessStatsEditor(
              p.trainee.heightInCm,
              p.trainee.age,
              s"dialog-fitness-stats-editor-${p.workoutDay.date.getTime}",
              p.workoutDay.stats,
              fitnessStatsChanged(p.workoutDay)(_)
            ),
            FormControlLabel(
              control = Checkbox(
                checked  = p.workoutDay.done,
                value    = "done",
                onChange = doneChanged(p.workoutDay)(_, _)
              )().rawElement,
              label   = "Done".rawNode
            )()
          )
        )
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

    def fitnessStatsChanged(workoutDay: WorkoutDay)(stats: FitnessStats): Callback =
      changeWorkoutDay(workoutDay.copy(stats = stats))

    def doneChanged(workoutDay: WorkoutDay)(e: ReactEvent, done: Boolean): Callback = {
      Callback.log(done.toString) >> changeWorkoutDay(workoutDay.copy(done = done))
    }

    def changeWorkoutDay(updatedWorkoutDay: WorkoutDay): Callback =
      $.props >>= { p => p.proxy.dispatchCB(WorkoutDayChanged(p.trainee, updatedWorkoutDay)) }
  }

  private val component = ScalaComponent.builder[Props](componentName)
    .backend(new Backend(_))
    .renderBackend
    .componentDidUpdate($ => Callback.log(s"Current workout day: ${$.currentProps.workoutDay.toString}"))
    .build

  def apply(
      proxy:           ModelProxy[RootModel],
      traineeUsername: String,
      open:            Boolean                   = false,
      workoutDate:     Option[js.Date]           = None,
      onClose:         ReactHandler1[ReactEvent] = js.undefined
  ): VdomElement = component(Props(proxy, traineeUsername, open, workoutDate, onClose))
}
