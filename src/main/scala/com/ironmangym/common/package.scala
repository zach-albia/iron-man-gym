package com.ironmangym

import com.ironmangym.domain._
import japgolly.scalajs.react.{ BackendScope, CallbackTo, ReactEvent }
import org.scalajs.dom.raw.HTMLInputElement

import scala.language.implicitConversions
import scala.scalajs.js

package object common {
  val monthNames = Seq("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

  val emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

  //noinspection TypeAnnotation
  val difficulties = List[Difficulty](Beginner, Intermediate, Advanced)
  implicit def strToDifficulty(s: String): Difficulty = s match {
    case s: String if difficulties.exists(_.toString == s) =>
      difficulties.find(_.toString == s).get
    case _ => throw new IllegalArgumentException("Invalid difficulty")
  }

  def getInputValue(e: ReactEvent): String = e.currentTarget.asInstanceOf[HTMLInputElement].value

  def fieldChanged[Props, State]($: BackendScope[Props, State], copyFunc: State => State): CallbackTo[Unit] =
    $.modState(copyFunc)

  def age(birthday: js.Date): Int = {
    val today = new js.Date()
    var age = today.getFullYear - birthday.getFullYear
    val m = today.getMonth - birthday.getMonth
    if (m < 0 || (m == 0 && today.getDate < birthday.getDate)) age -= 1
    age
  }

  def daysAfter(date: js.Date, n: Int): js.Date = {
    val d = new js.Date(date.getTime)
    d.setDate(d.getDate + n)
    d
  }

  def adultBMI(weightInKg: Double, heightInCm: Double): Double = {
    val heightInM = heightInCm / 100
    weightInKg / (heightInM * heightInM)
  }

  def adultBFP(bmi: Double, age: Int): Double =
    (1.2 * bmi) + (0.23 * age) - 10.8 - 5.4

  def round2f(d: Double): Double =
    BigDecimal(d).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble

  implicit def toPersistentDate(d: js.Date): Date =
    Date(d.getFullYear(), d.getMonth(), d.getDate())

  implicit def toJsDate(d: Date): js.Date =
    new js.Date(d.year, d.month, d.date)
}
