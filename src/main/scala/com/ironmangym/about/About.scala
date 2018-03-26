package com.ironmangym.about

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object About {
  val component =
    ScalaComponent.builder[Unit]("No args")
      .renderStatic(<.div(
        ^.margin := "0 auto",
        ^.marginTop := 40.px,
        ^.maxWidth := 900.px,
        ^.paddingLeft := 24.px,
        ^.paddingRight := 24.px
      ))

      .build
  def apply() = component()

}

