package com.ironmangym.logout

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.common._
import com.ironmangym.domain._
import com.pangwarta.sjrmui._
import diode.react.{ ModelProxy, ReactConnectProxy }
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.scalajs.js

object TrainerForm {

  case class State(
      usersWrapper:     ReactConnectProxy[Users],
      trainerName:      Option[String],
      trainerUsername:  Option[String],
      trainerPassword:  Option[String],
      trainerFormValid: Boolean                  = false
  ) extends FormState {
    def validate(): State = copy(
      trainerFormValid = trainerName.exists(_.nonEmpty)
        && trainerUsername.exists(_.nonEmpty)
        && trainerPassword.exists(_.nonEmpty)
    )

    def reset() = copy(
      trainerName     = None,
      trainerUsername = None,
      trainerPassword = None
    )
  }

  private class Backend($: BackendScope[Logout.Props, State]) {
    import Logout.Props

    def render(p: Props, s: State): VdomElement =
      Paper(className = Styles.paperPadding)()(
        Typography(variant = Typography.Variant.headline)()("Sign up as a Trainer"),
        FormControl()()(
          TextField(
            id         = "name",
            label      = "Name",
            required   = true,
            value      = s.trainerName.getOrElse("").toString,
            onChange   = (e: ReactEvent) => trainerNameChanged($, e),
            error      = s.wasMadeEmpty[State](_.trainerName),
            helperText = if (s.wasMadeEmpty[State](_.trainerName)) "Your name is required." else js.undefined
          )()(),
          TextField(
            id         = "trainerUsername",
            label      = "Username",
            required   = true,
            value      = s.trainerUsername.getOrElse("").toString,
            onChange   = (e: ReactEvent) => trainerUsernameChanged($, e),
            error      = s.wasMadeEmpty[State](_.trainerUsername),
            helperText = if (s.wasMadeEmpty[State](_.trainerUsername)) "A unique username is required." else js.undefined
          )()(),
          TextField(
            id         = "trainerPassword",
            label      = "Password",
            required   = true,
            typ        = "password",
            value      = s.trainerPassword.getOrElse("").toString,
            onChange   = (e: ReactEvent) => trainerPasswordChanged($, e),
            error      = s.wasMadeEmpty[State](_.trainerPassword),
            helperText = if (s.wasMadeEmpty[State](_.trainerPassword)) "A password is required." else js.undefined
          )()(),
          Button(
            variant   = Button.Variant.raised,
            className = Styles.registrationButton,
            disabled  = !s.trainerFormValid,
            onClick   = createTrainerAccount($)(_)
          )()("Create Account")
        )
      )

    def trainerNameChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val trainerName = getValue(e)
      fieldChanged[Props, State]($, _.copy(trainerName = Some(trainerName)).validate())
    }

    def trainerUsernameChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val username = getValue(e)
      fieldChanged[Props, State]($, _.copy(trainerUsername = Some(username)).validate())
    }

    def trainerPasswordChanged($: BackendScope[Props, State], e: ReactEvent): Callback = {
      val password = getValue(e)
      fieldChanged[Props, State]($, _.copy(trainerPassword = Some(password)).validate())
    }

    def createTrainerAccount($: BackendScope[Props, State])(e: ReactMouseEvent): Callback =
      ($.props >>= (p => $.state >>= (s =>
        p.proxy.dispatchCB(
          CreateTrainerAccount(Trainer(
            name        = s.trainerName.get,
            credentials = Credentials(s.trainerUsername.get, s.trainerPassword.get)
          ))
        )))) >> $.modState(_.reset())
  }

  private val component = ScalaComponent.builder[Logout.Props]("Trainer Form")
    .initialStateFromProps(props =>
      State(
        props.proxy.connect(identity),
        trainerName     = None,
        trainerUsername = None,
        trainerPassword = None
      ))
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]): VdomElement =
    component(Logout.Props(router, proxy))
}
