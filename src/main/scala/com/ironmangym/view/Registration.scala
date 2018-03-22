package com.ironmangym.view

import com.ironmangym.Main.Page
import com.ironmangym.domain._
import com.pangwarta.sjrmui._
import diode.react.{ ModelProxy, ReactConnectProxy }
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.extra.router.RouterCtl

object Registration {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users])

  case class State(usersWrapper: ReactConnectProxy[Users])

  private val component = ScalaComponent.builder[Props]("Registration")
    .initialStateFromProps(props => State(props.proxy.connect(identity)))
    .renderPS { (_, props, state) =>
      <.div(
        ^.margin := "0 auto",
        ^.marginTop := 40.px,
        ^.maxWidth := 900.px,
        ^.paddingLeft := 24.px,
        ^.paddingRight := 24.px,
        Typography(variant = Typography.Variant.headline)()("Sign Up"),
        FormControl()()(
          FormHelperText()()("Foo")
        )
      )
    }
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]) = component(Props(router, proxy))
}
