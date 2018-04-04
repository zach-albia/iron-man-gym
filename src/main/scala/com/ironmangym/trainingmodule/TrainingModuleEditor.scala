package com.ironmangym.trainingmodule

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.domain._
import com.pangwarta.sjrmui.icons.MuiSvgIcons.ExpandMoreIcon
import com.pangwarta.sjrmui.{ ExpansionPanel, ExpansionPanelDetails, ExpansionPanelSummary, FormControl, Grid, Paper, Select, TextField, Typography }
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

import scala.scalajs.js

object TrainingModuleEditor {

  case class Props(index: String, proxy: ModelProxy[Seq[TrainingModule]], ctl: RouterCtl[Page])

  private val numRegex = "\\d+"

  private class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props): VdomElement = {
      val index = if (p.index.matches(numRegex)) p.index.toInt else -1
      val tm = p.proxy().applyOrElse(index, (_: Int) => TrainingModule())
      <.div(
        Styles.containerDiv,
        Paper(className = Styles.paperPadding)()(
          Typography(
            variant   = Typography.Variant.title,
            className = Styles.marginBottom12
          )()("Training Module"),
          Grid(container = true)()(
            Grid(item = true, xs = 12)()(
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
                    scala.List[Difficulty](Beginner, Intermediate, Advanced).map(d =>
                      <.option(
                        ^.key := s"difficulty-${d.toString}",
                        ^.value := d.toString, d.toString
                      ).render): _*
                  ),
                Typography(
                  variant   = Typography.Variant.subheading,
                  className = Styles.subheadingMargin
                )()("Routines:"),
                <.div(
                  tm.routines.map(r =>
                    ExpansionPanel()()(
                      ExpansionPanelSummary(expandIcon = ExpandMoreIcon()().rawElement)()(Typography()()(r.name)),
                      ExpansionPanelDetails()()(
                        FormControl()()(
                          TextField(label = "Name", value = r.name)()(),
                          TextField(
                            label     = "Exercises",
                            multiline = true,
                            value     = r.exercises.mkString("\n")
                          )()()
                        )
                      )
                    ).vdomElement): _*
                )
              )
            )
          )
        )
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("TrainingModuleEditor")
    .renderBackend[Backend]
    .componentWillMount($ => redirectOnInvalidIndex($.props))
    .build

  private def redirectOnInvalidIndex(props: Props) =
    if (!validIndex(props.index)) props.ctl.set(Page.TrainingModules) else Callback.empty

  def validIndex(index: String) = index.matches(numRegex) || index == "new"

  def apply(index: String, proxy: ModelProxy[Seq[TrainingModule]], ctl: RouterCtl[Page]): VdomElement =
    component(Props(index, proxy, ctl))
}
