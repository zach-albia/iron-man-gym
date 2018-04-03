package com.ironmangym.trainingmodule

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.domain._
import com.pangwarta.sjrmui.icons.MuiSvgIcons.{ DeleteIcon, EditIcon }
import com.pangwarta.sjrmui.{ Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, FormControl, FormHelperText, Grid, Paper, Select, Table, TableBody, TableCell, TableHead, TableRow, TextField, Typography }
import com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.raw.ReactNode
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

import scala.scalajs.js

object TrainingModules {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel])

  case class State(
      editDialogOpen:         Boolean                = false,
      deleteDialogOpen:       Boolean                = false,
      selectedTrainingModule: Option[TrainingModule] = None
  )

  private class Backend($: BackendScope[Props, State]) {
    def render(p: Props, s: State): VdomElement = {
      val tmName = s.selectedTrainingModule.map(_.name).getOrElse("N/A")
      <.div(
        Styles.containerDiv,
        Grid(container = true)()(
          Grid(item = true, xs = 12)()(
            Paper(className = Styles.paperPadding)()(
              Typography(variant = Typography.Variant.title)()("Training Modules"),
              Table()()(
                TableHead()()(
                  TableRow()()(
                    TableCell()()("Training Program"),
                    TableCell()()("No. of Days"),
                    TableCell()()("Difficulty"),
                    TableCell()()("Action")
                  )
                ),
                TableBody()()(
                  p.proxy().trainingModules.map(tm =>
                    TableRow()()(
                      TableCell()()(tm.name),
                      TableCell()()(tm.routines.length),
                      TableCell()()(tm.difficulty.toString),
                      TableCell()()(
                        Button(onClick = openEditDialog(tm)(_))()(EditIcon()(), "Edit"),
                        Button(onClick = openDeleteDialog(tm)(_))()(DeleteIcon()(), "Delete")
                      )
                    ).vdomElement): _*
                )
              )
            ),
            Dialog(
              open    = s.editDialogOpen,
              onClose = closeEditDialog(_)
            )("transitionDuration" -> 0)(
                DialogTitle()()("Edit Training Module"),
                s.selectedTrainingModule.map(tm =>
                  DialogContent()()(
                    FormControl()()(
                      TextField(label = "Name", value = tm.name)()(),
                      TextField(
                        label       = "Difficulty",
                        select      = true,
                        SelectProps = js.Dynamic.literal(
                          native = true,
                          value  = tm.difficulty.toString
                        ).asInstanceOf[Select.Props]
                      )()(
                          List[Difficulty](Beginner, Intermediate, Advanced).map(d =>
                            <.option(
                              ^.key := s"difficulty-${d.toString}",
                              ^.value := d.toString, d.toString
                            ).render): _*
                        ),
                      Typography(
                        variant   = Typography.Variant.subheading,
                        className = Styles.marginTop24
                      )()("Routines (one per line)")
                    )
                  ).rawNode).getOrElse("".rawNode).asInstanceOf[ReactNode]
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
        )
      )
    }

    private def closeEditDialog(e: ReactEvent) =
      $.modState(_.copy(
        editDialogOpen         = false,
        selectedTrainingModule = None
      ))

    private def closeDeleteDialog(e: ReactEvent) =
      $.modState(_.copy(
        deleteDialogOpen       = false,
        selectedTrainingModule = None
      ))

    private def openEditDialog(tm: TrainingModule)(e: ReactEvent) =
      $.modState(_.copy(
        editDialogOpen         = true,
        selectedTrainingModule = Some(tm)
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
