val awsSdkVersion = "1.11.603"

lazy val root = project
  .in(file("."))
  .settings(
    name := "eventbridge-funland",
    description := "Example EventBridge pingpong loop that compiles using Dotty",
    version := "0.1.0",

    scalaVersion := "0.17.0-RC1",

    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-java-sdk-eventbridge" % awsSdkVersion,
      "com.amazonaws" % "aws-java-sdk-sqs" % awsSdkVersion
    )
  )
