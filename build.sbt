// import NativePackagerKeys._

organization := "ir.onto"

name := "rereverse"

version := "0.0.1"

mainClass in (Compile, run):= Some("rereverse.Bootstrap")

autoScalaLibrary := false

crossPaths := false

//================================ SBT Modules =================================
// Typesafe Console 
// addSbtPlugin("com.typesafe.sbt" % "sbt-atmos" % "0.3.+")

// atmosSettings

// Native Packager
// addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.+")

// packageArchetype.java_application

//Eclipse Project
// addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.+")

// .EclipseKeys.projectFlavor := EclipseProjectFlavor.Java
//==============================================================================

// Dependencies
libraryDependencies += "io.netty" % "netty-all" % "4.0.12"

libraryDependencies += "javax.persistence" % "persistence-api" % "1.0.2"

libraryDependencies += "org.avaje" % "ebean" % "2.8.1"

libraryDependencies += "com.h2database" % "h2" % "1.3.174"
