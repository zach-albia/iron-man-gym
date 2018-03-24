package com.ironmangym.view

import java.time.YearMonth

import com.ironmangym.Main.Page
import com.ironmangym.domain._
import diode.react.{ ModelProxy, ReactConnectProxy }
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.extra.router.RouterCtl
import Styles._
import com.pangwarta.sjrmui.{ Button, FormControl, FormLabel, Grid, Input, InputAdornment, Paper, Select, TextField, Typography }

import scala.scalajs.js

object Registration {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users])

  case class State(
      usersWrapper:  ReactConnectProxy[Users],
      traineeName:   Option[String],
      contactNumber: Option[String],
      birthday:      js.Date,
      username:      Option[String],
      password:      Option[String],
      trainerName:   Option[String]
  )

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
                  required = true,
                  onChange = (e: ReactEvent) => formChange(e)
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
                        year => <.option(^.key := s"year-$year", ^.value := year, year).render
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
                        month => <.option(^.key := s"month-$month", ^.value := month, monthNames(month - 1)).render
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
                        date => <.option(^.key := s"date-$date", ^.value := date, date).render
                      ): _*
                    ),
                  TextField(
                    id         = "height",
                    label      = "Height",
                    required   = true,
                    InputProps = {
                      val adornment = InputAdornment(position = InputAdornment.Position.end)()("meters")
                      val p = js.Dynamic.literal()
                      p.updateDynamic("endAdornment")(adornment.rawNode.asInstanceOf[js.Any])
                      p.asInstanceOf[Input.Props]
                    }
                  )()(),
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
                  )()(),
                  Button(variant   = Button.Variant.raised, className = Styles.registrationButton)()("Create Account")
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
                )()(),
                Button(variant   = Button.Variant.raised, className = Styles.registrationButton)()("Create Account")
              )
            )
          )
        )
      )

    private def daysInMonth(s: State) = YearMonth.of(s.birthday.getFullYear, s.birthday.getMonth + 1).lengthOfMonth

    def formChange(e: ReactEvent): Callback =
      Callback.alert("foo")

  }

  private val component = ScalaComponent.builder[Props]("Registration")
    .initialStateFromProps(props =>
      State(
        props.proxy.connect(identity),
        None,
        None,
        new js.Date(2000, 0),
        None,
        None,
        None
      ))
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]): VdomElement = component(Props(router, proxy))
}
