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
import scalacss.ScalaCssReact._

import scala.scalajs.js

object TrainingModules {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel])

  case class State(
      deleteDialogOpen: Boolean             = false,
      trainingModules:  Seq[TrainingModule]
  )

  private class Backend($: BackendScope[Props, State]) {
    def render(p: Props, s: State): VdomElement = {
      val marginRight = js.Dynamic.literal(marginRight = 6.px)
      <.div(
        Styles.containerDiv,
        Typography(variant   = Typography.Variant.title, className = Styles.marginBottom12)()("Training Modules"),
        <.div(
          p.proxy().trainingModules.zipWithIndex.map {
            case (tm: TrainingModule, i: Int) => {
              ExpansionPanel()()(
                ExpansionPanelSummary(expandIcon = ExpandMoreIcon()().rawElement)()(
                  Typography(variant = Typography.Variant.subheading)()(tm.name)
                ),
                ExpansionPanelDetails()()(
                  FormControl(className = Styles.width100)()(
                    Grid(container = true, spacing = 16)()(
                      Grid(item = true, xs = 12, sm = true)()(
                        TextField(
                          label     = "Name",
                          value     = tm.name,
                          className = marginRight16,
                          fullWidth = true
                        )()()
                      ),
                      Grid(item = true, xs = 12, sm = true)()(
                        TextField(
                          label       = "Difficulty",
                          select      = true,
                          SelectProps = js.Dynamic.literal(
                            native = true,
                            value  = tm.difficulty.toString
                          ).asInstanceOf[Select.Props]
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
                                  IconButton()()(AddCircleIcon()())
                                ).rawNode.asInstanceOf[js.Any]
                            ).asInstanceOf[Input.Props]
                          )("style" -> js.Dynamic.literal(flex = "auto"))()
                        )
                      )
                    ),
                    <.div(
                      tm.routines.zipWithIndex.map {
                        case (r: Routine, j: Int) =>
                          ExpansionPanel()()(
                            ExpansionPanelSummary(expandIcon = ExpandMoreIcon()().rawElement)()(
                              Typography(variant = Typography.Variant.subheading)()(s"Day ${j + 1}: ${r.name}")
                            ),
                            ExpansionPanelDetails()()(
                              FormControl(className = Styles.width100)()(
                                TextField(
                                  label     = "Name",
                                  value     = r.name,
                                  fullWidth = true
                                )()(),
                                TextField(
                                  label     = "Exercises (one per line)",
                                  multiline = true,
                                  value     = r.exercises.mkString("\n"),
                                  className = Styles.marginTop12,
                                  fullWidth = true
                                )()()
                              )
                            ),
                            ExpansionPanelActions()()(
                              Button()()(SaveIcon(style = marginRight)(), "Save"),
                              Button()()(DeleteIcon(style = marginRight)(), "Delete")
                            )
                          ).vdomElement
                      }: _*
                    )
                  )
                ),
                ExpansionPanelActions()()(
                  Button()()(SaveIcon(style = marginRight)(), "Save"),
                  Button()()(DeleteIcon(style = marginRight)(), "Delete")
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
                IconButton()()(AddCircleIcon()())
              ).rawNode.asInstanceOf[js.Any]
            ).asInstanceOf[Input.Props]
          )()()
        ),
        Dialog(
          open    = s.deleteDialogOpen,
          onClose = closeDeleteDialog(_)
        )("transitionDuration" -> 0)(
            DialogTitle()()("Delete Training Module"),
            DialogContent()()(
              DialogContentText()()(
                s"""Are you sure you want to delete "Blah"?"""
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
        deleteDialogOpen = false
      ))

    private def openDeleteDialog(tm: TrainingModule)(e: ReactEvent) =
      $.modState(_.copy(
        deleteDialogOpen = true
      ))
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
