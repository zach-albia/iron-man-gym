package com.ironmangym.view

import java.time.YearMonth

import com.ironmangym.Main.Page
import com.ironmangym.domain._
import com.ironmangym.view.Styles._
import com.pangwarta.sjrmui._
import diode.react.{ ModelProxy, ReactConnectProxy }
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.raw.{ HTMLInputElement, HTMLSelectElement }

import scala.scalajs.js
import scala.scalajs.js.UndefOr._

object Registration {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users])

  case class State(
      usersWrapper:     ReactConnectProxy[Users],
      traineeName:      Option[String],
      contactNumber:    Option[String],
      birthday:         js.Date,
      height:           Option[String],
      traineeUsername:  Option[String],
      traineePassword:  Option[String],
      trainerName:      Option[String],
      trainerUsername:  Option[String],
      trainerPassword:  Option[String],
      traineeFormValid: Boolean,
      trainerFormValid: Boolean
  ) {
    def validate(): State =
      copy(
        traineeFormValid =
          traineeName.exists(_.nonEmpty) &&
            height.exists(_.nonEmpty) &&
            traineeUsername.exists(_.nonEmpty) &&
            traineePassword.exists(_.nonEmpty),
        trainerFormValid =
          trainerName.exists(_.nonEmpty)
      )
  }

  val monthNames = Seq("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

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
                  onChange = (e: ReactEvent) => traineeNameChanged(bs, e),
                  value    = s.traineeName.getOrElse("").toString,
                  error    = s.traineeName.exists(_.isEmpty)
                )()(),
                TextField(
                  id       = "traineePhoneNumber",
                  label    = "Contact Number (optional)",
                  onChange = (e: ReactEvent) => contactNumberChanged(bs, e),
                  value    = s.contactNumber.getOrElse("").toString
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
                    ).asInstanceOf[Select.Props],
                    onChange    = (e: ReactEvent) => birthdayYearChanged(bs, e)
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
                    ).asInstanceOf[Select.Props],
                    onChange    = (e: ReactEvent) => birthdayMonthChanged(bs, e)
                  )()(
                      (0 to 11).map(
                        month => <.option(^.key := s"month-$month", ^.value := month, monthNames(month)).render
                      ): _*
                    ),
                  TextField(
                    id          = "birthdayDate",
                    select      = true,
                    SelectProps = js.Dynamic.literal(
                      native = true,
                      value  = s.birthday.getDate
                    ).asInstanceOf[Select.Props],
                    onChange    = (e: ReactEvent) => birthdayDateChanged(bs, e)
                  )()(
                      (1 to daysInMonth(s)).map(
                        date => <.option(^.key := s"date-$date", ^.value := date, date).render
                      ): _*
                    )
                ),
                TextField(
                  id         = "height",
                  label      = "Height",
                  required   = true,
                  InputProps = js.Dynamic.literal(
                    endAdornment = InputAdornment(position = InputAdornment.Position.end)()("meters")
                      .rawNode.asInstanceOf[js.Any]
                  ).asInstanceOf[Input.Props],
                  typ        = "number",
                  value      = s.height.getOrElse("").toString,
                  onChange   = (e: ReactEvent) => heightChanged(bs, e),
                  error      = s.height.exists(_.isEmpty)
                )()(),
                TextField(
                  id       = "traineeUsername",
                  label    = "Username",
                  required = true,
                  value    = s.traineeUsername.getOrElse("").toString,
                  onChange = (e: ReactEvent) => traineeUsernameChanged(bs, e),
                  error    = s.traineeUsername.exists(_.isEmpty)
                )()(),
                TextField(
                  id       = "traineePassword",
                  label    = "Password",
                  required = true,
                  typ      = "password",
                  value    = s.traineePassword.getOrElse("").toString,
                  onChange = (e: ReactEvent) => traineePasswordChanged(bs, e),
                  error    = s.traineePassword.exists(_.isEmpty)
                )()(),
                Button(
                  variant   = Button.Variant.raised,
                  className = Styles.registrationButton,
                  disabled  = !s.traineeFormValid,
                  onClick   = (e: ReactMouseEvent) => createTraineeAccount(bs, e)
                )()("Create Account")
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
                  required = true,
                  value    = s.trainerName.getOrElse("").toString,
                  onChange = (e: ReactEvent) => trainerNameChanged(bs, e),
                  error    = s.trainerName.exists(_.isEmpty)
                )()(),
                TextField(
                  id       = "trainerUsername",
                  label    = "Username",
                  required = true,
                  value    = s.trainerUsername.getOrElse("").toString,
                  onChange = (e: ReactEvent) => trainerUsernameChanged(bs, e),
                  error    = s.trainerUsername.exists(_.isEmpty)
                )()(),
                TextField(
                  id       = "trainerPassword",
                  label    = "Password",
                  required = true,
                  typ      = "password",
                  value    = s.traineePassword.getOrElse("").toString,
                  onChange = (e: ReactEvent) => trainerPasswordChanged(bs, e),
                  error    = s.trainerPassword.exists(_.isEmpty)
                )()(),
                Button(
                  variant   = Button.Variant.raised,
                  className = Styles.registrationButton,
                  disabled  = !s.trainerFormValid
                )()("Create Account")
              )
            )
          )
        )
      )

    private def daysInMonth(s: State) =
      YearMonth.of(s.birthday.getFullYear, s.birthday.getMonth + 1).lengthOfMonth

    def traineeNameChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val traineeName = getValue(e)
      fieldChanged($, _.copy(traineeName = Some(traineeName)).validate())
    }

    def contactNumberChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val contactNumber = getValue(e)
      fieldChanged($, _.copy(contactNumber = Some(contactNumber)).validate())
    }

    def birthdayYearChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val birthdayYear = getValue(e)
      fieldChanged($, s =>
        s.copy(birthday =
          new js.Date(birthdayYear.toInt, s.birthday.getMonth, math.min(s.birthday.getDate(), daysInMonth(s)))).validate())
    }

    def birthdayMonthChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val birthdayMonth = e.currentTarget.asInstanceOf[HTMLSelectElement].selectedIndex
      fieldChanged($, s =>
        s.copy(birthday =
          new js.Date(s.birthday.getFullYear, birthdayMonth, math.min(s.birthday.getDate(), daysInMonth(s)))).validate())
    }

    def birthdayDateChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val birthdayDate = getValue(e)
      fieldChanged($, s =>
        s.copy(birthday =
          new js.Date(s.birthday.getFullYear, s.birthday.getMonth, birthdayDate.toInt)).validate())
    }

    def heightChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val height = getValue(e)
      fieldChanged($, _.copy(height = Some(height)).validate())
    }

    def traineeUsernameChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val username = getValue(e)
      fieldChanged($, _.copy(traineeUsername = Some(username)).validate())
    }

    def traineePasswordChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val password = getValue(e)
      fieldChanged($, _.copy(traineePassword = Some(password)).validate())
    }

    def trainerNameChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val trainerName = getValue(e)
      fieldChanged($, _.copy(trainerName = Some(trainerName)).validate())
    }

    def trainerUsernameChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val username = getValue(e)
      fieldChanged($, _.copy(trainerUsername = Some(username)).validate())
    }

    def trainerPasswordChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val password = getValue(e)
      fieldChanged($, _.copy(trainerPassword = Some(password)).validate())
    }

    def createTraineeAccount($: BackendScope[Props, State], e: ReactMouseEvent): Callback =
      $.props >>= (p => $.state >>= (s =>
        p.proxy.dispatchCB(
          CreateTraineeAccount(Trainee(
            name            = s.traineeName.get,
            birthday        = Date(s.birthday.getFullYear, s.birthday.getMonth() + 1, s.birthday.getDate),
            height          = s.height.get.toDouble,
            phoneNumber     = s.contactNumber,
            credentials     = Credentials(s.traineeUsername.get, s.traineePassword.get),
            trainingProgram = None
          ))
        )))

    private def getValue(e: ReactEvent) = e.currentTarget.asInstanceOf[HTMLInputElement].value

    private def fieldChanged($: BackendScope[Props, State], copyFunc: State => State) =
      $.modState(copyFunc)
  }

  private val component = ScalaComponent.builder[Props]("Registration")
    .initialStateFromProps(props =>
      State(
        props.proxy.connect(identity),
        traineeName      = None,
        contactNumber    = None,
        birthday         = new js.Date(2000, 0),
        height           = None,
        traineeUsername  = None,
        traineePassword  = None,
        trainerName      = None,
        trainerUsername  = None,
        trainerPassword  = None,
        traineeFormValid = false,
        trainerFormValid = false
      ))
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]): VdomElement = component(Props(router, proxy))
}
