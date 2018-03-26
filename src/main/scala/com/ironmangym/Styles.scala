package com.ironmangym

import scalacss.DevDefaults._

import scala.language.implicitConversions
import scala.scalajs.js

object Styles extends StyleSheet.Inline {
  import dsl._

  val margins = style(
    margin :=! "0 auto"
  )

  val verticalCenter = style(
    media.minWidth(960.px)(
      position.absolute,
      top(50.%%),
      left(50.%%),
      height(30.%%),
      width(50.%%),
      margin :=! "-15% 0 0 -25%"
    )
  )

  val paperPadding = style(
    padding :=! "24px"
  )

  val registrationBirthday = style(
    marginTop(24.px)
  )

  val registrationButton = style(
    marginTop(24.px)
  )

  val appBarHeight = style(
    height(72.px)
  )

  val ironManGymTitle = style(
    flex := "1",
    textDecoration := "none"
  )

  implicit def styleAToClassName(styleA: StyleA): String =
    styleA.className.value

  implicit def styleAToUndefOrClassName(styleA: StyleA): js.UndefOr[String] =
    js.UndefOr.any2undefOrA(styleA.className.value)

  implicit def stylesToClassName(styleAs: Seq[StyleA]): String =
    styleAs.map(styleAToClassName).mkString(" ")

  implicit def stylesToUndefOrClassName(styleAs: Seq[StyleA]): js.UndefOr[String] =
    stylesToClassName(styleAs)

}
