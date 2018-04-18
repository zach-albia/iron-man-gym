package com.ironmangym.profile

import com.ironmangym.Main.Page
import com.ironmangym.domain.RootModel
import diode.react.ModelProxy
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._

object Profile {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel])

  // We only render profiles when a user is logged in.
  private val component = ScalaComponent.builder[Props]("Profile")
    .render_P { p =>
      val users = p.proxy().users
      val currentUser = users.currentUser.get
      val trainerUsername = users.findTrainer(currentUser.credentials.username).map(_.credentials.username)
      val traineeIndex = users.trainees.indexWhere(_.credentials.username == currentUser.credentials.username)
      // It's always either a trainer or trainee at this point.
      if (trainerUsername.isDefined)
        TrainerProfile(p.router, p.proxy, trainerUsername.get)
      else
        TraineeProfile(p.router, p.proxy, traineeIndex, readOnly = false)
    }
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[RootModel]): VdomElement =
    component(Props(router, proxy))
}
