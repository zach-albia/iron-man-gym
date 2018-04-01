package com.ironmangym.common

import com.ironmangym.domain.FitnessStats
import com.pangwarta.sjrmui.{ FormControl, Handler1, Input, InputAdornment, TextField }
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scala.scalajs.js

object FitnessStatsEditor {

  case class Props(
      heightInCm:          Double,
      age:                 Int,
      key:                 String,
      initialFitnessStats: FitnessStats           = FitnessStats(),
      onChange:            Handler1[FitnessStats] = js.undefined
  )

  private class Backend($: BackendScope[Props, FitnessStats]) {
    def render(p: Props, s: FitnessStats): VdomElement =
      FormControl()("key" -> p.key)(
        TextField(
          label      = "Weight (in kg)",
          InputProps = js.Dynamic.literal(
            endAdornment = InputAdornment(position = InputAdornment.Position.end)()("kg")
              .rawNode.asInstanceOf[js.Any]
          ).asInstanceOf[Input.Props],
          typ        = "number",
          value      = if (s.weight.isDefined) s.weight.get else "",
          onChange   = weightChanged(p.heightInCm, p.age)(_)
        )()(),
        TextField(
          label      = "Body Mass Index",
          InputProps = js.Dynamic.literal(
            endAdornment = InputAdornment(position = InputAdornment.Position.end)()("kg/m²")
              .rawNode.asInstanceOf[js.Any]
          ).asInstanceOf[Input.Props],
          typ        = "number",
          value      = if (s.bodyMassIndex.isDefined) round2f(s.bodyMassIndex.get) else "",
          onChange   = bmiChanged(p.age)(_)
        )()(),
        TextField(
          label      = "Body Fat Percentage",
          InputProps = js.Dynamic.literal(
            endAdornment = InputAdornment(position = InputAdornment.Position.end)()("%")
              .rawNode.asInstanceOf[js.Any]
          ).asInstanceOf[Input.Props],
          typ        = "number",
          value      = if (s.bodyFatPercentage.isDefined) round2f(s.bodyFatPercentage.get) else "",
          onChange   = bfpChanged(_)
        )()()
      )

    def weightChanged(height: Double, age: Int)(e: ReactEvent): Callback = {
      val raw = getInputValue(e)
      val weight = if (raw.nonEmpty) Some(raw.toDouble) else None
      val bmi = weight.map(adultBMI(_, height))
      $.props >>= { p =>
        $.state >>= { s =>
          val newStats = s.copy(
            weight            = weight,
            bodyMassIndex     = bmi,
            bodyFatPercentage = bmi.map(adultBFP(_, age))
          )
          fieldChanged[Props, FitnessStats]($, _ => newStats) >> p.onChange.map(_(newStats)).getOrElse(Callback.empty)
        }
      }
    }

    def bmiChanged(age: Int)(e: ReactEvent): Callback = {
      val raw = getInputValue(e)
      val bmi = if (raw.nonEmpty) Some(raw.toDouble) else None
      $.props >>= { p =>
        $.state >>= { s =>
          val newStats = s.copy(
            bodyMassIndex     = bmi,
            bodyFatPercentage = bmi.map(adultBFP(_, age))
          )
          fieldChanged[Props, FitnessStats]($, _ => newStats) >> p.onChange.map(_(newStats)).getOrElse(Callback.empty)
        }
      }
    }

    def bfpChanged(e: ReactEvent): Callback = {
      $.props >>= { p =>
        $.state >>= { s =>
          val newStats = s.copy(
            bodyFatPercentage = if (getInputValue(e).nonEmpty) Some(getInputValue(e).toDouble) else None
          )
          fieldChanged[Props, FitnessStats]($, _ => newStats) >> p.onChange.map(_(newStats)).getOrElse(Callback.empty)
        }
      }
    }
  }

  private val component = ScalaComponent.builder[Props]("FitnessStatsEditor")
    .initialStateFromProps(_.initialFitnessStats)
    .renderBackend[Backend]
    .build

  def apply(
      heightInCm:          Double,
      age:                 Int,
      key:                 String,
      initialFitnessStats: FitnessStats           = FitnessStats(),
      onChange:            Handler1[FitnessStats] = js.undefined
  ): VdomElement =
    component(Props(heightInCm, age, key, initialFitnessStats, onChange))
}
