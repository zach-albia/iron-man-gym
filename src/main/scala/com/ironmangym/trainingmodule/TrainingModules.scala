package com.ironmangym.trainingmodule

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.domain._
import com.pangwarta.sjrmui.icons.MuiSvgIcons._
import com.pangwarta.sjrmui.{ Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, ExpansionPanel, ExpansionPanelDetails, ExpansionPanelSummary, FormControl, FormHelperText, Grid, IconButton, Paper, Select, Table, TableBody, TableCell, TableHead, TableRow, TextField, Tooltip, Typography }
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

import scala.scalajs.js

object TrainingModules {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel])

  case class State(
      deleteDialogOpen:       Boolean                = false,
      selectedTrainingModule: Option[TrainingModule] = None
  )

  private class Backend($: BackendScope[Props, State]) {
    def render(p: Props, s: State): VdomElement = {
      val tmName = s.selectedTrainingModule.map(_.name).getOrElse("N/A")
      <.div(
        Styles.containerDiv,
        Paper(className = Styles.paperPadding)()(
          Typography(variant   = Typography.Variant.title, className = Styles.marginBottom12)()("Training Modules"),
          <.div(
            p.proxy().trainingModules.map(tm =>
              ExpansionPanel()()(
                ExpansionPanelSummary(expandIcon = ExpandMoreIcon()().rawElement)()(
                  Typography(variant = Typography.Variant.subheading)()(tm.name)
                ),
                ExpansionPanelDetails()()(
                  FormControl(className = Styles.width100)()(
                    <.div(
                      TextField(
                        label     = "Name",
                        value     = tm.name,
                        className = marginRight16
                      )()(),
                      TextField(
                        label       = "Difficulty",
                        select      = true,
                        SelectProps = js.Dynamic.literal(
                          native = true,
                          value  = tm.difficulty.toString
                        ).asInstanceOf[Select.Props]
                      )()(
                          scala.List[Difficulty](Beginner, Intermediate, Advanced).map(d =>
                            <.option(
                              ^.key := s"difficulty-${d.toString}",
                              ^.value := d.toString, d.toString
                            ).render): _*
                        )
                    ),
                    Grid(container = true)()(
                      Grid(item = true, xs = 12)()(
                        Typography(
                          variant   = Typography.Variant.subheading,
                          className = Styles.subheadingMargin,
                          component = "p"
                        )()("Routines"),
                        <.div(
                          tm.routines.zipWithIndex.map {
                            case (r: Routine, i: Int) =>
                              ExpansionPanel()()(
                                ExpansionPanelSummary(expandIcon = ExpandMoreIcon()().rawElement)()(
                                  Typography()()(s"Day ${i + 1}: ${r.name}")
                                ),
                                ExpansionPanelDetails()()(
                                  FormControl()()(
                                    TextField(label = "Name", value = r.name)()(),
                                    TextField(
                                      label     = "Exercises (one per line)",
                                      multiline = true,
                                      value     = r.exercises.mkString("\n"),
                                      className = Styles.marginTop12
                                    )()(),
                                    Button(
                                      variant   = Button.Variant.raised,
                                      className = Styles.marginTop12
                                    )()(SaveIcon()(), "Save")
                                  )
                                )
                              ).vdomElement
                          }: _*
                        ),
                        <.div(
                          ^.marginTop := 12.px,
                          FormControl()()(
                            TextField(
                              label     = "New routine name",
                              className = Styles.marginRight16
                            )()(),
                            Button(
                              variant   = Button.Variant.raised,
                              className = Styles.marginTop12
                            )()(AddBoxIcon()(), "Add Routine")
                          )
                        )
                      )
                    )
                  )
                )
              ).vdomElement): _*
          ),
          FormControl()()(
            TextField(
              label     = "New training module name",
              className = Styles.marginRight16
            )()(),
            Button(
              variant   = Button.Variant.raised,
              className = Styles.marginTop12
            )()(AddBoxIcon()(), "Add Training Module")
          )
        ),
        Dialog(
          open    = s.deleteDialogOpen,
          onClose = closeDeleteDialog(_)
        )("transitionDuration" -> 0)(
            DialogTitle()()("Delete Training Module"),
            DialogContent()()(
              DialogContentText()()(
                s"""Are you sure you want to delete "$tmName"?"""
              )
            ),
            DialogActions()()(
              Button(
                variant = Button.Variant.raised,
                onClick = closeDeleteDialog(_)
              )()("No"),
              Button(
                variant   = Button.Variant.raised,
                color     = Button.Color.primary,
                className = Styles.dangerButton
              )()("Yes")
            )
          )
      )
    }

    private def closeDeleteDialog(e: ReactEvent) =
      $.modState(_.copy(
        deleteDialogOpen       = false,
        selectedTrainingModule = None
      ))

    private def openDeleteDialog(tm: TrainingModule)(e: ReactEvent) =
      $.modState(_.copy(
        deleteDialogOpen       = true,
        selectedTrainingModule = Some(tm)
      ))
  }

  private val component = ScalaComponent.builder[Props]("TrainingModules")
    .initialState(State())
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
