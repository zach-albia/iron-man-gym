package com.ironmangym.common

import java.time.YearMonth

import com.pangwarta.sjrmui._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.raw.HTMLSelectElement

import scala.scalajs.js

object DatePicker {

  case class Props(
      initialDate: js.UndefOr[js.Date] = js.undefined,
      onChange:    Handler1[js.Date]   = js.undefined,
      readOnly:    Boolean
  )

  private class Backend($: BackendScope[Props, js.Date]) {
    def render(p: Props, date: js.Date): VdomElement =
      <.div(
        ^.overflow.hidden,
        TextField(
          disabled    = p.readOnly,
          select      = true,
          SelectProps = js.Dynamic.literal(
            native = true,
            value  = date.getFullYear
          ).asInstanceOf[Select.Props],
          onChange    = yearChanged(_)
        )()(
            (1900 to new js.Date().getFullYear).map(year =>
              <.option(^.key := s"year-$year", ^.value := year, year).render): _*
          ),
        TextField(
          disabled    = p.readOnly,
          select      = true,
          SelectProps = js.Dynamic.literal(
            native = true,
            value  = date.getMonth
          ).asInstanceOf[Select.Props],
          onChange    = monthChanged(_)
        )()(
            (0 to 11).map(
              month => <.option(^.key := s"month-$month", ^.value := month, monthNames(month)).render
            ): _* // Seq[VdomNode] => VdomNode*
          ),
        TextField(
          disabled    = p.readOnly,
          select      = true,
          SelectProps = js.Dynamic.literal(
            native = true,
            value  = date.getDate
          ).asInstanceOf[Select.Props],
          onChange    = dateChanged(_)
        )()(
            (1 to daysInMonth(date)).map(
              date => <.option(^.key := s"date-$date", ^.value := date, date).render
            ): _*
          )
      )

    def yearChanged(e: ReactEvent): Callback = {
      val year = getInputValue(e).toInt
      $.props >>= { p =>
        $.state >>= { s =>
          val newDate = new js.Date(year, s.getMonth, math.min(s.getDate, daysInMonth(s)))
          fieldChanged[Props, js.Date]($, s => { newDate }) >>
            p.onChange.map(_(newDate)).getOrElse(Callback.empty)
        }
      }
    }

    def monthChanged(e: ReactEvent): Callback = {
      val month = e.currentTarget.asInstanceOf[HTMLSelectElement].selectedIndex
      $.props >>= { p =>
        $.state >>= { s =>
          val newDate = new js.Date(s.getFullYear, month, math.min(s.getDate, daysInMonth(s)))
          fieldChanged[Props, js.Date]($, s => { newDate }) >>
            p.onChange.map(_(newDate)).getOrElse(Callback.empty)
        }
      }
    }

    def dateChanged(e: ReactEvent): Callback = {
      val date = getInputValue(e).toInt
      $.props >>= { p =>
        $.state >>= { s =>
          val newDate = new js.Date(s.getFullYear, s.getMonth, date)
          fieldChanged[Props, js.Date]($, s => { newDate }) >>
            p.onChange.map(_(newDate)).getOrElse(Callback.empty)
        }
      }
    }

    private def daysInMonth(date: js.Date) =
      YearMonth.of(date.getFullYear, date.getMonth + 1).lengthOfMonth
  }

  private val component = ScalaComponent.builder[Props]("DatePicker")
    .initialStateFromProps(p => p.initialDate.getOrElse(new js.Date()))
    .renderBackend[Backend]
    .build

  def apply(
      date:         js.UndefOr[js.Date] = js.undefined,
      onDateChange: Handler1[js.Date]   = js.undefined,
      readOnly:     Boolean
  ): VdomElement = component(Props(date, onDateChange, readOnly))
}
