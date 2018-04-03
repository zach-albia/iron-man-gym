package com.ironmangym.profile

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.domain._
import com.ironmangym.common._
import com.pangwarta.sjrmui.{ Grid, Paper, Table, TableBody, TableCell, TableHead, TableRow, Typography }
import diode.react.ModelProxy
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

object TrainerProfile {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel], trainerUsername: String) {
    def trainer: Trainer =
      proxy().users.trainers.find(_.credentials.username == trainerUsername).get

    def trainingData: Seq[TrainingData] =
      proxy().users.trainees
        .filter(_.trainingProgram.isDefined)
        .filter(_.trainingProgram.get.trainer.credentials.username == trainerUsername)
        .map(trainee => TrainingData(
          trainee.name,
          trainee.trainingProgram.get.name,
          trainee.trainingProgram.get.progress,
          trainee.latestWeight,
          trainee.latestBMI,
          trainee.latestBFP
        ))
  }

  case class TrainingData(
      traineeName:         String,
      trainingProgramName: String,
      workoutProgress:     Progress,
      weight:              Option[Double],
      bmi:                 Option[Double],
      bfp:                 Option[Double]
  )

  case class State()

  private class Backend($: BackendScope[Props, State]) {
    def render(p: Props, s: State): VdomElement = {
      <.div(
        Styles.containerDiv,
        Grid(container = true, spacing = 24)()(
          Grid(item = true)()(
            Paper(className = Styles.paperPadding)()(
              Typography(variant = Typography.Variant.title)()("Trainer name"),
              Table()()(
                TableHead()()(
                  TableRow()()(
                    TableCell()()("Trainee"),
                    TableCell()()("Training Program"),
                    TableCell()()("Workout Progress"),
                    TableCell()()("Weight"),
                    TableCell()()("Body Mass Index"),
                    TableCell()()("Body Fat Percentage")
                  )
                )
              ),
              TableBody()()(
                p.trainingData.map(td => {
                  val progress = td.workoutProgress
                  TableRow()()(
                    TableCell()()(td.traineeName),
                    TableCell()()(td.trainingProgramName),
                    TableCell()()(s"${progress.done} of ${progress.all}"),
                    TableCell()()(td.weight.map(v => s"${round2f(v)} kg").getOrElse("N/A").toString),
                    TableCell()()(s"Body Mass Index"),
                    TableCell()()(s"Body Fat Percentage")
                  ).vdomElement
                }): _*
              )
            )
          )
        )
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("TrainerProfile")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[RootModel], trainerUsername: String): VdomElement =
    component(Props(router, proxy, trainerUsername))
}
