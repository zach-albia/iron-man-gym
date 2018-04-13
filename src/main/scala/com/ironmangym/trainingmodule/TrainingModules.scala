package com.ironmangym.trainingmodule

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.common._
import com.ironmangym.domain._
import com.pangwarta.sjrmui._
import icons.MuiSvgIcons._
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.raw.HTMLInputElement
import scalacss.ScalaCssReact._

import scala.scalajs.js

object TrainingModules {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel])

  case class State(trainingModules: Seq[TrainingModule])

  private class Backend($: BackendScope[Props, State]) {
    def render(p: Props, s: State): VdomElement = {
      val marginRight = js.Dynamic.literal(marginRight = 6.px)
      <.div(
        Styles.containerDiv,
        Typography(variant   = Typography.Variant.title, className = Styles.marginBottom12)()("Training Modules"),
        <.div(
          s.trainingModules.zipWithIndex.map {
            case (tm: TrainingModule, i: Int) => {
              ExpansionPanel()("key" -> s"tm-$i")(
                ExpansionPanelSummary(expandIcon = ExpandMoreIcon()().rawElement)()(
                  Typography(variant = Typography.Variant.subheading)()(
                    s"${tm.name}${if (trainingModuleModified(p, s, i)) " (modified)" else ""}"
                  )
                ),
                ExpansionPanelDetails()()(
                  FormControl(className = Styles.width100)()(
                    Grid(container = true, spacing = 16)()(
                      Grid(item = true, xs = 12, sm = true)()(
                        TextField(
                          label     = "Name",
                          value     = tm.name,
                          className = marginRight16,
                          fullWidth = true,
                          onChange  = trainingNameChanged(i)(_)
                        )()()
                      ),
                      Grid(item = true, xs = 12, sm = true)()(
                        TextField(
                          label       = "Difficulty",
                          select      = true,
                          SelectProps = js.Dynamic.literal(
                            native = true,
                            value  = tm.difficulty.toString
                          ).asInstanceOf[Select.Props],
                          onChange    = difficultyChanged(i)(_)
                        )()(
                            difficulties.map(d =>
                              <.option(
                                ^.key := s"difficulty-${d.toString}",
                                ^.value := d.toString, d.toString
                              ).render): _*
                          )
                      )
                    ),
                    Grid(container = true, spacing = 16)()(
                      Grid(item = true, xs = 12, sm = 6)()(
                        Typography(
                          variant   = Typography.Variant.title,
                          className = Styles.marginTop24,
                          component = "p"
                        )()("Routines")
                      ),
                      Grid(item = true, xs = 12, sm = 6)()(
                        <.div(
                          ^.display.flex,
                          ^.marginBottom := 12.px,
                          TextField(
                            label      = "New routine name",
                            className  = Styles.marginRight16,
                            InputProps = js.Dynamic.literal(
                              endAdornment =
                                InputAdornment(position = InputAdornment.Position.end)()(
                                  IconButton(onClick = newRoutine(i)(_))()(AddCircleIcon()())
                                ).rawNode.asInstanceOf[js.Any]
                            ).asInstanceOf[Input.Props]
                          )(
                              "style" -> js.Dynamic.literal(flex = "auto"),
                              "id" -> s"add-routine-$i"
                            )()
                        )
                      )
                    ),
                    <.div(
                      tm.routines.zipWithIndex.map {
                        case (r: Routine, j: Int) =>
                          ExpansionPanel()("key" -> s"tm-$i-r-$j")(
                            ExpansionPanelSummary(expandIcon = ExpandMoreIcon()().rawElement)()(
                              Typography(variant = Typography.Variant.subheading)()(s"Day ${j + 1}: ${r.name}")
                            ),
                            ExpansionPanelDetails()()(
                              FormControl(className = Styles.width100)()(
                                TextField(
                                  label     = "Name",
                                  value     = r.name,
                                  fullWidth = true,
                                  onChange  = routineNameChanged(i, j)(_)
                                )()(),
                                TextField(
                                  label     = "Exercises (one per line)",
                                  multiline = true,
                                  value     = r.exercises.mkString("\n"),
                                  className = Styles.marginTop12,
                                  fullWidth = true,
                                  onChange  = exercisesChanged(i, j)(_)
                                )()()
                              )
                            ),
                            ExpansionPanelActions()()(
                              Button(onClick = removeRoutine(i, j)(_))()(DeleteIcon(style = marginRight)(), "Delete")
                            )
                          ).vdomElement
                      }: _*
                    )
                  )
                ),
                ExpansionPanelActions()()(
                  Button(
                    disabled = !trainingModuleModified(p, s, i),
                    onClick  = saveTrainingModule(_)
                  )()(SaveIcon(style = marginRight)(), "Save"),
                  Button(
                    onClick = deleteTrainingModule(i)(_)
                  )()(DeleteIcon(style = marginRight)(), "Delete")
                )
              ).vdomElement
            }
          }: _*
        ),
        <.div(
          ^.marginTop := 12.px,
          TextField(
            label      = "New training module name",
            className  = Styles.marginRight16,
            fullWidth  = true,
            InputProps = js.Dynamic.literal(
              endAdornment = InputAdornment(position = InputAdornment.Position.end)()(
                IconButton(onClick = newTrainingModule(_))()(AddCircleIcon()())
              ).rawNode.asInstanceOf[js.Any]
            ).asInstanceOf[Input.Props]
          )()()
        )
      )
    }

    private def trainingModuleModified(p: Props, s: State, i: Int) = {
      val tms = p.proxy().trainingModules
      tms(i) != s.trainingModules(i)
    }

    private def trainingNameChanged(i: Int)(e: ReactEvent) = {
      val newName = getInputValue(e)
      fieldChanged[Props, State]($, s => {
        val updated = s.trainingModules.updated(i, s.trainingModules(i).copy(name = newName))
        s.copy(trainingModules = updated)
      })
    }

    private def difficultyChanged(i: Int)(e: ReactEvent) = {
      val difficulty = getInputValue(e)
      fieldChanged[Props, State]($, s => {
        val updated = s.trainingModules.updated(i, s.trainingModules(i).copy(difficulty = difficulty))
        s.copy(trainingModules = updated)
      })
    }

    private def newRoutine(i: Int)(e: ReactEvent) = {
      val input = e.currentTarget.parentNode.previousSibling.domAsHtml.asInstanceOf[HTMLInputElement]
      val routineName = input.value
      input.value = ""
      fieldChanged[Props, State]($, s => {
        val tm = s.trainingModules(i)
        val updated = s.trainingModules.updated(i, tm.copy(routines =
          tm.routines :+ Routine(routineName)))
        s.copy(trainingModules = updated)
      })
    }

    private def removeRoutine(i: Int, j: Int)(e: ReactEvent) =
      fieldChanged[Props, State]($, s => {
        val tm = s.trainingModules(i)
        val updated = s.trainingModules.updated(i, tm.copy(routines =
          tm.routines.take(j) ++ tm.routines.drop(j + 1)))
        s.copy(trainingModules = updated)
      })

    private def newTrainingModule(e: ReactEvent) = {
      val input = e.currentTarget.parentNode.previousSibling.domAsHtml.asInstanceOf[HTMLInputElement]
      val trainingModuleName = input.value
      input.value = ""
      val newTrainingModule = TrainingModule(trainingModuleName)
      fieldChanged[Props, State]($, s => {
        s.copy(s.trainingModules :+ newTrainingModule)
      }) >>
        $.props >>= { p => p.proxy.dispatchCB(CreateTrainingModule(newTrainingModule)) }
    }

    private def deleteTrainingModule(i: Int)(value: Any) =
      fieldChanged[Props, State]($, s =>
        s.copy(trainingModules = s.trainingModules.take(i) ++ s.trainingModules.drop(i + 1))) >>
        $.props >>= (_.proxy.dispatchCB(DeleteTrainingModule(i)))

    private def routineNameChanged(i: Int, j: Int)(e: ReactEvent) = {
      val name = getInputValue(e)
      fieldChanged[Props, State]($, s => {
        val tm = s.trainingModules(i)
        val updated = s.trainingModules.updated(i, tm.copy(routines = {
          val r = tm.routines(j)
          tm.routines.updated(j, r.copy(name = name))
        }))
        s.copy(trainingModules = updated)
      })
    }

    private def exercisesChanged(i: Int, j: Int)(e: ReactEvent) = {
      val exerciseLines = getInputValue(e).split("\n", -1).toSeq
      fieldChanged[Props, State]($, s => {
        val tm = s.trainingModules(i)
        val updated = s.trainingModules.updated(i, tm.copy(routines = {
          val r = tm.routines(j)
          val exercises = if (exerciseLines.forall(_.isEmpty)) Seq.empty else exerciseLines
          tm.routines.updated(j, r.copy(exercises = exercises))
        }))
        s.copy(trainingModules = updated)
      })
    }

    private def saveTrainingModule(e: ReactEvent) =
      $.props >>= { p => $.state >>= { s => p.proxy.dispatchCB(UpdateTrainingModules(s.trainingModules)) } }
  }

  private val component = ScalaComponent.builder[Props]("TrainingModules")
    .initialStateFromProps(p => State(trainingModules = p.proxy().trainingModules))
    .renderBackend[Backend]
    .componentWillMount($ => {
      val router = $.props.router
      val users = $.props.proxy().users
      val currentUser = users.currentUser.map(PersistentUser.toUser(users, _))
      if (currentUser.exists(_.isTrainer)) Callback.empty else router.set(Page.Landing)
    })
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[RootModel]): VdomElement =
    component(Props(router, proxy))
}
