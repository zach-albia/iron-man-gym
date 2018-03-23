package com.ironmangym.view

import java.time.YearMonth

import com.ironmangym.Main.Page
import com.ironmangym.domain._
import diode.react.{ ModelProxy, ReactConnectProxy }
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.extra.router.RouterCtl
import Styles._
import com.pangwarta.sjrmui.{ FormControl, FormLabel, Grid, Paper, Select, TextField, Typography }

import scala.scalajs.js

object Registration {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users])

  case class State(usersWrapper: ReactConnectProxy[Users], birthday: js.Date)

  val monthNames = List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

  private class Backend(bs: BackendScope[Props, State]) {

    def render(p: Props, s: State): VdomElement =
      <.div(
        ^.margin := "0 auto",
        ^.marginTop := 40.px,
        ^.maxWidth := 900.px,
        ^.paddingLeft := 24.px,
        ^.paddingRight := 24.px,
        Grid(container = true)()(
          Grid(item = true, xs = 12, md = 5)()(
            Paper(className = Styles.paperPadding)()(
              Typography(variant = Typography.Variant.headline)()("Sign up as a Trainee"),
              FormControl()()(
                TextField(
                  id       = "traineeName",
                  label    = "Name",
                  required = true
                )()(),
                TextField(
                  id    = "traineePhoneNumber",
                  label = "Contact Number"
                )()(),
                FormLabel(component = "legend", className = Styles.registrationBirthday)()("Birthday"),
                <.div(
                  ^.overflow.hidden,
                  TextField(
                    id          = "birthdayYear",
                    select      = true,
                    className   = Styles.birthdayYear,
                    SelectProps = js.Dynamic.literal(
                      native = true,
                      value  = s.birthday.getFullYear
                    ).asInstanceOf[Select.Props]
                  )()(
                      (1900 to new js.Date().getFullYear).map(
                        year => <.option(^.key := s"birthday-$year", ^.value := year, year).render
                      ): _*
                    ),
                  TextField(
                    id          = "birthdayMonth",
                    select      = true,
                    SelectProps = js.Dynamic.literal(
                      native = true,
                      value  = s.birthday.getMonth
                    ).asInstanceOf[Select.Props]
                  )()(
                      (1 to 12).map(
                        month => <.option(^.key := s"birthday-$month", ^.value := month, monthNames(month - 1)).render
                      ): _*
                    ),
                  TextField(
                    id          = "birthdayDate",
                    select      = true,
                    SelectProps = js.Dynamic.literal(
                      native = true,
                      value  = s.birthday.getDate
                    ).asInstanceOf[Select.Props]
                  )()(
                      (1 to daysInMonth(s)).map(
                        date => <.option(^.key := s"birthday-$date", ^.value := date, date).render
                      ): _*
                    ),
                  TextField(
                    id       = "traineeUsername",
                    label    = "Username",
                    required = true
                  )()(),
                  TextField(
                    id       = "traineePassword",
                    label    = "Password",
                    required = true,
                    typ      = "password"
                  )()()
                )
              )
            )
          ),
          Grid(item    = true, xs = 12, md = 2, classes = Map())("style" -> js.Dynamic.literal(position = "relative"))(
            Typography(
              variant   = Typography.Variant.headline,
              align     = Typography.Alignment.center,
              className = Styles.verticalCenter
            )()("or")
          ),
          Grid(item = true, xs = 12, md = 5)()(
            Paper(className = Styles.paperPadding)()(
              Typography(variant = Typography.Variant.headline)()("Sign up as a Trainer"),
              FormControl()()(
                TextField(
                  id       = "name",
                  label    = "Name",
                  required = true
                )()()
              )
            )
          )
        )
      )

    private def daysInMonth(s: State) = YearMonth.of(s.birthday.getFullYear, s.birthday.getMonth + 1).lengthOfMonth
  }

  private val component = ScalaComponent.builder[Props]("Registration")
    .initialStateFromProps(props => State(props.proxy.connect(identity), new js.Date(2000, 0)))
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]): VdomElement = component(Props(router, proxy))
}
