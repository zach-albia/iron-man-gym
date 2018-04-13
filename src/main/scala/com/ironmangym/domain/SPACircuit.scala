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
    goal:           FitnessStats,
    startDate:      js.Date
) extends Action

case class WorkoutDayChanged(trainee: Trainee, updatedWorkoutDay: WorkoutDay) extends Action

case class CreateTrainingModule(trainingModule: TrainingModule) extends Action

case class UpdateTrainingModules(trainingModules: Seq[TrainingModule]) extends Action

case class DeleteTrainingModule(index: Int) extends Action

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
  val trainingModulesKey = "trainingModules"

  protected def initialModel: RootModel = RootModel(
    fromLocalStorage[Users]("users", Users(
      Seq(
        Trainer(
          "Trainer Dude",
          Credentials("abc", "123")
        )
      ),
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
    fromLocalStorage[Seq[TrainingModule]]("trainingModules", Seq(
      TrainingModule(
        "4-Week Beginner's Workout", Beginner, List(
          Routine("Full Body", List(
            "Dumbbell Bench Press - 3 Sets (8, 10, 12 Reps)",
            "Lat Pulldown - 3 Sets (8, 10, 12 Reps)",
            "Overhead Dumbbell Press - 3 Sets (8, 10, 12 Reps)",
            "Leg Press - 3 Sets (8, 10, 12 Reps)",
            "Lying Leg Curl - 3 Sets (8, 10, 12 Reps)",
            "Rope Pressdown - 3 Sets (8, 10, 12 Reps)",
            "Barbell Biceps Curl - 3 Sets (8, 10, 12 Reps)",
            "Standing Calf Raise - 3 Sets (8, 10, 12 Reps)",
            "Crunch - 3 Sets (8, 10, 12 Reps)"
          )),
          Routine("Rest", List.empty),
          Routine("Full Body", List(
            "Dumbbell Bench Press - 3 Sets (8, 10, 12 Reps)",
            "Lat Pulldown - 3 Sets (8, 10, 12 Reps)",
            "Overhead Dumbbell Press - 3 Sets (8, 10, 12 Reps)",
            "Leg Press - 3 Sets (8, 10, 12 Reps)",
            "Lying Leg Curl - 3 Sets (8, 10, 12 Reps)",
            "Rope Pressdown - 3 Sets (8, 10, 12 Reps)",
            "Barbell Biceps Curl - 3 Sets (8, 10, 12 Reps)",
            "Standing Calf Raise - 3 Sets (8, 10, 12 Reps)",
            "Crunch - 3 Sets (8, 10, 12 Reps)"
          )),
          Routine("Rest", List.empty),
          Routine("Full Body", List(
            "Dumbbell Bench Press - 3 Sets (8, 10, 12 Reps)",
            "Lat Pulldown - 3 Sets (8, 10, 12 Reps)",
            "Overhead Dumbbell Press - 3 Sets (8, 10, 12 Reps)",
            "Leg Press - 3 Sets (8, 10, 12 Reps)",
            "Lying Leg Curl - 3 Sets (8, 10, 12 Reps)",
            "Rope Pressdown - 3 Sets (8, 10, 12 Reps)",
            "Barbell Biceps Curl - 3 Sets (8, 10, 12 Reps)",
            "Standing Calf Raise - 3 Sets (8, 10, 12 Reps)",
            "Crunch - 3 Sets (8, 10, 12 Reps)"
          )),
          Routine("Rest", List.empty),
          Routine("Rest", List.empty),
          Routine("Upper Body", List(
            "Barbell Bench Press - 3 Sets (10, 12, 15 Reps)",
            "Dumbbell Flye - 3 Sets (10, 12, 15 Reps)",
            "Barbell Bent - 3 Sets (10, 12, 15 Reps)",
            "Lat Pulldown - 3 Sets (10, 12, 15 Reps)",
            "Overhead Dumbbell Press - 3 Sets (10, 12, 15 Reps)",
            "Dumbbell Lateral Raise - 3 Sets (10, 12, 15 Reps)",
            "Barbell Biceps Curl - 3 Sets (10, 12, 15 Reps)",
            "Preacher Curl With Cable - 3 Sets (10, 12, 15 Reps)",
            "Lying Ez-Bar Triceps Extension - 3 Sets (10, 12, 15 Reps)",
            "Rope Pressdown - 3 Sets (10, 12, 15 Reps)",
            "Crunch - 3 Sets (8, 10, 12 Reps)"
          )),
          Routine("Lower Body", List(
            "Leg Press - 3 Sets (10, 12, 15 Reps)",
            "Lying Leg Curl - 3 Sets (10, 12, 15 Reps)",
            "Seated Legs Curl - 3 Sets (10, 12, 15 Reps)",
            "Standing Calf Raise - 3 Sets (15 - 20 Reps)",
            "Seated Calf Raise - 3 Sets (15 - 20 Reps)"
          )),
          Routine("Rest", List.empty),
          Routine("Upper Body", List(
            "Barbell Bench Press - 3 Sets (10, 12, 15 Reps)",
            "Dumbbell Flye - 3 Sets (10, 12, 15 Reps)",
            "Barbell Bent - 3 Sets (10, 12, 15 Reps)",
            "Lat Pulldown - 3 Sets (10, 12, 15 Reps)",
            "Overhead Dumbbell Press - 3 Sets (10, 12, 15 Reps)",
            "Dumbbell Lateral Raise - 3 Sets (10, 12, 15 Reps)",
            "Barbell Biceps Curl - 3 Sets (10, 12, 15 Reps)",
            "Preacher Curl With Cable - 3 Sets (10, 12, 15 Reps)",
            "Lying Ez-Bar Triceps Extension - 3 Sets (10, 12, 15 Reps)",
            "Rope Pressdown - 3 Sets (10, 12, 15 Reps)",
            "Crunch - 3 Sets (8, 10, 12 Reps)"
          )),
          Routine("Lower Body", List(
            "Leg Press - 3 Sets (10, 12, 15 Reps)",
            "Lying Leg Curl - 3 Sets (10, 12, 15 Reps)",
            "Seated Legs Curl - 3 Sets (10, 12, 15 Reps)",
            "Standing Calf Raise - 3 Sets (15 - 20 Reps)",
            "Seated Calf Raise - 3 Sets (15 - 20 Reps)"
          )),
          Routine("Rest", List.empty),
          Routine("Rest", List.empty),
          Routine("Push", List(
            "Incline Barbell Bench Press - 4 Sets (10, 10, 12, 15 Reps)",
            "Dumbbell Flye - 3 Sets (10, 10, 12, 15 Reps)",
            "Overhead Dumbbell Press - 3 Sets (10, 10, 12, 15 Reps)",
            "Smith Machine Upright Row - 3 Sets (8, 8, 10, 12 Reps)",
            "Lying Ez-Bar Triceps Extension - 3 Sets (10, 10, 12, 15 Reps)",
            "Dumbbell Kickback - 3 Sets (10, 10, 12, 15 Reps)"
          )),
          Routine("Pull", List(
            "Barbell Upright Row - 4 Sets (8, 8, 10, 12 Reps)",
            "Single-Arm Neutral-Grip Dumbbell Row - 4 Sets (8, 8, 10, 12 Reps)",
            "Incline Dumbbell Biceps Curl - 4 Sets (8, 8, 10, 12 Reps)",
            "Preacher Curl With Cable - 4 Sets (8, 8, 10, 12 Reps)",
            "Reverse Crunch - 3 Sets (15 - 20 Reps)",
            "Crunch - 3 Sets (15 - 20 Reps)"
          )),
          Routine("Legs", List(
            "Back Squat - 4 Sets (8, 8, 10, 12 Reps)",
            "Leg Press - 4 Sets (8, 8, 10, 12 Reps)",
            "Seated Legs Curl - 4 Sets (8, 8, 10, 12 Reps)",
            "Romanian Deadlift - 4 Sets (8, 8, 10, 12 Reps)",
            "Standing Calf Raise - 3 Sets ( 25 Reps)",
            "Seated Calf Raise - 3 Sets (25 Reps)"
          )),
          Routine("Push", List(
            "Incline Barbell Bench Press - 4 Sets (10, 10, 12, 15 Reps)",
            "Dumbbell Flye - 3 Sets (10, 10, 12, 15 Reps)",
            "Overhead Dumbbell Press - 3 Sets (10, 10, 12, 15 Reps)",
            "Smith Machine Upright Row - 3 Sets (8, 8, 10, 12 Reps)",
            "Lying Ez-Bar Triceps Extension - 3 Sets (10, 10, 12, 15 Reps)",
            "Dumbbell Kickback - 3 Sets (10, 10, 12, 15 Reps)"
          )),
          Routine("Pull", List(
            "Barbell Upright Row - 4 Sets (8, 8, 10, 12 Reps)",
            "Single-Arm Neutral-Grip Dumbbell Row - 4 Sets (8, 8, 10, 12 Reps)",
            "Incline Dumbbell Biceps Curl - 4 Sets (8, 8, 10, 12 Reps)",
            "Preacher Curl With Cable - 4 Sets (8, 8, 10, 12 Reps)",
            "Reverse Crunch - 3 Sets (15 - 20 Reps)",
            "Crunch - 3 Sets (15 - 20 Reps)"
          )),
          Routine("Legs", List(
            "Back Squat - 4 Sets (8, 8, 10, 12 Reps)",
            "Leg Press - 4 Sets (8, 8, 10, 12 Reps)",
            "Seated Legs Curl - 4 Sets (8, 8, 10, 12 Reps)",
            "Romanian Deadlift - 4 Sets (8, 8, 10, 12 Reps)",
            "Standing Calf Raise - 3 Sets ( 25 Reps)",
            "Seated Calf Raise - 3 Sets (25 Reps)"
          )),
          Routine("Rest", List.empty),
          Routine("Chest, Triceps, Calves ", List(
            "Incline Barbell Bench Press - 5 Sets (10 Reps)",
            "Dumbbell Bench Press - 5 Sets (8, 8, 10, 12 Reps)",
            "Dumbbell Flye - 5 Sets (8, 8, 10, 12 Reps)",
            "Rope Pressdown - 4 Sets (10, 10, 12, 12 Reps)",
            "Dumbbell Kickback - 3 Sets ( 10 Reps)",
            "Lying Ez-Bar Triceps Extension - 3 Sets ( 10 Reps)",
            "Standing Calf Raise - 3 Sets ( 10 Reps)",
            "Seated Calf Raise - 3 Sets ( 225 Reps)"
          )),
          Routine("Legs & Abs ", List(
            "Back Squat - 5 Sets (10 Reps)",
            "Leg Pressh - 5 Sets (8, 8, 10, 12 Reps)",
            "Lying Leg Curl - 3 Sets (8, 8, 10, 12 Reps)",
            "Romanian Deadlift - 3 Sets ( 8, 10, 12 Reps)",
            "Seated Legs Curl - 3 Sets ( 8, 10, 12 Reps)",
            "Reverse Crunch - 2 Sets ( 20 Reps)",
            "Crunch - 2 Sets ( 20 Reps)"
          )),
          Routine("Rest", List.empty),
          Routine("Shoulder & Calves ", List(
            "Overhead Dumbbell Press - 4 Sets (12 Reps)",
            "Smith Machine Upright Row - 3 Sets (8, 10, 12 Reps)",
            "Dumbbell Lateral Raise - 3 Sets (10 Reps)",
            "Seated Calf Raise - 10 Sets ( 10 Reps)"
          )),
          Routine("Back, Biceps & Abs ", List(
            "Barbell Bent-Over Row - 5 Sets (12 Reps)",
            "Lat Pulldown - 5 Sets (8, 10, 12 Reps)",
            "Single-Arm Neutral-Grip Dumbbell Row - 5 Sets (8, 10, 12 Reps)",
            "Barbell Biceps Curl - 4 Sets ( 10 Reps)",
            "Incline Dumbbell Biceps Curl - 3 Sets ( 10 Reps)",
            "Preacher Curl With Cable - 3 Sets ( 10 Reps)",
            "Crunch - 3 Sets ( 20 Reps)"
          )),
          Routine("Rest", List.empty),
          Routine("Rest", List.empty)
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
    new TrainingProfileHandler(zoomRW(identity)((_, v) => v)),
    new TrainingModulesHandler(zoomRW(_.trainingModules)((m, v) => m.copy(trainingModules = v)))
  )
}

import com.ironmangym.domain.SPACircuit.{ usersKey, trainingModulesKey }

class TrainingProfileHandler[M](modelRW: ModelRW[M, RootModel]) extends ActionHandler(modelRW) {
  def handle = {
    case EnrolTrainingProgram(trainee, trainingModule, goal, date) =>
      val updatedUsers = value.users.enrol(trainee, trainingModule, goal, date)
      dom.window.localStorage.setItem(usersKey, Pickle.intoString(updatedUsers))
      updated(RootModel(updatedUsers, value.trainingModules))
    case WorkoutDayChanged(trainee, updatedWorkoutDay) =>
      val updatedUsers = value.users.updateWorkoutDay(trainee, updatedWorkoutDay)
      dom.window.localStorage.setItem(usersKey, Pickle.intoString(updatedUsers))
      updated(RootModel(updatedUsers, value.trainingModules))
  }
}

class TrainingModulesHandler[M](modelRW: ModelRW[M, Seq[TrainingModule]]) extends ActionHandler(modelRW) {
  def handle = {
    case CreateTrainingModule(trainingModule) =>
      val newVal = value :+ trainingModule
      dom.window.localStorage.setItem(trainingModulesKey, Pickle.intoString(newVal))
      updated(newVal)
    case UpdateTrainingModules(trainingModules) =>
      dom.window.localStorage.setItem(trainingModulesKey, Pickle.intoString(trainingModules))
      updated(trainingModules)
    case DeleteTrainingModule(index) =>
      val newVal = value.take(index) ++ value.drop(index + 1)
      dom.window.localStorage.setItem(trainingModulesKey, Pickle.intoString(newVal))
      updated(newVal)
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
