package com.ironmangym.domain

import diode.Circuit
import diode.react.ReactConnector

object SPACircuit extends Circuit[RootModel] with ReactConnector[RootModel] {
  override protected def initialModel: RootModel = RootModel(Users(Seq.empty, Seq.empty), Seq.empty)

  override protected def actionHandler: SPACircuit.HandlerFunction = ???
}
