package com.ironmangym.domain

import scala.language.implicitConversions

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
    routines:   List[Routine] = List.empty,
    difficulty: Difficulty
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
)

case class RootModel(
    users:           Users,
    trainingModules: Seq[TrainingModule] = Seq.empty
)
