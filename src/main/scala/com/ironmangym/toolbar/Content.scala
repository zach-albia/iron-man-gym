package com.ironmangym.toolbar

import com.ironmangym.Main.Page
import com.ironmangym.domain.Users
import com.pangwarta.sjrmui.{ Button, Dialog, DialogTitle, Hidden, IconButton, Menu, MenuItem, Popover, Tooltip, Typography }
import com.pangwarta.sjrmui.icons.MuiSvgIcons.AccountBoxIcon
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html

import scala.scalajs.js

object Content {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users])

  case class State(anchorEl: js.UndefOr[html.Element], dialogOpen: Boolean = false)

  private class Backend($: BackendScope[Props, State]) {

    def render(p: Props, s: State): VdomElement = {
      val currentUser = p.proxy().currentUser
      <.div(
        ^.style := js.Dynamic.literal(display = "contents"),
        Hidden(xsDown = true)()(
          if (currentUser.nonEmpty)
            Typography(variant = Typography.Variant.subheading)("style" -> js.Dynamic.literal(
            verticalAlign = "center"
          ))(currentUser.get.name)
          else
            Button(onClick = openLoginDialog(_))()("Sign In")
        ),
        Tooltip(title = if (currentUser.isEmpty) "Not signed in" else s"Signed in as ${currentUser.get.name}")()(
          IconButton(onClick = handleMenu(_))()(
            AccountBoxIcon()()
          )
        ),
        Menu(
          open               = s.anchorEl.isDefined,
          anchorEl           = s.anchorEl,
          anchorOrigin       = js.Dynamic.literal(
            horizontal = "right",
            vertical   = "bottom"
          ).asInstanceOf[Popover.Origin],
          transformOrigin    = js.Dynamic.literal(
            horizontal = "right",
            vertical   = "top"
          ).asInstanceOf[Popover.Origin],
          getContentAnchorEl = null,
          onClose            = handleClose(_)
        )()(
            if (p.proxy().currentUser.isEmpty)
              MenuItem()("onClick" -> handleSignInMenuItem)("Sign In")
            else
              MenuItem()("onClick" -> handleMenuItemClose)("Sign Out")
          ),
        LoginDialog(p.proxy, s.dialogOpen, closeLoginDialog(_))
      )
    }

    def handleMenu(e: ReactEvent): Callback = {
      val anchorElement = e.currentTarget.domAsHtml
      $.modState(_.copy(anchorEl = anchorElement))
    }

    def handleClose(e: ReactEvent): Callback =
      $.modState(_.copy(anchorEl = js.undefined))

    def openLoginDialog(e: ReactEvent): Callback =
      $.modState(_.copy(dialogOpen = true))

    def closeLoginDialog(e: ReactEvent): Callback =
      $.modState(_.copy(dialogOpen = false))

    private val handleMenuItemClose =
      (e: ReactEvent) => handleClose(e).runNow()

    private val handleSignInMenuItem =
      (e: ReactEvent) => {
        handleMenuItemClose(e)
        openLoginDialog(e).runNow()
      }
  }

  private val component = ScalaComponent.builder[Props]("ToolbarContent")
    .initialState(State(js.undefined))
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]): VdomElement =
    component(Props(router, proxy))
}
