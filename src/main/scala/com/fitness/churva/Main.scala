package com.fitness.churva

import com.pangwarta.sjrmui._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

object Main {

  def main(args: Array[String]): Unit = {
    layout.renderIntoDOM(dom.document.getElementById("root"))
  }

  private val layout =
    CssBaseline(
      <.div(
        AppBar(position = AppBar.static, color = AppBar.default)()(
          Toolbar()()(
            Typography(variant = Typography.Variant.title, color = Typography.Color.inherit)()(
              "Fitness Web Application"
            )
          )
        )
      ).rawElement
    )
}
