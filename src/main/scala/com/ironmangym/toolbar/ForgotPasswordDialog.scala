package com.ironmangym.toolbar

import com.ironmangym.common._
import com.pangwarta.sjrmui.{Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, FormControl, ReactHandler1, TextField}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._

import scala.scalajs.js

object ForgotPasswordDialog {

  case class Props(
                    open: Boolean,
                    onClose: ReactHandler1[ReactEvent],
                    onSnackbarClose: ReactHandler1[ReactMouseEvent]
                  )

  case class State(
                    email: Option[String] = None,
                   formValid: Boolean = false,
  ) extends FormState {
    def validate(): State =
      copy(formValid = email.exists(_.matches(emailRegex)))

    def reset(): State =
      copy(email = None, formValid = false)
  }

  private class Backend($: BackendScope[Props, State]) {

    def render(p: Props, s: State): VdomElement =
      Dialog(
        open = p.open,
        onClose = p.onClose,
      )("transitionDuration" -> 0)(
        DialogTitle()()("Forgot Password"),
        DialogContent()()(
          DialogContentText()()("Please enter the email address:"),
          FormControl()()(
            TextField(
              label = "Email",
              required = true,
              onChange = emailChanged($)(_),
              error = hasErrors(s),
              helperText = if (hasErrors(s)) "Please enter a valid email." else js.undefined
            )()()
          ),
          DialogActions()()(
            Button(
              variant = Button.Variant.raised,
              onClick = p.onSnackbarClose,
              disabled = !s.formValid
            )("type" -> "submit")("Send")
          )
        )
      )

    def emailChanged(value: BackendScope[Props, State])(e: ReactEvent): Callback = {
      val email = getInputValue(e)
      fieldChanged[Props, State]($, _.copy(email = Some(email)).validate())
    }
  }

  private def hasErrors(s: State) = {
    !s.formValid && s.email.isDefined
  }

  private val component = ScalaComponent.builder[Props]("ForgotPasswordDialog")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(
           open: Boolean,
           onClose: ReactHandler1[ReactEvent],
           onSnackbarClose: ReactHandler1[ReactMouseEvent]
           ): VdomElement = component(Props(open, onClose, onSnackbarClose))
}
