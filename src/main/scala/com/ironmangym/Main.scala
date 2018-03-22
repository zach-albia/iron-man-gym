package com.ironmangym

import com.ironmangym.CssSettings._
import com.ironmangym.domain.SPACircuit
import com.ironmangym.view._
import com.pangwarta.sjrmui._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

object Main {

  sealed trait Page
  object Page {
    case object Registration extends Page
    case object Login extends Page
  }

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (
      staticRoute(root, Page.Registration) ~>
      renderR(ctl => SPACircuit.wrap(_.users)(proxy => Registration(ctl, proxy)))
    ).notFound(redirectToPage(Page.Registration)(Redirect.Replace))
  }.renderWith(layout)

  def layout(c: RouterCtl[Page], r: Resolution[Page]) =
    CssBaseline(
      <.div(
        AppBar(position = AppBar.static, color = AppBar.default)()(
          Toolbar()()(
            <.img(
              ^.src := "./target/scala-2.12/classes/ironmangym_logo.png",
              ^.height := 40.px, ^.width := 40.px, ^.marginRight := 12.px
            ),
            Typography(variant = Typography.Variant.headline, color = Typography.Color.inherit)()(
              "Iron Man Gym"
            )
          )
        ),
        <.div(
          ^.id := "container",
          r.render()
        )
      ).rawElement
    )

  def main(args: Array[String]): Unit = {
    val router = Router(BaseUrl.until_#, routerConfig)
    router().renderIntoDOM(dom.document.getElementById("root"))
    Styles.addToDocument()
  }
}
