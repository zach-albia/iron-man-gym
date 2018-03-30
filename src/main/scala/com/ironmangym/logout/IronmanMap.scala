package com.ironmangym.logout

import chandu0101.macros.tojs.GhPagesMacros
import chandu0101.scalajs.react.components.GoogleMap
import chandu0101.scalajs.react.components.fascades.LatLng
import com.ironmangym.MapStyle
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object IronmanMap {


  val code = GhPagesMacros.exampleSource
  // EXAMPLE:START

  val latlng = LatLng(16.3008, 80.4428)

  private val component = ScalaComponent
    .builder[Unit]("BasicMap")
    .render_P(
      p =>
        <.div(
          <.h2(^.cls := "mui-font-style-headline")("Basic Map"),
          MapStyle(code, "GoogleMapBasic")(
            GoogleMap(width  = "600px", height = "500px", center = latlng, zoom = 8)
          )
        )
    )
    .build

  // EXAMPLE:END

  def apply() = component()
}
