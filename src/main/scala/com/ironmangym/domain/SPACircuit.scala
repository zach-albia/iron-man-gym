package com.ironmangym.domain

import diode._
import diode.react.ReactConnector
import org.scalajs.dom
import prickle._

import scala.scalajs.js

case class CreateTraineeAccount(trainee: Trainee) extends Action

case class CreateTrainerAccount(trainer: Trainer) extends Action

case class LogIn(user: User) extends Action

case object LogOut extends Action

case class EnrolTrainingProgram(
    trainee:        Trainee,
    trainingModule: TrainingModule,
    goal:           Goal,
    startDate:      js.Date
) extends Action

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

import com.ironmangym.domain.Picklers._

object SPACircuit extends Circuit[RootModel] with ReactConnector[RootModel] {
  val usersKey = "users"

  protected def initialModel: RootModel = RootModel(
    fromLocalStorage[Users]("users", Users(
      Seq(),
      Seq(
        Trainee(
          "Foo Dude",
          Date(2000, 1, 1),
          172.0,
          None,
          Credentials("foo", "bar"),
          None
        )
      )
    )),
    fromLocalStorage[Seq[TrainingModule]]("trainingModule", Seq(
      TrainingModule(
        "Leg Day Errday", Beginner, List(
          Routine("Rest", List.empty),
          Routine("Leg Day", List(
            "Curtsy Lunges",
            "Side Lunges",
            "Leg Lifts",
            "Sumo Squats"
          ))
        )
      ),
      TrainingModule(
        "Upper Fix ErrDay", Intermediate, List(
          Routine("Rest", List.empty),
          Routine("Upper Fix", List(
            "Push-ups",
            "Transverse Twists",
            "Bench Press",
            "Long Plank"
          ))
        )
      )
    ))
  )

  private def fromLocalStorage[A](key: String, default: A)(implicit unpickler: Unpickler[A]) =
    Unpickle[A](unpickler).fromString(dom.window.localStorage.getItem(key)).getOrElse(default)

  protected def actionHandler: SPACircuit.HandlerFunction = composeHandlers(
    new AuthHandler(zoomRW(_.users)((m, v) => m.copy(users = v))),
    new TrainingProfileHandler(zoomRW(identity)((_, v) => v))
  )
}

import com.ironmangym.domain.SPACircuit.usersKey

class TrainingProfileHandler[M](modelRW: ModelRW[M, RootModel]) extends ActionHandler(modelRW) {
  def handle = {
    case EnrolTrainingProgram(trainee, trainingModule, goal, date) =>
      val updatedUsers = value.users.enrol(trainee, trainingModule, goal, date)
      dom.window.localStorage.setItem(usersKey, Pickle.intoString(updatedUsers))
      updated(RootModel(updatedUsers, value.trainingModules))
  }
}

class AuthHandler[M](modelRW: ModelRW[M, Users]) extends ActionHandler(modelRW) {

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
