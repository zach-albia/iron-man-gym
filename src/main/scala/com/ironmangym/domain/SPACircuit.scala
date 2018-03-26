package com.ironmangym.domain

import diode._
import diode.react.ReactConnector
import org.scalajs.dom
import prickle._

case class CreateTraineeAccount(trainee: Trainee) extends Action

case class CreateTrainerAccount(trainer: Trainer) extends Action

case class LogIn(user: User) extends Action

case object LogOut extends Action

object SPACircuit extends Circuit[RootModel] with ReactConnector[RootModel] {
  override protected def initialModel: RootModel = RootModel(
    Unpickle[Users].fromString(dom.window.localStorage.getItem("users")).getOrElse(Users()),
    Seq.empty
  )

  override protected def actionHandler: SPACircuit.HandlerFunction = composeHandlers(
    new RegistrationHandler(zoomRW(_.users)((m, v) => m.copy(users = v)))
  )
}

class RegistrationHandler[M](modelRW: ModelRW[M, Users]) extends ActionHandler(modelRW) {
  implicit val traineePickler = Pickler.materializePickler[Trainee]
  implicit val trainingProgramPickler = Pickler.materializePickler[TrainingProgram]
  implicit val workoutDayPickler = Pickler.materializePickler[WorkoutDay]
  implicit val userPickler = Pickler.materializePickler[PersistentUser]
  implicit val usersPickler = Pickler.materializePickler[Users]

  def handle = {
    case CreateTraineeAccount(trainee) =>
      changeUsers(value.copy(trainees = value.trainees :+ trainee))
    case CreateTrainerAccount(trainer) =>
      changeUsers(value.copy(trainers = value.trainers :+ trainer))
    case LogIn(user) =>
      changeUsers(value.copy(currentUser = Some(user)))
    case LogOut =>
      changeUsers(value.copy(currentUser = None))
  }

  private def changeUsers(newUsers: Users) = {
    dom.window.localStorage.setItem("users", Pickle.intoString(newUsers))
    updated(newUsers)
  }
}
