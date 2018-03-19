package com.fitness.churva.domain

import java.time.LocalDate
import squants.space.Length
import squants.mass.Mass

case class Trainee(
    name:            String,
    phoneNumber:     String,
    credentials:     Credentials,
    trainingProgram: Option[TrainingProgram],
    fitnessInfo:     Seq[FitnessInfo]
)

case class FitnessInfo(
    date:              LocalDate,
    age:               Int,
    height:            Length,
    weight:            Mass,
    bodyFatPercentage: Double,
    bodyMassIndex:     Double
)

case class Credentials(
    username: String,
    password: String
)

case class Trainer(
    name:             String,
    trainingPrograms: Seq[TrainingProgram]
)

case class TrainingProgram(
    trainingSchedule: TrainingSchedule,
    trainer:          Trainer,
    trainee:          Trainee,
    progress:         Progress,
    goal:             Goal
)

case class Progress(
    bodyFatPercentage: Double,
    bodyMassIndex:     Double,
    weight:            Mass
)

case class Goal(
    bodyFatPercentage: Option[Double],
    bodyMassIndex:     Option[Double],
    weight:            Option[Mass]
)

case class TrainingSchedule(
    workouts: Seq[Workout]
)

case class Workout(
    date:        LocalDate,
    description: String,
    done:        Boolean
)

case class TrainingModule(exercises: List[String])

