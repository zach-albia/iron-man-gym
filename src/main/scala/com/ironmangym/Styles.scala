package com.ironmangym

import scalacss.DevDefaults._

import scala.language.implicitConversions
import scala.scalajs.js

object Styles extends StyleSheet.Inline {
  import dsl._

  private val maxWidthPx = 1170.px

  val containerDiv = style(
    margin :=! "40px auto 0 auto",
    maxWidth(maxWidthPx),
    paddingLeft(24.px),
    paddingRight(24.px)
  )

  val or = style(
    marginTop(24.px),
    marginBottom(24.px)
  )

  val paperPadding = style(
    media.minWidth(600.px)(
      padding(24.px)
    ),
    media.maxWidth(599.px)(
      padding(12.px)
    )
  )

  val marginTop24 = style(
    marginTop(24.px)
  )

  val marginTop12 = style(
    marginTop(12.px)
  )

  val marginBottom12 = style(
    marginBottom(12.px)
  )

  val subheadingMargin = style(
    marginTop(24.px),
    marginBottom(12.px)
  )

  val appBarHeight = style(
    height(72.px)
  )

  val toolbar = style(
    media.minWidth(maxWidthPx)(
      height(72.px),
      marginLeft.auto,
      marginRight.auto,
      width(maxWidthPx)
    )
  )

  val ironManGymTitle = style(
    flex := "1",
    textDecoration := "none"
  )

  val logoutCardMedia = style(
    height(420.px)
  )

  val mapCardMedia = style(
    marginTop(20.px),
    height(300.px),
    width(300.px)
  )

  val root = style(
    width(1000.px),
    marginTop(50.px)
  )

  val table = style(
    minWidth(700.px)
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
