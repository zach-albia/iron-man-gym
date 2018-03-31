package com.ironmangym.about

import com.pangwarta.sjrmui.{ Card, CardContent, CardMedia, Typography }
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object About {
  val component =
    ScalaComponent.builder[Unit]("No args")
      .renderStatic(<.div(
        ^.margin := "0 auto",
        ^.marginTop := 40.px,
        ^.maxWidth := 960.px,
        ^.paddingLeft := 24.px,
        ^.paddingRight := 24.px,
        Card(
          className = "classes.card"
        )()(
          Typography(variant = Typography.Variant.headline)()(" IRON MAN GYM"),
          CardMedia(
            className = "classes.media",
            image     = "target/scala-2.12/classes/workoutChalk.jpg"
          )()(
            Typography(variant = Typography.Variant.caption)()("Lemme check if this is working")
          )

        //<.img(
        //^.src := "target/scala-2.12/classes/workoutChalk.jpg",
        //,
        //^.height := 48.px, ^.width := 48.px, ^.marginRight := 12.px
        )
      ))
      .build
  def apply() = component()

}

