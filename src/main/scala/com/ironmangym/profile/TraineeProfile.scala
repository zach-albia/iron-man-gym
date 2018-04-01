package com.ironmangym.profile

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.common._
import com.ironmangym.domain._
import com.ironmangym.wrapper.BigCalendar
import com.pangwarta.sjrmui.{ Button, FormControl, Grid, Paper, Select, Typography }
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.raw.ReactElement
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLSelectElement
import scalacss.ScalaCssReact._

import scala.scalajs.js
import scala.scalajs.js.UndefOr._

object TraineeProfile {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel], trainee: Trainee)

  case class State(
      trainingModuleSelection: TrainingModule,
      goal:                    FitnessStats           = FitnessStats(),
      date:                    js.Date                = new js.Date(),
      workoutDayDialogOpen:    Boolean                = false,
      selectedWorkoutDay:      js.UndefOr[WorkoutDay] = js.undefined
  )

  private class Backend($: BackendScope[Props, State]) {
    def render(p: Props, s: State): VdomElement = {
      val trainingModules = p.proxy().trainingModules
      val currentTrainingProgram = p.trainee.trainingProgram
      val usersWrapper = SPACircuit.connect(_.users)
      <.div(
        Styles.containerDiv,
        Grid(container = true, spacing = 24)()(
          Grid(item = true, md = 3, sm = 12, xs = 12)()(
            Paper(className = Styles.paperPadding)()(
              Typography(variant   = Typography.Variant.title, className = Styles.marginBottom12)()(p.trainee.name),
              Typography()()(s"Age: ${age(p.trainee.birthday)}"),
              Typography()()(s"Height: ${p.trainee.heightInCm} cm"),
              Typography(
                variant   = Typography.Variant.subheading,
                className = Styles.marginTop24
              )()("Latest Stats"),
              Typography()()(s"Weight: ${p.trainee.latestWeight.map(w => s"$w kg").getOrElse("N/A")}"),
              Typography()()(s"BMI: ${p.trainee.latestBMI.map(_.toString).getOrElse("N/A")}"),
              Typography()()(s"Body Fat Percentage: ${p.trainee.latestBFP.map(v => s"$v%").getOrElse("N/A")}"),
              <.div(
                Typography(
                  variant   = Typography.Variant.subheading,
                  className = Styles.marginTop24
                )()("Current Training Program:"),
                Typography(
                  className = Styles.marginBottom12
                )()(currentTrainingProgram.map(_.name).getOrElse("None").toString),
                Typography(
                  className = Styles.marginTop12
                )()(s"Trainer: ${currentTrainingProgram.map(_.trainer.name).getOrElse("None")}"),
                FormControl()()(
                  Typography(
                    variant   = Typography.Variant.subheading,
                    className = Styles.marginTop24
                  )()(s"${if (currentTrainingProgram.isDefined) "Change" else "Select a"} Training Program"),
                  Select(
                    value     = s.trainingModuleSelection.name,
                    autoWidth = true,
                    native    = true,
                    onChange  = js.UndefOr.any2undefOrA((a: ReactEventFromHtml, b: ReactElement) => trainingModuleSelectionChanged(a, b)) // wtf?
                  )()(trainingModules.map(v => <.option(^.value := v.name)(v.name).render): _*),
                  Typography(
                    className = Styles.marginTop24,
                    variant   = Typography.Variant.subheading
                  )()("Select start date"),
                  DatePicker(onDateChange = dateChanged(_)),
                  Typography(
                    className = Styles.marginTop24,
                    variant   = Typography.Variant.subheading
                  )()("Fitness Goal"),
                  FitnessStatsEditor(
                    heightInCm          = p.trainee.heightInCm,
                    age                 = age(p.trainee.birthday),
                    initialFitnessStats = s.goal,
                    onChange            = fitnessStatsChanged(_)
                  ),
                  Button(
                    variant   = Button.Variant.raised,
                    className = Styles.marginTop24,
                    onClick   = enrolSelection(_)
                  )()(if (currentTrainingProgram.isDefined) "Change" else "Enrol")
                )
              )
            )
          ),
          Grid(item = true, md = 9, sm = 12, xs = 12)()(
            Paper(className = Styles.paperPadding)()(
              Typography(
                variant   = Typography.Variant.title,
                className = Styles.marginBottom12
              )()("Schedule"),
              <.div(
                ^.height := 600.px,
                BigCalendar(
                  defaultDate   = new js.Date(),
                  events        = toEvents(currentTrainingProgram),
                  selectable    = true,
                  views         = js.Array("month", "agenda"),
                  resizable     = true,
                  onSelectEvent = any2undefOrA(openWorkoutDayDialog(_, _))
                )
              )
            )
          )
        ),
        usersWrapper(usersProxy =>
          WorkoutDayDialog(
            s.selectedWorkoutDay.getOrElse(WorkoutDay()),
            usersProxy,
            s.workoutDayDialogOpen,
            (e: ReactEvent) => $.modState(_.copy(workoutDayDialogOpen = false))
          ))
      )
    }

    private def toEvents(currentTrainingProgram: Option[TrainingProgram]) = {
      currentTrainingProgram.map(v => {
        val events = v.workoutDays.foldLeft(js.Array[BigCalendar.Event]())((arr, wd) => {
          arr.push(BigCalendar.event(wd.name, wd.date, wd.date))
          arr
        })
        events.push(BigCalendar.event(v.name, v.startDate, v.endDate))
        events
      }).getOrElse(js.Array[BigCalendar.Event]())
    }

    def trainingModuleSelectionChanged(e: ReactEventFromHtml, elem: ReactElement): Callback = {
      val selection = e.currentTarget.asInstanceOf[HTMLSelectElement].value
      $.props >>= { p =>
        $.modState(s =>
          s.copy(trainingModuleSelection = findTrainingModule(p, selection).get))
      }
    }

    def fitnessStatsChanged(fs: FitnessStats): Callback =
      $.modState(_.copy(goal = fs))

    def dateChanged(date: js.Date): Callback =
      $.modState(_.copy(date = date))

    def enrolSelection(e: ReactMouseEvent): Callback =
      $.props >>= { p =>
        $.state >>= { s =>
          val program = EnrolTrainingProgram(p.trainee, s.trainingModuleSelection, s.goal, s.date)
          p.proxy.dispatchCB(program)
        }
      }

    def openWorkoutDayDialog(e: BigCalendar.Event, re: ReactEvent): js.Any = {
      ($.props >>= { p =>
        val day = p.trainee.trainingProgram.flatMap(_.workoutDays.find(v => v.date == toPersistentDate(e.start))).get
        if (e.start.getTime() == e.end.getTime())
          $.modState(_.copy(workoutDayDialogOpen = true, selectedWorkoutDay = day))
        else Callback.empty
      }).runNow()
      e
    }

    private def findTrainingModule(p: Props, selection: String) = {
      p.proxy().trainingModules.find(_.name == selection)
    }
  }

  private val component = ScalaComponent.builder[Props]("TraineeProfile")
    .initialStateFromProps(p =>
      State(
        p.proxy().trainingModules.head,
        p.trainee.trainingProgram.map(_.goal).getOrElse(FitnessStats())
      ))
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[RootModel], trainee: Trainee): VdomElement =
    component(Props(router, proxy, trainee))
}
