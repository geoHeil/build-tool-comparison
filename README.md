# comparison of different build tools for complex assemblies

desired goal: get a fat jar of:
- spark geomesa
- geotools
- JAI

currently in SBT are various assembly problems with duplicate stuff on the classpath. It might be related to not correctly parsing some exclusions in the maven pom files.
It would be great if you have some ideas how to fix the
## sbt

For the dependencies of:
```
val sparkBase = "org.apache.spark" %% "spark-core"  % sparkV % "provided"
val sparkSql  = "org.apache.spark" %% "spark-sql"   % sparkV % "provided"
val geomesaSpark = "org.locationtech.geomesa" %% "geomesa-spark-sql" % geomesaV
```

without any fancy settings the error is that:
```
com/vividsolutions/jts/1.12/jts-1.12.jar
com/vividsolutions/jts-core/1.14.0/jts-core-1.14.0.jar
```
Put around 400 basic geometry classes twice but in different versions to the classpath.

> root cause geomesa is using version 1.14 of JTS related libraries, but transitive dependency is 1.12 https://github.com/jdeolive/jai-tools/blob/master/utils/pom.xml introduced via geotools 1.18 via jt-utils 1.4.0

enabling exclusions:
```
excludeDependencies ++= Seq("com.vividsolutions" % "jts")
``

results in less problems - hoping that the more current library will not break stuff in the transitive dependency.

problems with the various registry files remain, and a couple of other issues as well (execute `sbt assembly` to see for yourself) 
```
registryFile.jai

```
With other projects I has massive problems getting the right concatenation to work and secondly having the spark class loader load the registry files (did not work) https://stackoverflow.com/questions/44341018/jai-cant-execute-in-native-spark-only-in-sbt-and-as-a-separate-scala-function and https://stackoverflow.com/questions/43910006/geotools-jai-fatjar-causing-problems-in-native-dependencies

Now instead trying https://github.com/locationtech/geomesa/blob/master/geomesa-hbase/geomesa-hbase-spark-runtime/pom.xml I am sceptical as most JAI stuff is excluded - i.e. some of the geotools processes might crash at runtime. Also, HBase is included which I would prefer to be excluded (should be fixable easily in my build tool).

However this also fails with many errors.

## gradle
using gradle shadowJar plugin a `gradle shadowJar` works without any complaints. META_INF files are merged by default.
I still need to test if the outputted JAR actually works - but the process of getting there seems to be by far less painful than SBT

## maven
same goes for maven
