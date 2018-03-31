package com.ironmangym.logout

import java.time.YearMonth

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.common._
import com.ironmangym.domain._
import com.pangwarta.sjrmui._
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.scalajs.js

object TraineeForm {

  case class State(
      name:             Option[String] = None,
      contactNumber:    Option[String] = None,
      birthday:         js.Date,
      heightInCm:       Option[String] = None,
      username:         Option[String] = None,
      password:         Option[String] = None,
      traineeFormValid: Boolean        = false,
      snackbarOpen:     Boolean        = false
  ) extends FormState {
    def validate(): State = copy(
      traineeFormValid =
        name.exists(_.nonEmpty) &&
          heightInCm.exists(_.nonEmpty) &&
          username.exists(_.nonEmpty) &&
          password.exists(_.nonEmpty)
    )

    def reset(): State = copy(
      name             = None,
      contactNumber    = None,
      birthday         = new js.Date(2000, 0),
      heightInCm       = None,
      username         = None,
      password         = None,
      traineeFormValid = false,
      snackbarOpen     = false
    )
  }

  private class Backend($: BackendScope[Logout.Props, State]) {
    import Logout.Props

    def render(p: Props, s: State): VdomElement =
      Paper(className = Styles.paperPadding)()(
        Typography(variant = Typography.Variant.headline)()("Sign up as a Trainee"),
        FormControl()()(
          TextField(
            label      = "Name",
            required   = true,
            onChange   = traineeNameChanged(_),
            value      = s.name.getOrElse("").toString,
            error      = s.wasMadeEmpty[State](_.name),
            autoFocus  = true,
            helperText = if (s.wasMadeEmpty[State](_.name)) "Your name is required." else js.undefined
          )()(),
          TextField(
            label    = "Contact Number (optional)",
            onChange = contactNumberChanged(_),
            value    = s.contactNumber.getOrElse("").toString
          )()(),
          FormLabel(component = "legend", className = Styles.marginTop24)()("Birthday"),
          DatePicker(s.birthday, onDateChange(_)),
          TextField(
            id         = "height",
            label      = "Height",
            required   = true,
            InputProps = js.Dynamic.literal(
              endAdornment = InputAdornment(position = InputAdornment.Position.end)()("cm")
                .rawNode.asInstanceOf[js.Any]
            ).asInstanceOf[Input.Props],
            typ        = "number",
            value      = s.heightInCm.getOrElse("").toString,
            onChange   = heightChanged(_),
            error      = s.wasMadeEmpty[State](_.heightInCm),
            helperText = if (s.wasMadeEmpty[State](_.heightInCm)) "Your heigh in centimeters is required." else js.undefined
          )()(),
          TextField(
            id         = "traineeUsername",
            label      = "Username",
            required   = true,
            value      = s.username.getOrElse("").toString,
            onChange   = traineeUsernameChanged(_),
            error      = s.wasMadeEmpty[State](_.username),
            helperText = if (s.wasMadeEmpty[State](_.username)) "A unique username is required." else js.undefined
          )()(),
          TextField(
            id         = "traineePassword",
            label      = "Password",
            required   = true,
            typ        = "password",
            value      = s.password.getOrElse("").toString,
            onChange   = traineePasswordChanged(_),
            error      = s.wasMadeEmpty[State](_.password),
            helperText = if (s.wasMadeEmpty[State](_.password)) "A password is required." else js.undefined
          )()(),
          Button(
            variant   = Button.Variant.raised,
            className = Styles.marginTop24,
            disabled  = !s.traineeFormValid,
            onClick   = createTraineeAccount(_)
          )()("Create Account"),
          Snackbar(
            anchorOrigin     = Snackbar.Origin(
              Left(Snackbar.Horizontal.center),
              Left(Snackbar.Vertical.center)
            ),
            open             = s.snackbarOpen,
            message          = <.span("Your trainee account has been created. You may now log in with it.").rawElement,
            autoHideDuration = 6000,
            onClose          = onSnackbarClose(_, _)
          )()()
        )
      )

    def traineeNameChanged(e: ReactEvent): Callback = {
      val name = getInputValue(e)
      fieldChanged[Props, State]($, _.copy(name = Some(name)).validate())
    }

    def contactNumberChanged(e: ReactEvent): Callback = {
      val contactNumber = getInputValue(e)
      fieldChanged[Props, State]($, _.copy(contactNumber = Some(contactNumber)).validate())
    }

    def onDateChange(date: js.Date): Callback = $.modState(_.copy(birthday = date).validate())

    def heightChanged(e: ReactEvent): Callback = {
      val height = getInputValue(e)
      fieldChanged[Props, State]($, _.copy(heightInCm = Some(height)).validate())
    }

    def traineeUsernameChanged(e: ReactEvent): Callback = {
      val username = getInputValue(e)
      fieldChanged[Props, State]($, _.copy(username = Some(username)).validate())
    }

    def traineePasswordChanged(e: ReactEvent): Callback = {
      val password = getInputValue(e)
      fieldChanged[Props, State]($, _.copy(password = Some(password)).validate())
    }

    def createTraineeAccount(e: ReactMouseEvent): Callback =
      ($.props >>= (p => $.state >>= (s =>
        p.proxy.dispatchCB(
          CreateTraineeAccount(Trainee(
            name            = s.name.get,
            birthday        = Date(s.birthday.getFullYear, s.birthday.getMonth() + 1, s.birthday.getDate),
            heightInCm      = s.heightInCm.get.toDouble,
            phoneNumber     = s.contactNumber,
            credentials     = Credentials(s.username.get, s.password.get),
            trainingProgram = None
          ))
        )))) >> $.modState(_.reset()) >> $.modState(_.copy(snackbarOpen = true))

    def onSnackbarClose(e: ReactEvent, reason: String): CallbackTo[Unit] =
      $.modState(_.copy(snackbarOpen = false))

    private def daysInMonth(s: State) =
      YearMonth.of(s.birthday.getFullYear, s.birthday.getMonth + 1).lengthOfMonth
  }

  private val component = ScalaComponent.builder[Logout.Props]("Trainee Form")
    .initialState(State(birthday = new js.Date(2000, 0)))
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[RootModel]): VdomElement =
    component(Logout.Props(router, proxy))
}
