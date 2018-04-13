package com.ironmangym.profile

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.common._
import com.ironmangym.domain._
import com.pangwarta.sjrmui._
import diode.react.ModelProxy
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

import scala.collection.immutable.{ NumericRange, Range }

object TrainerProfile {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel], trainerUsername: String) {
    def trainer: Trainer =
      proxy().users.findTrainer(trainerUsername).get

    def trainingData: Seq[TrainingData] =
      proxy().users.trainees
        .filter(_.trainingProgram.isDefined)
        .filter(_.trainingProgram.get.trainer.credentials.username == trainerUsername)
        .map(trainee => TrainingData(
          trainee.name,
          trainee.trainingProgram.get.name,
          trainee.trainingProgram.get.progress,
          trainee.trainingProgram.get.goal.weight,
          trainee.latestWeight,
          trainee.latestBMI,
          trainee.latestBFP,
          trainee.latestBMIAssessment
        ))
  }

  case class TrainingData(
      traineeName:         String,
      trainingProgramName: String,
      workoutProgress:     Progress,
      goalWeight:          Option[Double],
      weight:              Option[Double],
      bmi:                 Option[Double],
      bfp:                 Option[Double],
      bmiAssessment:       Option[Assessment]
  )

  case class State()

  private class Backend($: BackendScope[Props, State]) {
    def render(p: Props, s: State): VdomElement = {
      <.div(
        Styles.containerDiv,
        Grid(container = true, spacing = 24)()(
          Grid(item = true, xs = 12)()(
            Paper(className = Styles.paperPadding)()(
              Typography(variant = Typography.Variant.title)()("Training Assignments"),

              Table()()(
                TableHead()()(
                  TableRow()()(
                    TableCell()()(Typography(variant = Typography.Variant.subheading, align = Typography.Alignment.center)()("Trainee")),
                    TableCell()()(Typography(variant = Typography.Variant.subheading, align = Typography.Alignment.center)()("Training Program")),
                    TableCell()()(Typography(variant = Typography.Variant.subheading, align = Typography.Alignment.center)()("Workout Progress")),
                    TableCell()()(Typography(variant = Typography.Variant.subheading, align = Typography.Alignment.center)()("Weight Goal")),
                    TableCell()()(Typography(variant = Typography.Variant.subheading, align = Typography.Alignment.center)()("Latest Weight")),
                    TableCell()()(Typography(variant = Typography.Variant.subheading, align = Typography.Alignment.center)()("Body Mass Index")),
                    TableCell()()(Typography(variant = Typography.Variant.subheading, align = Typography.Alignment.center)()("Body Fat Percentage"))
                  )
                ),
                if (p.trainingData.nonEmpty) {
                  TableBody()()(
                    p.trainingData.map(td => {
                      val progress = td.workoutProgress
                      TableRow()()(
                        TableCell()()(Typography(variant = Typography.Variant.button, align = Typography.Alignment.center)()(td.traineeName)),
                        TableCell()()(Typography(align = Typography.Alignment.center)()(td.trainingProgramName)),
                        TableCell(numeric = true)()(Typography(align = Typography.Alignment.center)()(s"${progress.done} of ${progress.all}")),
                        TableCell(numeric = true)()(Typography(align = Typography.Alignment.center)()(td.goalWeight.map(v => s"${round2f(v)} kg").getOrElse("N/A").toString)),
                        TableCell(numeric = true)()(Typography(align = Typography.Alignment.center)()(td.weight.map(v => s"${round2f(v)} kg").getOrElse("N/A").toString)),
                        TableCell(numeric = true)()(Typography(align = Typography.Alignment.center)()(td.bmi.map(v => s"${round2f(v)} (${td.bmiAssessment.get})").getOrElse("N/A").toString)),
                        TableCell(numeric = true)()(Typography(align = Typography.Alignment.center)()(td.bfp.map(v => s"${round2f(v)} %").getOrElse("N/A").toString))
                      ).vdomElement
                    }): _*
                  )
                } else TableBody()()(
                  TableRow()()(
                    TableCell()()(
                      Typography(variant = Typography.Variant.caption)()("You have no trainee assignments")
                    )
                  )
                )
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
