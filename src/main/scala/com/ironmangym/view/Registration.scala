package com.ironmangym.view

import com.ironmangym.Main.Page
import com.ironmangym.domain._
import com.pangwarta.sjrmui._
import diode.react.{ ModelProxy, ReactConnectProxy }
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.extra.router.RouterCtl
import Styles._

import scala.scalajs.js

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
        Grid(container = true)()(
          Grid(item = true, xs = 12, md = 5)()(
            Paper(className = Styles.paperPadding)()(
              Typography(variant = Typography.Variant.headline)()("Sign up as a Trainee"),
              FormControl()()(
                TextField(
                  id    = "name",
                  label = "Name"
                )()()
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
                  id    = "name",
                  label = "Name"
                )()()
              )
            )
          )
        )
      )
    }
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]) = component(Props(router, proxy))
}
