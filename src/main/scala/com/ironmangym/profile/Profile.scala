package com.ironmangym.profile

import com.ironmangym.Main.Page
import com.ironmangym.domain.Users
import diode.react.ModelProxy
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._

object Profile {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users])

  private val component = ScalaComponent.builder[Props]("Profile")
    .render_P { p =>
      val users = p.proxy()
      val currentUser = users.currentUser.get
      val trainer = users.trainers.find(_.credentials.username == currentUser.credentials.username)
      val trainee = users.trainees.find(_.credentials.username == currentUser.credentials.username)
      if (trainer.isDefined)
        TrainerProfile(p.router, p.proxy, trainer.get)
      else
        TraineeProfile(p.router, p.proxy, trainee.get)
    }
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]): VdomElement =
    component(Props(router, proxy))
}
