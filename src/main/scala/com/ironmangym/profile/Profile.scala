package com.ironmangym.profile

import com.ironmangym.Main.Page
import com.ironmangym.domain.Users
import com.pangwarta.sjrmui.Typography
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement

object Profile {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users])

  private val component = ScalaComponent.builder[Props]("Profile")
    .render_P { p =>
      Typography(variant = Typography.Variant.title)()("Profile")
    }
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]): VdomElement =
    component(Props(router, proxy))
}
