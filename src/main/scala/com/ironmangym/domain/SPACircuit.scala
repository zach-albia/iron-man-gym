package com.ironmangym.domain

import diode._
import diode.react.ReactConnector
import org.scalajs.dom
import prickle._

case class CreateTraineeAccount(trainee: Trainee) extends Action

case class CreateTrainerAccount(trainer: Trainer) extends Action

case class LogIn(user: User) extends Action

case object LogOut extends Action

object Picklers {
  implicit val traineePickler = Pickler.materializePickler[Trainee]
  implicit val trainingProgramPickler = Pickler.materializePickler[TrainingProgram]
  implicit val workoutDayPickler = Pickler.materializePickler[WorkoutDay]
  implicit val userPickler = Pickler.materializePickler[PersistentUser]
  implicit val usersPickler = Pickler.materializePickler[Users]
  implicit val difficultyCompPickler = CompositePickler[Difficulty]
    .concreteType[Beginner.type]
    .concreteType[Intermediate.type]
    .concreteType[Advanced.type]
  implicit val difficultyPickler = difficultyCompPickler.pickler
  implicit val trainingModulePickler = Pickler.materializePickler[TrainingModule]
}

import Picklers._

object SPACircuit extends Circuit[RootModel] with ReactConnector[RootModel] {
  protected def initialModel: RootModel = RootModel(
    fromLocalStorage[Users]("users", Users()),
    fromLocalStorage[Seq[TrainingModule]](
      "trainingModule",
      Seq(TrainingModule(name       = "Foo", difficulty = Beginner))
    )
  )

  private def fromLocalStorage[A](key: String, default: A)(implicit unpickler: Unpickler[A]) = {
    Unpickle[A](unpickler).fromString(dom.window.localStorage.getItem(key)).getOrElse(default)
  }

  protected def actionHandler: SPACircuit.HandlerFunction = composeHandlers(
    new UsersHandler(zoomRW(_.users)((m, v) => m.copy(users = v)))
  )
}

class UsersHandler[M](modelRW: ModelRW[M, Users]) extends ActionHandler(modelRW) {
  val usersKey = "users"

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
    dom.window.localStorage.setItem(usersKey, Pickle.intoString(newUsers))
    updated(newUsers)
  }
}
