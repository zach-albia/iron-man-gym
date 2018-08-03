package com.ironmangym.toolbar

import com.ironmangym.Main.Page
import com.ironmangym.Styles
import com.ironmangym.Styles._
import com.ironmangym.domain.{ LogOut, Users }
import com.pangwarta.sjrmui.Snackbar.{ Horizontal, Vertical }
import com.pangwarta.sjrmui.icons.MuiSvgIcons.{ AccountBoxIcon, CheckCircleIcon }
import com.pangwarta.sjrmui.{ Button, Hidden, IconButton, Menu, MenuItem, Popover, Snackbar, SnackbarContent, Tooltip, Typography }
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html

import scala.scalajs.js

object AppBarContent {

  case class Props(router: RouterCtl[Page], proxy: ModelProxy[Users])

  case class State(
      anchorEl:                 js.UndefOr[html.Element],
      loginDialogOpen:          Boolean                  = false,
      forgotPasswordDialogOpen: Boolean                  = false,
      snackbarOpen:             Boolean                  = false
  )

  private class Backend($: BackendScope[Props, State]) {

    def render(p: Props, s: State): VdomElement = {
      val currentUser = p.proxy().currentUser
      <.div(
        ^.style := js.Dynamic.literal(display = "contents"),
        Hidden(xsDown = true)()(
          if (currentUser.nonEmpty) {
            Typography(
              variant = Typography.Variant.subheading
            )("style" -> js.Dynamic.literal(verticalAlign = "center"))(currentUser.get.name)
          } else Button(onClick = openLoginDialog(_))()("Sign In")
        ),
        Tooltip(title = if (currentUser.isEmpty) "Not signed in" else s"Signed in as ${currentUser.get.name}")()(
          IconButton(onClick = openMenu(_))()(
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
          onClose            = handleCloseMenu(_)
        )()(
            if (p.proxy().currentUserIsTrainer)
              MenuItem()("onClick" -> handleTrainingModulesMenuItem)("Training Modules")
            else "",
            if (p.proxy().currentUser.isEmpty)
              MenuItem()("onClick" -> handleSignInMenuItem)("Sign In")
            else
              MenuItem()("onClick" -> handleSignOutMenuItem)("Sign Out")
          ),
        LoginDialog(p.router, p.proxy, s.loginDialogOpen, openForgotPasswordDialog(_), closeLoginDialog(_)),
        ForgotPasswordDialog(s.forgotPasswordDialogOpen, closeForgotPasswordDialog(_), onSubmit(_)),
        Snackbar(
          anchorOrigin     = Snackbar.Origin(Left(Horizontal.left), Left(Vertical.bottom)),
          open             = s.snackbarOpen,
          autoHideDuration = 6000,
          onClose          = onSnackbarClose(_, _)
        )()(
            SnackbarContent(
              className = Styles.snackbarBgColor,
              message   = <.span(
                ^.alignItems.center,
                ^.display.flex,
                CheckCircleIcon()(),
                <.span(^.paddingLeft := 8.px, "Login details sent!")
              ).render.rawElement
            )()
          )
      )
    }

    def openMenu(e: ReactEvent): Callback = {
      val anchorElement = e.currentTarget.domAsHtml
      $.modState(_.copy(anchorEl = anchorElement))
    }

    def handleCloseMenu(e: ReactEvent): Callback =
      $.modState(_.copy(anchorEl = js.undefined))

    def openForgotPasswordDialog(e: ReactEvent): Callback =
      $.modState(_.copy(forgotPasswordDialogOpen = true, loginDialogOpen = false))

    def openLoginDialog(e: ReactEvent): Callback =
      $.modState(_.copy(loginDialogOpen = true))

    def closeLoginDialog(e: ReactEvent): Callback =
      $.modState(_.copy(loginDialogOpen = false))

    def closeForgotPasswordDialog(e: ReactEvent): Callback =
      $.modState(_.copy(forgotPasswordDialogOpen = false))

    def onSubmit(e: ReactMouseEvent): Callback =
      $.modState(_.copy(snackbarOpen             = true, forgotPasswordDialogOpen = false))

    def onSnackbarClose(e: ReactEvent, s: String): Callback =
      $.modState(_.copy(snackbarOpen = false))

    val goToTrainingModules: Callback =
      $.props.flatMap(_.router.set(Page.TrainingModules))

    private val handleSignOutMenuItem =
      (e: ReactEvent) => (handleCloseMenu(e) >> ($.props >>= (_.proxy.dispatchCB(LogOut)))).runNow()

    private val handleSignInMenuItem =
      (e: ReactEvent) => (handleCloseMenu(e) >> openLoginDialog(e)).runNow()

    private val handleTrainingModulesMenuItem =
      (e: ReactEvent) => (handleCloseMenu(e) >> goToTrainingModules).runNow()
  }

  private val component = ScalaComponent.builder[Props]("ToolbarContent")
    .initialState(State(js.undefined))
    .renderBackend[Backend]
    .build

  def apply(router: RouterCtl[Page], proxy: ModelProxy[Users]): VdomElement =
    component(Props(router, proxy))
}
