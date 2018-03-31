package com.ironmangym.domain

import scala.language.implicitConversions
import scala.scalajs.js
import scala.util.Random

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

  def enrol(trainingModule: TrainingModule, trainer: Trainer, goal: Goal): Trainee = copy(trainingProgram =
    Some(TrainingProgram(trainer, this, trainingModule.name, generateModule(trainingModule), goal)))

  def generateModule(trainingModule: TrainingModule): Seq[WorkoutDay] = ???

  def latestWeight: Option[Double] =
    trainingProgram.map(_.workoutDays.last.weight)

  def latestBMI: Option[Double] =
    trainingProgram.map(_.workoutDays.last.bodyMassIndex)

  def latestBFP: Option[Double] =
    trainingProgram.map(_.workoutDays.last.bodyFatPercentage)
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
    name:            String,
    credentials:     Credentials,
    trainingProgram: Seq[TrainingProgram] = Seq.empty
) extends User

case class TrainingProgram(
    trainer:     Trainer,
    trainee:     Trainee,
    name:        String,
    workoutDays: Seq[WorkoutDay],
    goal:        Goal
)

case class Progress(
    bodyFatPercentage: Double,
    bodyMassIndex:     Double,
    weight:            Double
)

case class Goal(
    bodyFatPercentage: Option[Double] = None,
    bodyMassIndex:     Option[Double] = None,
    weight:            Option[Double] = None
)

case class WorkoutDay(
    date:              Date,
    name:              String,
    description:       String,
    done:              Boolean,
    bodyFatPercentage: Double,
    bodyMassIndex:     Double,
    weight:            Double
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
  def enrol(trainee: Trainee, trainingModule: TrainingModule, goal: Goal): Users = {
    val idx = trainees.indexOf(trainee)
    val randomTrainer = trainers(Random.nextInt(trainers.size))
    copy(trainees = trainees.updated(idx, trainee.enrol(trainingModule, randomTrainer, goal)))
  }

}

case class RootModel(
    users:           Users,
    trainingModules: Seq[TrainingModule] = Seq.empty
)
