package com.ironmangym.domain

import com.ironmangym.common
import com.ironmangym.common._

import scala.collection.immutable.{ NumericRange, Range }
import scala.language.implicitConversions
import scala.scalajs.js
import scala.util.Random

sealed trait User {
  def credentials: Credentials
  def name: String
  def isTrainer: Boolean
  def isTrainee: Boolean
}

case class Trainee(
    name:            String,
    birthday:        Date,
    heightInCm:      Double,
    phoneNumber:     Option[String],
    credentials:     Credentials,
    trainingProgram: Option[TrainingProgram] = None
) extends User {

  def updateWorkoutDay(updatedWorkoutDay: WorkoutDay): Trainee = {
    val tp = trainingProgram.get
    val i = tp.workoutDays.indexWhere(_.date == updatedWorkoutDay.date)
    copy(trainingProgram = Some(tp.copy(workoutDays = tp.workoutDays.updated(i, updatedWorkoutDay))))
  }

  def enrol(trainingModule: TrainingModule, trainer: Trainer, goal: FitnessStats, startDate: js.Date): Trainee =
    copy(trainingProgram = Some(
      TrainingProgram(
        trainer,
        trainingModule.name,
        generateWorkoutDays(trainingModule, startDate),
        startDate,
        daysAfter(startDate, trainingModule.routines.size),
        goal
      )
    ))

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

  def latestBMIAssessment: Option[Assessment] = for {
    tp <- trainingProgram
    ld <- tp.workoutDays.filter(_.stats.assessment.isDefined).lastOption
    ass <- ld.stats.assessment
  } yield ass

  def latestBFP: Option[Double] = for {
    tp <- trainingProgram
    ld <- tp.workoutDays.filter(_.stats.bodyFatPercentage.isDefined).lastOption
    bfp <- ld.stats.bodyFatPercentage
  } yield bfp

  def hasTrainingProgramWith(trainerUsername: String): Boolean =
    trainingProgram.exists(_.trainer.credentials.username == trainerUsername)

  val age = common.age(birthday)

  override def isTrainer: Boolean = false

  override def isTrainee: Boolean = true
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
) extends User {
  override def isTrainer: Boolean = true

  override def isTrainee: Boolean = false
}

case class TrainingProgram(
    trainer:     Trainer,
    name:        String,
    workoutDays: Seq[WorkoutDay],
    startDate:   Date,
    endDate:     Date,
    goal:        FitnessStats
) {

  def progress: Progress =
    Progress(workoutDays.count(_.done), workoutDays.length)
}

case class Progress(done: Int, all: Int)

case class FitnessStats(
    bodyFatPercentage: Option[Double] = None,
    bodyMassIndex:     Option[Double] = None,
    weight:            Option[Double] = None
) {
  def assessment: Option[Assessment] = bodyMassIndex.map {
    case bmi if 0.00 <= bmi && bmi < 14.0            => SeverelyUnderweight
    case bmi if 14.0 <= bmi && bmi < 18.5            => Underweight
    case bmi if 18.5 <= bmi && bmi < 25.0            => Normal
    case bmi if 25.0 <= bmi && bmi < 30.0            => Overweight
    case bmi if 30.0 <= bmi && bmi < Double.MaxValue => Obese
  }
}

sealed trait Assessment
case object SeverelyUnderweight extends Assessment
case object Underweight extends Assessment
case object Normal extends Assessment
case object Overweight extends Assessment
case object Obese extends Assessment

case class WorkoutDay(
    date:      Date         = new js.Date,
    name:      String       = "",
    exercises: Seq[String]  = List.empty,
    done:      Boolean      = false,
    stats:     FitnessStats = FitnessStats()
)

sealed trait Difficulty
case object Beginner extends Difficulty
case object Intermediate extends Difficulty
case object Advanced extends Difficulty

case class TrainingModule(
    name:       String       = "",
    difficulty: Difficulty   = Beginner,
    routines:   Seq[Routine] = Seq.empty
)

case class Routine(
    name:      String,
    exercises: Seq[String] = List.empty
)

case class PersistentUser(
    credentials: Credentials,
    name:        String
)

object PersistentUser {
  implicit def fromUser(user: User): PersistentUser = PersistentUser(user.credentials, user.name)
  def toUser(users: Users, persistentUser: PersistentUser): User = {
    val trainer = users.findTrainer(persistentUser.credentials.username)
    val trainee = users.findTrainee(persistentUser.credentials.username)
    if (trainer.isDefined) trainer.get else trainee.get
  }
}

case class Users(
    trainers:    Seq[Trainer]           = Seq.empty,
    trainees:    Seq[Trainee]           = Seq.empty,
    currentUser: Option[PersistentUser] = None
) {
  def currentUserIsTrainer: Boolean =
    (for {
      cu <- currentUser
      tr <- findTrainer(cu.credentials.username)
    } yield tr).isDefined

  def trainerWithCredentials(formCredentials: Credentials): Option[Trainer] =
    trainers.find(_.credentials == formCredentials)

  def traineeWithCredentials(formCredentials: Credentials): Option[Trainee] =
    trainees.find(_.credentials == formCredentials)

  def enrol(trainee: Trainee, trainingModule: TrainingModule, goal: FitnessStats, startDate: js.Date): Users = {
    val randomTrainer = trainers(Random.nextInt(trainers.size))
    val i = trainees.indexOf(trainee)
    val j = trainers.indexOf(randomTrainer)
    val enrolledTrainee = trainee.enrol(trainingModule, randomTrainer, goal, startDate)
    copy(trainees = trainees.updated(i, enrolledTrainee))
  }

  def updateWorkoutDay(trainee: Trainee, updatedWorkoutDay: WorkoutDay): Users = {
    val i = trainees.indexOf(trainee)
    val updatedTrainee = trainee.updateWorkoutDay(updatedWorkoutDay)
    copy(trainees = trainees.updated(i, updatedTrainee))
  }

  def findTrainer(username: String): Option[Trainer] =
    trainers.find(_.credentials.username == username)

  def findTrainee(username: String): Option[Trainee] =
    trainees.find(_.credentials.username == username)
}

case class RootModel(
    users:           Users,
    trainingModules: Seq[TrainingModule] = Seq.empty
)
