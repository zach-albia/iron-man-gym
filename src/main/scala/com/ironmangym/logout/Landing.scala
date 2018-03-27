package com.ironmangym.logout

import com.ironmangym.Main.Page
import com.ironmangym.domain.Users
import com.ironmangym.profile.{ Profile, TrainerProfile }
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

object Landing {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users])

  private val component = ScalaComponent.builder[Props]("Logout")
    .render_P { p =>
      if (p.proxy().currentUser.isEmpty)
        Logout(p.router, p.proxy)
      else
        Profile(p.router, p.proxy)
    }
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]): VdomElement =
    component(Props(router, proxy))
}
