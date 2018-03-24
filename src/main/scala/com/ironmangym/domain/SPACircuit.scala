package com.ironmangym.domain

import diode._
import diode.react.ReactConnector
import org.scalajs.dom
import prickle._

case class CreateTraineeAccount(trainee: Trainee) extends Action

object SPACircuit extends Circuit[RootModel] with ReactConnector[RootModel] {
  override protected def initialModel: RootModel = RootModel(Users(Seq.empty, Seq.empty, None), Seq.empty)

  override protected def actionHandler: SPACircuit.HandlerFunction = composeHandlers(
    new RegistrationHandler(zoomRW(_.users)((m, v) => m.copy(users = v)))
  )
}

class RegistrationHandler[M](modelRW: ModelRW[M, Users]) extends ActionHandler(modelRW) {
  implicit val traineePickler = Pickler.materializePickler[Trainee]
  implicit val trainingProgramPickler = Pickler.materializePickler[TrainingProgram]
  implicit val workoutDayPickler = Pickler.materializePickler[WorkoutDay]

  override def handle = {
    case CreateTraineeAccount(trainee) =>
      dom.window.localStorage.setItem(s"trainee-${trainee.name}", Pickle.intoString(trainee))
      updated(value.copy(trainees = value.trainees :+ trainee))
  }
}
