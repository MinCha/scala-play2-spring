name := """scala-play2-spring"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

val springVersion = "4.1.3.RELEASE"
val hibernateVersion = "4.3.8.Final"

libraryDependencies ++= Seq(
  ws,
  "org.springframework.data" % "spring-data-jpa" % "1.7.2.RELEASE",
  "org.springframework" % "spring-context" % springVersion,
  "org.springframework" % "spring-context-support" % springVersion,
  "org.springframework" % "spring-test" % springVersion,
  "org.springframework" % "spring-aop" % springVersion,
  "org.springframework" % "spring-orm" % springVersion,
  "org.springframework" % "spring-jdbc" % springVersion,
  "org.springframework" % "spring-tx" % springVersion,
  "commons-configuration" % "commons-configuration" % "1.10",
  "com.h2database" % "h2" % "1.4.181",
  "org.hibernate" % "hibernate-core" % hibernateVersion,
  "org.hibernate" % "hibernate-entitymanager" % hibernateVersion,
  "commons-dbcp" % "commons-dbcp" % "1.4",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.4.2",
  "junit" % "junit" % "4.11",
  "antlr" % "antlr" % " 2.7.7",
  "net.sf.ehcache" % "ehcache" % "2.9.1",
  "com.wordnik" % "swagger-play2_2.11" % "1.3.10",
  "commons-io" % "commons-io" % "2.4",
  "org.scalamock" % "scalamock-scalatest-support_2.11" % "3.2",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "org.mockito" % "mockito-all" % "1.10.8" % "test"
)
