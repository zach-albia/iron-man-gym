package com.ironmangym

import com.ironmangym.CssSettings._
import com.ironmangym.Styles._
import com.ironmangym.domain.{RootModel, SPACircuit}
import com.ironmangym.logout._
import com.ironmangym.trainingmodule._
import com.pangwarta.sjrmui._
import diode.react.ModelProxy
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.document

object Main {

  sealed trait Page
  object Page {
    case object Landing extends Page
    case object TrainingModules extends Page
  }

  val routerConfig: RouterConfig[Page] = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    val modelWrapper = SPACircuit.connect(m => m)
    val trainingModulesWrapper = SPACircuit.connect(_.trainingModules)
    (emptyRule
      | staticRoute(root, Page.Landing) ~>
          renderR(ctl => { modelWrapper((proxy: ModelProxy[RootModel]) => Landing(ctl, proxy)) })
      | staticRoute("#training-modules", Page.TrainingModules) ~>
          renderR(ctl => modelWrapper(TrainingModules(ctl, _)))
    ).notFound(redirectToPage(Page.Landing)(Redirect.Replace))
  }.renderWith(layout)

  def layout(c: RouterCtl[Page], r: Resolution[Page]) =
    CssBaseline(
      <.div(
        AppBar(position  = AppBar.static, color = AppBar.default, className = Styles.appBarHeight)()(
          Toolbar(className = Styles.toolbar)()(
            <.img(
              ^.src := "./target/scala-2.12/classes/ironmangym_logo.png",
              ^.height := 48.px, ^.width := 48.px, ^.marginRight := 12.px
            ),
            Typography(
              component = "a",
              variant   = Typography.Variant.headline,
              color     = Typography.Color.inherit,
              className = Styles.ironManGymTitle, // remove comma to re-enable scalariform
            )("href" -> c.baseUrl.value)(
              "Iron Man Gym"
            ),
            SPACircuit.wrap(_.users)(toolbar.AppBarContent(c, _))
          )
        ),
        <.div(^.id := "container", r.render())
      ).rawElement
    )

  def main(args: Array[String]): Unit = {
    val router = Router(BaseUrl.until_#, routerConfig)
    router().renderIntoDOM(document.getElementById("root"))
    Styles.addToDocument()
    addReactBigCalendarStyles()
  }

  private def addReactBigCalendarStyles() = {
    val head = document.head
    val link = document.createElement("link")
    link.setAttribute("type", "text/css")
    link.setAttribute("rel", "stylesheet")
    link.setAttribute("href", "target/scala-2.12/classes/react-big-calendar.css")
    head.appendChild(link)
  }
}
