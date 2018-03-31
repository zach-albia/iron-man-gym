package com.ironmangym

import japgolly.scalajs.react.{ BackendScope, CallbackTo, ReactEvent }
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js

package object common {
  val monthNames = Seq("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

  def getInputValue(e: ReactEvent): String = e.currentTarget.asInstanceOf[HTMLInputElement].value

  def fieldChanged[Props, State]($: BackendScope[Props, State], copyFunc: State => State): CallbackTo[Unit] =
    $.modState(copyFunc)

  def age(birthday: js.Date) = {
    val today = new js.Date()
    var age = today.getFullYear - birthday.getFullYear
    val m = today.getMonth - birthday.getMonth
    if (m < 0 || (m == 0 && today.getDate < birthday.getDate)) age -= 1
    age
  }

  def adultBMI(weightInKg: Double, heightInCm: Double) = {
    val heightInM = heightInCm / 100
    weightInKg / (heightInM * heightInM)
  }

  def adultBFP(bmi: Double, age: Int) =
    (1.2 * bmi) + (0.23 * age) - 10.8 - 5.4

  def round2f(d: Double) =
    BigDecimal(d).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
}
