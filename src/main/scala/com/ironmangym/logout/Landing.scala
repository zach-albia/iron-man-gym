package com.ironmangym.logout

import com.ironmangym.Main.Page
import com.ironmangym.domain.RootModel
import com.ironmangym.profile.Profile
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

object Landing {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel])

  private val component = ScalaComponent.builder[Props]("Logout")
    .render_P { p =>
      if (p.proxy().users.currentUser.isEmpty)
        Logout(p.router, p.proxy)
      else
        Profile(p.router, p.proxy)
    }
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[RootModel]): VdomElement =
    component(Props(router, proxy))
}
