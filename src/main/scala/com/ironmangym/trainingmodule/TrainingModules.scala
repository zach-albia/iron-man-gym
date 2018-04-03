package com.ironmangym.trainingmodule

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.domain.{ PersistentUser, RootModel }
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

object TrainingModules {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel])

  private class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props): VdomElement =
      <.div(Styles.containerDiv)
  }

  private val component = ScalaComponent.builder[Props]("TrainingModules")
    .renderBackend[Backend]
    .componentWillMount($ => {
      val router = $.props.router
      val users = $.props.proxy().users
      val currentUser = users.currentUser.map(PersistentUser.toUser(users, _))
      if (currentUser.forall(_.isTrainer)) Callback.empty else router.set(Page.Landing)
    })
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[RootModel]): VdomElement =
    component(Props(router, proxy))
}
