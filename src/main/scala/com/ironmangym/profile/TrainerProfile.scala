package com.ironmangym.profile

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.domain._
import com.pangwarta.sjrmui.{ Grid, Paper, Table, TableCell, TableHead, TableRow, Typography }
import diode.react.ModelProxy
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

object TrainerProfile {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[RootModel], trainerUsername: String) {
    def trainer: Trainer =
      proxy().users.trainers.find(_.credentials.username == trainerUsername).get
  }

  private val component = ScalaComponent.builder[Props]("TrainerProfile")
    .render_P { p =>
      <.div(
        Styles.containerDiv,
        Grid(container = true, spacing = 24)()(
          Grid(item = true, md = 3, sm = 12, xs = 12)()(
            Paper(className = Styles.paperPadding)()(
              Typography(variant = Typography.Variant.title)()("Trainer name"),
              Table()()(
                TableHead()()(
                  TableRow()()(
                    TableCell()()(s"Sno"),
                    TableCell()()(s"Trainee"),
                    TableCell()()(s"Training Program"),
                    TableCell()()(s"Workout Progress"),
                    TableCell()()(s"Weight"),
                    TableCell()()(s"Body Mass Index"),
                    TableCell()()(s"Body Fat Percentage")
                  )
                )
              )
            )

          )
        )
      )

    }
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[RootModel], trainerUsername: String): VdomElement =
    component(Props(router, proxy, trainerUsername))
}
