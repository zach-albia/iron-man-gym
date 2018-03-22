package com.ironmangym.view

import scalacss.DevDefaults._

import scala.language.implicitConversions
import scala.scalajs.js

object Styles extends StyleSheet.Inline {
  import dsl._

  val margins = style(
    margin :=! "0 auto"
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
