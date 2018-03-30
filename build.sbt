import scalariform.formatter.preferences._

name := "booking"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.4"

scalariformPreferences := scalariformPreferences.value
  .setPreference(AlignArguments, true)
  .setPreference(AlignParameters, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AllowParamGroupsOnNewlines, true)
  .setPreference(DanglingCloseParenthesis, Force)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DoubleIndentMethodDeclaration, true)
  .setPreference(IndentLocalDefs, true)
  .setPreference(NewlineAtEndOfFile, true)

skip in packageJSDependencies := false
webpackBundlingMode := BundlingMode.LibraryOnly()
useYarn := true
emitSourceMaps := false
scalaJSUseMainModuleInitializer := true
scalaJSUseMainModuleInitializer in Test := false
scalacOptions += "-Ypartial-unification"

libraryDependencies ++=
  "com.pangwarta"           %%% "scalajs-react-material-ui" % "0.1.0-SNAPSHOT" ::
  "io.github.cquiroz"       %%% "scala-java-time"           % "2.0.0-M13"      ::
  "io.suzaku"               %%% "diode"                     % "1.1.3"          ::
  "io.suzaku"               %%% "diode-react"               % "1.1.3"          ::
  "com.github.benhutchison" %%% "prickle"                   % "1.1.13"         ::
  "com.olvind"              %%% "scalajs-react-components"  % "1.0.0-M2"       ::
  Nil

npmDependencies in Compile ++=
  "react-big-calendar" -> "latest" ::
  "moment"             -> "latest" ::
  Nil

enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)