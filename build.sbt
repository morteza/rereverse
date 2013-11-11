name := "rereverse"

version := "0.0.0"

autoScalaLibrary := false

crossPaths := false

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

mainClass in (Compile, run):= Some("rereverse.Bootstrap")

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

libraryDependencies += "io.netty" % "netty-all" % "4.0.12.Final"

libraryDependencies += "javax.persistence" % "persistence-api" % "1.0.2"

libraryDependencies += "org.avaje" % "ebean" % "2.8.1"

libraryDependencies += "com.h2database" % "h2" % "1.3.174"
