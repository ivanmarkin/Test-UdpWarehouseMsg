plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.ivm.lab.warehouse"
version = "1.0.0"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
application.mainClass.set("net.ivm.lab.warehouse.central.CentralServiceApp")

repositories.mavenCentral()

dependencies {
    implementation("org.apache.commons:commons-lang3:3.20.0")
    implementation("io.nats:jnats:2.16.14")
    implementation("ch.qos.logback:logback-classic:1.5.25")
//    implementation("com.typesafe.akka:akka-slf4j_2.13:2.8.5")
//    implementation("org.slf4j:slf4j-api:2.0.13")

    implementation("com.typesafe.akka:akka-actor_2.13:2.8.5")
    implementation("com.typesafe.akka:akka-stream_2.13:2.8.5")
//    implementation("com.typesafe.akka:akka-actor-typed_2.13:2.8.5")
//    implementation("com.typesafe.akka:akka-stream-typed_2.13:2.8.5")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.mockito:mockito-core:5.7.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    manifest.attributes("Main-Class" to "net.ivm.lab.warehouse.central.CentralServiceApp")
    mergeServiceFiles()
    append("reference.conf")
}
