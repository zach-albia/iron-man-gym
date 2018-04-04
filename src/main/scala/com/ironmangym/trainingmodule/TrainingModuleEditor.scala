package com.ironmangym.trainingmodule

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import com.ironmangym.Styles._
import com.ironmangym.domain.TrainingModule
import com.pangwarta.sjrmui.Typography
import diode.react.ModelProxy
import japgolly.scalajs.react.extra.router.RouterCtl
import scalacss.ScalaCssReact._

object TrainingModuleEditor {

  case class Props(index: String, proxy: ModelProxy[Seq[TrainingModule]], ctl: RouterCtl[Page])

  private val numRegex = "\\d+"

  private class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props): VdomElement = {
      val index = if (p.index.matches(numRegex)) p.index.toInt else -1
      val tm = p.proxy().applyOrElse(index, (_: Int) => TrainingModule())
      <.div(
        Styles.containerDiv,
        Typography(variant = Typography.Variant.title)()("Training Module")
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
