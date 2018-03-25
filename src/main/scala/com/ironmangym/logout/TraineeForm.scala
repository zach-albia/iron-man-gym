package com.ironmangym.logout

import java.time.YearMonth

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.domain._
import com.pangwarta.sjrmui._
import diode.react.{ ModelProxy, ReactConnectProxy }
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.raw.HTMLSelectElement

import scala.scalajs.js

object TraineeForm {

  case class State(
      usersWrapper:     ReactConnectProxy[Users],
      traineeName:      Option[String],
      contactNumber:    Option[String],
      birthday:         js.Date,
      height:           Option[String],
      traineeUsername:  Option[String],
      traineePassword:  Option[String],
      traineeFormValid: Boolean
  ) {
    def validate(): State = copy(
      traineeFormValid =
        traineeName.exists(_.nonEmpty) &&
          height.exists(_.nonEmpty) &&
          traineeUsername.exists(_.nonEmpty) &&
          traineePassword.exists(_.nonEmpty)
    )
  }

  val monthNames = Seq("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

  private class Backend($: BackendScope[Registration.Props, State]) {
    import Common._
    import Registration.Props

    def render(p: Props, s: State): VdomElement =
      Paper(className = Styles.paperPadding)()(
        Typography(variant = Typography.Variant.headline)()("Sign up as a Trainee"),
        FormControl()()(
          TextField(
            id       = "traineeName",
            label    = "Name",
            required = true,
            onChange = (e: ReactEvent) => traineeNameChanged($, e),
            value    = s.traineeName.getOrElse("").toString,
            error    = s.traineeName.exists(_.isEmpty)
          )()(),
          TextField(
            id       = "traineePhoneNumber",
            label    = "Contact Number (optional)",
            onChange = (e: ReactEvent) => contactNumberChanged($, e),
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
              onChange    = (e: ReactEvent) => birthdayYearChanged($, e)
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
              onChange    = (e: ReactEvent) => birthdayMonthChanged($, e)
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
              onChange    = (e: ReactEvent) => birthdayDateChanged($, e)
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
            onChange   = (e: ReactEvent) => heightChanged($, e),
            error      = s.height.exists(_.isEmpty)
          )()(),
          TextField(
            id       = "traineeUsername",
            label    = "Username",
            required = true,
            value    = s.traineeUsername.getOrElse("").toString,
            onChange = (e: ReactEvent) => traineeUsernameChanged($, e),
            error    = s.traineeUsername.exists(_.isEmpty)
          )()(),
          TextField(
            id       = "traineePassword",
            label    = "Password",
            required = true,
            typ      = "password",
            value    = s.traineePassword.getOrElse("").toString,
            onChange = (e: ReactEvent) => traineePasswordChanged($, e),
            error    = s.traineePassword.exists(_.isEmpty)
          )()(),
          Button(
            variant   = Button.Variant.raised,
            className = Styles.registrationButton,
            disabled  = !s.traineeFormValid,
            onClick   = (e: ReactMouseEvent) => createTraineeAccount($, e)
          )()("Create Account")
        )
      )

    def traineeNameChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val traineeName = getValue(e)
      fieldChanged[Props, State]($, _.copy(traineeName = Some(traineeName)).validate())
    }

    def contactNumberChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val contactNumber = getValue(e)
      fieldChanged[Props, State]($, _.copy(contactNumber = Some(contactNumber)).validate())
    }

    def birthdayYearChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val birthdayYear = getValue(e)
      fieldChanged[Props, State]($, s =>
        s.copy(birthday =
          new js.Date(birthdayYear.toInt, s.birthday.getMonth, math.min(s.birthday.getDate(), daysInMonth(s)))).validate())
    }

    def birthdayMonthChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val birthdayMonth = e.currentTarget.asInstanceOf[HTMLSelectElement].selectedIndex
      fieldChanged[Props, State]($, s =>
        s.copy(birthday =
          new js.Date(s.birthday.getFullYear, birthdayMonth, math.min(s.birthday.getDate(), daysInMonth(s)))).validate())
    }

    def birthdayDateChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val birthdayDate = getValue(e)
      fieldChanged[Props, State]($, s =>
        s.copy(birthday =
          new js.Date(s.birthday.getFullYear, s.birthday.getMonth, birthdayDate.toInt)).validate())
    }

    def heightChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val height = getValue(e)
      fieldChanged[Props, State]($, _.copy(height = Some(height)).validate())
    }

    def traineeUsernameChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val username = getValue(e)
      fieldChanged[Props, State]($, _.copy(traineeUsername = Some(username)).validate())
    }

    def traineePasswordChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val password = getValue(e)
      fieldChanged[Props, State]($, _.copy(traineePassword = Some(password)).validate())
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

    private def daysInMonth(s: State) =
      YearMonth.of(s.birthday.getFullYear, s.birthday.getMonth + 1).lengthOfMonth
  }

  private val component = ScalaComponent.builder[Registration.Props]("Trainee Form")
    .initialStateFromProps(props =>
      State(
        props.proxy.connect(identity),
        traineeName      = None,
        contactNumber    = None,
        birthday         = new js.Date(2000, 0),
        height           = None,
        traineeUsername  = None,
        traineePassword  = None,
        traineeFormValid = false
      ))
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]): VdomElement =
    component(Registration.Props(router, proxy))
}
