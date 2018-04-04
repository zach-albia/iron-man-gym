package com.ironmangym.trainingmodule

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.domain._
import com.pangwarta.sjrmui.icons.MuiSvgIcons.{ DeleteIcon, EditIcon }
import com.pangwarta.sjrmui.{ Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, FormControl, FormHelperText, Grid, IconButton, Paper, Select, Table, TableBody, TableCell, TableHead, TableRow, TextField, Tooltip, Typography }
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
                        Tooltip(title = "Edit")()(IconButton(onClick = openEditPage(tm)(_))()(EditIcon()())),
                        Tooltip(title = "Delete")()(IconButton(onClick = openDeleteDialog(tm)(_))()(DeleteIcon()()))
                      )
                    ).vdomElement): _*
                )
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
        )
      )
    }

    private def closeDeleteDialog(e: ReactEvent) =
      $.modState(_.copy(
        deleteDialogOpen       = false,
        selectedTrainingModule = None
      ))

    private def openEditPage(tm: TrainingModule)(e: ReactEvent) =
      $.props >>= { p =>
        {
          p.router.set(
            Page.TrainingModule(p.proxy().trainingModules.indexWhere(_.name == tm.name).toString)
          )
        }
      }

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
