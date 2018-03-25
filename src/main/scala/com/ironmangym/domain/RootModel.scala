package com.ironmangym.domain

sealed trait User

case class Trainee(
    name:            String,
    birthday:        Date,
    height:          Double,
    phoneNumber:     Option[String],
    credentials:     Credentials,
    trainingProgram: Option[TrainingProgram] = None
) extends User

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
    name:        String,
    credentials: Credentials
) extends User

case class TrainingProgram(
    trainer:     String,
    workoutDays: Seq[WorkoutDay],
    goal:        Goal
)

case class Progress(
    bodyFatPercentage: Double,
    bodyMassIndex:     Double,
    weight:            Double
)

case class Goal(
    bodyFatPercentage: Option[Double],
    bodyMassIndex:     Option[Double],
    weight:            Option[Double]
)

case class WorkoutDay(
    date:              Date,
    description:       String,
    done:              Boolean,
    bodyFatPercentage: Double,
    bodyMassIndex:     Double,
    weight:            Double
)

case class TrainingModule(
    name:      String,
    exercises: List[String]
)

case class Users(
    trainers:    Seq[Trainer],
    trainees:    Seq[Trainee],
    currentUser: Option[User]
)

case class RootModel(
    users:           Users,
    trainingModules: Seq[TrainingModule]
)
