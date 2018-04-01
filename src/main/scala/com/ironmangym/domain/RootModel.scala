package com.ironmangym.domain

import scala.language.implicitConversions
import scala.scalajs.js
import scala.util.Random

import com.ironmangym.common._

sealed trait User {
  def credentials: Credentials
  def name: String
}

case class Trainee(
    name:            String,
    birthday:        Date,
    heightInCm:      Double,
    phoneNumber:     Option[String],
    credentials:     Credentials,
    trainingProgram: Option[TrainingProgram] = None
) extends User {

  def enrol(trainingModule: TrainingModule, trainer: Trainer, goal: Stats, startDate: js.Date): Trainee =
    copy(
      trainingProgram =
        Some(
          TrainingProgram(
            trainer,
            trainingModule.name,
            generateWorkoutDays(trainingModule, startDate),
            startDate,
            daysAfter(startDate, trainingModule.routines.size),
            goal
          )
        )
    )

  def generateWorkoutDays(trainingModule: TrainingModule, startDate: js.Date): Seq[WorkoutDay] = {
    val numDays = trainingModule.routines.length
    val dates = (0 until numDays).map(n => daysAfter(startDate, n))
    trainingModule.routines.zip(dates).map {
      case (routine, date) =>
        WorkoutDay(date, routine.name, routine.exercises)
    }
  }

  def latestWeight: Option[Double] = for {
    tp <- trainingProgram
    ld <- tp.workoutDays.filter(_.stats.weight.isDefined).lastOption
    wt <- ld.stats.weight
  } yield wt

  def latestBMI: Option[Double] = for {
    tp <- trainingProgram
    ld <- tp.workoutDays.filter(_.stats.bodyMassIndex.isDefined).lastOption
    bmi <- ld.stats.bodyMassIndex
  } yield bmi

  def latestBFP: Option[Double] = for {
    tp <- trainingProgram
    ld <- tp.workoutDays.filter(_.stats.bodyFatPercentage.isDefined).lastOption
    bfp <- ld.stats.bodyFatPercentage
  } yield bfp
}

case class Date(
    year:  Int,
    month: Int,
    date:  Int
)

object Date {
  implicit def toJsDate(date: Date): js.Date =
    new js.Date(date.year, date.month, date.date)
}

case class Credentials(
    username: String,
    password: String
)

case class Trainer(
    name:        String,
    credentials: Credentials
) extends User

case class TrainingProgram(
    trainer:     Trainer,
    name:        String,
    workoutDays: Seq[WorkoutDay],
    startDate:   Date,
    endDate:     Date,
    goal:        Stats
)

case class Progress(
    bodyFatPercentage: Double,
    bodyMassIndex:     Double,
    weight:            Double
)

case class Stats(
    bodyFatPercentage: Option[Double] = None,
    bodyMassIndex:     Option[Double] = None,
    weight:            Option[Double] = None
)

case class WorkoutDay(
    date:      Date         = new js.Date,
    name:      String       = "",
    exercises: List[String] = List.empty,
    done:      Boolean      = false,
    stats:     Stats        = Stats()
)

sealed trait Difficulty
case object Beginner extends Difficulty
case object Intermediate extends Difficulty
case object Advanced extends Difficulty

case class TrainingModule(
    name:       String,
    difficulty: Difficulty,
    routines:   List[Routine] = List.empty
)

case class Routine(
    name:      String,
    exercises: List[String]
)

case class PersistentUser(
    credentials: Credentials,
    name:        String
)

object PersistentUser {
  implicit def fromUser(user: User): PersistentUser = PersistentUser(user.credentials, user.name)
}

case class Users(
    trainers:    Seq[Trainer]           = Seq.empty,
    trainees:    Seq[Trainee]           = Seq.empty,
    currentUser: Option[PersistentUser] = None
) {
  def enrol(trainee: Trainee, trainingModule: TrainingModule, goal: Stats, startDate: js.Date): Users = {
    val randomTrainer = trainers(Random.nextInt(trainers.size))
    val i = trainees.indexOf(trainee)
    val j = trainers.indexOf(randomTrainer)
    val enrolledTrainee = trainee.enrol(trainingModule, randomTrainer, goal, startDate)
    copy(trainees = trainees.updated(i, enrolledTrainee))
  }

}

case class RootModel(
    users:           Users,
    trainingModules: Seq[TrainingModule] = Seq.empty
)
