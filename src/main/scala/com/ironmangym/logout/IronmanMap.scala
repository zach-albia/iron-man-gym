package com.ironmangym.logout

import chandu0101.scalajs.react.components.GoogleMap
import chandu0101.scalajs.react.components.fascades.{ LatLng, Marker }
import com.ironmangym.GMapKeys
import com.pangwarta.sjrmui.Typography
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object IronmanMap {

  // EXAMPLE:START

  val latlng = LatLng(26.1229089, 50.589338)
  val marker = Marker(position = latlng, title = "Iron Man Gym")

  private val component = ScalaComponent
    .builder[Unit]("BasicMap")
    .renderStatic(
      <.div(
        Typography(variant = Typography.Variant.headline)()("Basic Map"),
        GoogleMap(
          width   = "600px",
          height  = "500px",
          center  = latlng,
          url     = s"https://maps.googleapis.com/maps/api/js?key=${GMapKeys.key}",
          markers = List(marker),
          zoom = 18,
        )
      )
    )
    .build

  // EXAMPLE:END

  def apply() = component()
}
