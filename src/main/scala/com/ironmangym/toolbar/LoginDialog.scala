package com.ironmangym.toolbar

import com.ironmangym.Main.Page
import com.ironmangym.common._
import com.ironmangym.domain.{ Credentials, LogIn, Users }
import com.pangwarta.sjrmui.{ Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, FormControl, FormHelperText, Hidden, ReactHandler1, TextField }
import diode.react.{ ModelProxy, ReactConnectProxy }
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

import scala.scalajs.js

object LoginDialog {

  case class Props(
      router:  RouterCtl[Page],
      proxy:   ModelProxy[Users],
      open:    Boolean,
      onClose: ReactHandler1[ReactEvent]
  )

  case class State(
      usersWrapper: ReactConnectProxy[Users],
      username:     Option[String]           = None,
      password:     Option[String]           = None,
      formValid:    Boolean                  = false,
      loginValid:   Option[Boolean]          = None
  ) extends FormState {
    def validate(): State =
      copy(formValid = username.exists(_.nonEmpty) && password.exists(_.nonEmpty))

    def reset(): State =
      copy(username   = None, password = None, formValid = false, loginValid = None)
  }

  private class Backend($: BackendScope[Props, State]) {

    def render(p: Props, s: State): VdomElement =
      Dialog(
        open    = p.open || s.loginValid.contains(false),
        onClose = resetAndClose(_)
      )("onKeyUp" -> { (e: ReactKeyboardEvent) =>
          if (e.keyCode == 13 & s.formValid)
            dom.document.getElementById("loginDialogSubmit").domAsHtml.click()
        })(
          DialogTitle()()("Sign In"),
          DialogContent()()(
            DialogContentText()()("Please sign in to access your profile."),
            FormControl()()(
              TextField(
                label      = "Username",
                autoFocus  = true,
                required   = true,
                onChange   = usernameChanged($)(_),
                error      = s.wasMadeEmpty[State](_.username),
                helperText = if (s.wasMadeEmpty[State](_.username)) "Please enter your username." else js.undefined
              )()(),
              TextField(
                label      = "Password",
                typ        = "password",
                required   = true,
                onChange   = passwordChanged($)(_),
                error      = s.wasMadeEmpty[State](_.password),
                helperText = if (s.wasMadeEmpty[State](_.password)) "Please enter your password" else js.undefined
              )()(),
              Hidden(xsUp = s.loginValid.isEmpty || s.loginValid.contains(true))()(
                FormHelperText(error = true)()("Invalid username and password")
              )
            ),
            DialogActions()()(
              Button(
                onClick  = handleGoButton(_),
                variant  = Button.Variant.raised,
                disabled = !s.formValid
              )("id" -> "loginDialogSubmit")("Go")
            )
          )
        )

    def usernameChanged($: BackendScope[Props, State])(e: ReactEvent): Callback = {
      val username = getInputValue(e)
      fieldChanged[Props, State]($, _.copy(username = Some(username)).validate())
    }

    def passwordChanged($: BackendScope[Props, State])(e: ReactEvent): Callback = {
      val password = getInputValue(e)
      fieldChanged[Props, State]($, _.copy(password = Some(password)).validate())
    }

    def resetAndClose(e: ReactEvent): Callback =
      $.modState(_.reset()) >> closeForm(e)

    def handleGoButton(e: ReactEvent): Callback =
      login(e) >> closeForm(e)

    def closeForm(e: ReactEvent): Callback =
      $.props >>= { p => p.onClose.getOrElse((_: ReactEvent) => Callback.empty)(e) }

    def login(e: ReactEvent): Callback = {
      $.props >>= (p => $.state >>= (s => {
        val formCredentials = Credentials(s.username.get, s.password.get)
        val users = p.proxy()
        val trainer = users.trainers.find(_.credentials == formCredentials)
        val trainee = users.trainees.find(_.credentials == formCredentials)
        if (trainer.nonEmpty) p.proxy.dispatchCB(LogIn(trainer.get)) >> $.modState(_.copy(loginValid = Some(true)))
        else if (trainee.nonEmpty) p.proxy.dispatchCB(LogIn(trainee.get)) >> $.modState(_.copy(loginValid = Some(true)))
        else $.modState(_.copy(loginValid = Some(false)))
      }))
    }
  }

  private val component = ScalaComponent.builder[Props]("Login Form")
    .initialStateFromProps(p => State(p.proxy.connect(identity)))
    .renderBackend[Backend]
    .build

  def apply(
      router:  RouterCtl[Page],
      proxy:   ModelProxy[Users],
      open:    Boolean,
      onClose: ReactHandler1[ReactEvent]
  ): VdomElement = component(Props(router, proxy, open, onClose))
}
