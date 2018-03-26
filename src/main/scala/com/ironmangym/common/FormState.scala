package com.ironmangym.common

trait FormState {
  def wasMadeEmpty[A <: FormState](pick: A => Option[String]): Boolean =
    pick(this.asInstanceOf[A]).exists(_.isEmpty)
}
