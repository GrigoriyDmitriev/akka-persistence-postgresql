import sbt.Keys._
import sbt._
import org.scalafmt.sbt.ScalafmtPlugin.autoImport._

object BuildSettings {

  def commonSettings = Seq(
    parallelExecution in Test := false,
    concurrentRestrictions in Global += Tags.limit(Tags.Test, 1),
    resolvers ++= Seq(
      "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
      Resolver.typesafeRepo("releases")
    ),
    updateOptions := updateOptions.value.withCachedResolution(true),
    organization := "be.wegenenverkeer",
    scalafmtOnCompile := true
  )

  val publishingCredentials = Option(System.getenv("GITHUB_TOKEN"))
    .map { token =>
      Credentials(
        "GitHub Package Registry",
        "maven.pkg.github.com",
        "GrigoriyDmitriev",
        token
      )
    }

  val publishSettings = Seq(
    publishMavenStyle := true,
    pomIncludeRepository := { _ =>
      false
    },
    publishTo := (for {
      owner <- Option("GrigoriyDmitriev")
      repo <- Option("akka-persistence-postgresql")
    } yield "GitHub Package Registry" at s"https://maven.pkg.github.com/$owner/$repo"),
    pomExtra := <url>https://github.com/WegenenVerkeer/akka-persistence-postgresql</url>
      <licenses>
        <license>
          <name>MIT licencse</name>
          <url>http://opensource.org/licenses/MIT</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <developers>
        <developer>
          <id>AWV</id>
          <name>De ontwikkelaars van AWV</name>
          <url>http://www.wegenenverkeer.be</url>
        </developer>
      </developers>,
    credentials ++= publishingCredentials
  )

}
