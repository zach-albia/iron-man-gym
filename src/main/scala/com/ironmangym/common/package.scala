package com.ironmangym

import japgolly.scalajs.react.{ BackendScope, CallbackTo, ReactEvent }
import org.scalajs.dom.raw.HTMLInputElement

package object common {
  val monthNames = Seq("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

  def getValue(e: ReactEvent): String = e.currentTarget.asInstanceOf[HTMLInputElement].value

  def fieldChanged[Props, State]($: BackendScope[Props, State], copyFunc: State => State): CallbackTo[Unit] =
    $.modState(copyFunc)
}
