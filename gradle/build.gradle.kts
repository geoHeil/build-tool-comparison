plugins {
    // Apply the scala plugin to add support for Scala
    scala
    id("com.github.johnrengelman.shadow").version("2.0.4")

}

dependencies {
    // Use Scala 2.11 in our library project
    compile("org.scala-lang:scala-library:2.11.12")

    compile("org.locationtech.geomesa:geomesa-spark-sql_2.11:2.0.1")
    compile("org.locationtech.geomesa:geomesa-fs-datastore_2.11:2.0.1")
    compile("org.locationtech.geomesa:geomesa-fs-storage-orc_2.11:2.0.1")

    //compile(zinc "com.typesafe.zinc:zinc:0.3.9")

}

// In this section you declare where to find the dependencies of your project
repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    maven(url = "https://repo.locationtech.org/content/groups/releases")
    maven(url = "https://repo.boundlessgeo.com/main")
    maven(url = "http://download.osgeo.org/webdav/geotools")
    maven(url = "http://conjars.org/repo")
    maven(url = "http://nexus-private.hortonworks.com/nexus/content/groups/public")
    jcenter()
}

