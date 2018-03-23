package com.ironmangym.domain

import java.time.LocalDate

import squants.space.Length
import squants.mass.{ AreaDensity, Mass }

sealed trait User

case class Trainee(
    name:            String,
    birthday:        LocalDate,
    height:          Length,
    phoneNumber:     String,
    credentials:     Credentials,
    trainingProgram: Option[TrainingProgram]
) extends User

case class Credentials(
    username: String,
    password: String
)

case class Trainer(
    name:             String,
    credentials:      Credentials,
    trainingPrograms: Map[Trainee, TrainingProgram]
) extends User

case class TrainingProgram(
    trainer:          Trainer,
    trainee:          Trainee,
    trainingSchedule: TrainingSchedule,
    goal:             Goal
)

case class Progress(
    bodyFatPercentage: Double,
    bodyMassIndex:     AreaDensity,
    weight:            Mass
)

case class Goal(
    bodyFatPercentage: Option[Double],
    bodyMassIndex:     Option[AreaDensity],
    weight:            Option[Mass]
)

case class TrainingSchedule(
    workoutDays: Seq[WorkoutDay]
)

case class WorkoutDay(
    date:              LocalDate,
    description:       String,
    done:              Boolean,
    bodyFatPercentage: Double,
    bodyMassIndex:     AreaDensity,
    weight:            Mass
)

case class TrainingModule(
    name:      String,
    exercises: List[String]
)

case class Users(
    trainer:     Seq[Trainer],
    trainee:     Seq[Trainee],
    currentUser: Option[User]
)

case class RootModel(
    users:           Users,
    trainingModules: Seq[TrainingModule]
)
