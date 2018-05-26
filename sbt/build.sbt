name := "comparison"
organization in ThisBuild := "com.github.geoheil"
scalaVersion in ThisBuild := "2.11.12"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled")
scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-Xfuture",
  "-Xlint:missing-interpolator",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-dead-code",
  "-Ywarn-unused"
)
parallelExecution in Test := false
resolvers ++= Seq(
  "hortonworks public" at "http://nexus-private.hortonworks.com/nexus/content/groups/public",
  "locationtech-releases" at "https://repo.locationtech.org/content/groups/releases",
  "boundlessgeo" at "https://repo.boundlessgeo.com/main",
  "osgeo" at "http://download.osgeo.org/webdav/geotools",
  "conjars.org" at "http://conjars.org/repo"
)
fork := true

lazy val mydd =
  new {
    val sparkOpenV = "2.2"
    val hdpV       = "2.6.4.0-91"
    val sparkV     = s"$sparkOpenV.0.$hdpV"

    val geomesaV = "2.0.1"

    val sparkBase = "org.apache.spark" %% "spark-core"  % sparkV % "provided"
    val sparkSql  = "org.apache.spark" %% "spark-sql"   % sparkV % "provided"

    val geomesaSpark = "org.locationtech.geomesa" %% "geomesa-spark-sql" % geomesaV
//    val geomesaFsds    = "org.locationtech.geomesa" %% "geomesa-fs-datastore"   % geomesaV
//    val geomesaFsdsOrc = "org.locationtech.geomesa" %% "geomesa-fs-storage-orc" % geomesaV

  }

lazy val commonDependencies = Seq(
  mydd.sparkBase,
  mydd.sparkSql,
)
lazy val geomesaDependencies = Seq(
  mydd.geomesaSpark
)


libraryDependencies ++= commonDependencies ++ geomesaDependencies // ++ Seq("com.vividsolutions" % "jts" % "1.14") // 1.14 ist not found / not downloadable directly.

// geomesa is using 1.14, but transitive dependency is 1.12 https://github.com/jdeolive/jai-tools/blob/master/utils/pom.xml
excludeDependencies ++= Seq("com.vividsolutions" % "jts")

assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case PathList("META-INF", "LICENSE")     => MergeStrategy.discard
    case PathList("META-INF", "NOTICE.txt")  => MergeStrategy.discard
    case PathList("META-INF", "NOTICE")      => MergeStrategy.discard
    case PathList("META-INF", "LICENSE.txt") => MergeStrategy.discard
    case PathList("rootdoc.txt")             => MergeStrategy.discard
    case PathList("git.properties")          => MergeStrategy.discard
    case _                                   => MergeStrategy.deduplicate
  }
assemblyShadeRules in assembly := Seq(ShadeRule.rename("shapeless.**" -> "new_shapeless.@1").inAll)
test in assembly := {}
