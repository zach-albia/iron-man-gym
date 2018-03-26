package com.ironmangym

import japgolly.scalajs.react.{ BackendScope, CallbackTo, ReactEvent }
import org.scalajs.dom.raw.HTMLInputElement

package object common {
  def getValue(e: ReactEvent): String = e.currentTarget.asInstanceOf[HTMLInputElement].value

  def fieldChanged[Props, State]($: BackendScope[Props, State], copyFunc: State => State): CallbackTo[Unit] =
    $.modState(copyFunc)
}
