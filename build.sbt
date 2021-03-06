import com.typesafe.sbt.packager.archetypes.JavaAppPackaging

val ScalatraVersion     = "2.7.0"
val Json4sVersion       = "3.5.2"
val JettyVersion        = "9.2.19.v20160908"
val LogbackVersion      = "1.2.3"
val ServletApiVersion   = "3.1.0"
val DoobieVersion       = "0.9.0"
val CatsVersion         = "2.0.0"

lazy val root = project.in(file("."))

lazy val db = Map(
  "url"       -> sys.env.getOrElse("DB_URL_JDBC", "postgresql://localhost:5432/coffee_shop"),
  "user"      -> sys.env.getOrElse("DATABASE_USER", "postgres"),
  "password"  -> sys.env.getOrElse("DATABASE_PASSWORD", "123")
)

organization := "com.thiago_dev"

name := "Coffee Shop"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.12"

maintainer := "tfarias@protonmail.com"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra"            %% "scalatra"             % ScalatraVersion,
  "org.scalatra"            %% "scalatra-scalatest"   % ScalatraVersion     % "test",
  "ch.qos.logback"          %  "logback-classic"      % LogbackVersion      % "runtime",
  "org.eclipse.jetty"       %  "jetty-webapp"         % JettyVersion        % "compile;container",
  "org.eclipse.jetty"       %  "jetty-plus"           % JettyVersion        % "compile;container",
  "javax.servlet"           %  "javax.servlet-api"    % ServletApiVersion   % "provided",
  "org.scalatra"            %% "scalatra-json"        % ScalatraVersion,
  "org.json4s"              %% "json4s-jackson"       % Json4sVersion,
  "org.typelevel"           %% "cats-core"            % CatsVersion,
  "org.tpolecat"            %% "doobie-core"          % DoobieVersion,
  "org.tpolecat"            %% "doobie-postgres"      % DoobieVersion,
  "org.typelevel"           %% "cats-effect"          % "2.2.0",
  "org.postgresql"          %  "postgresql"           % "9.4-1206-jdbc42",
)

scalacOptions += "-Ypartial-unification"

enablePlugins(JavaAppPackaging)
enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
enablePlugins(FlywayPlugin)

flywayDriver            := "org.postgresql.Driver"
flywayUrl               := s"jdbc:${db("url")}"
flywayUser              := db("user")
flywayPassword          := db("password")
flywayBaselineOnMigrate := true

