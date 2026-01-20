plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.ivm.lab.warehouse"
version = "1.0.0"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
application.mainClass.set("net.ivm.lab.warehouse.WarehouseServiceApp")

repositories.mavenCentral()

dependencies {
    implementation("io.nats:jnats:2.16.14")
    implementation("ch.qos.logback:logback-classic:1.5.25")
//    implementation("com.fasterxml.jackson.core:jackson-databind:2.21.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.7.0")
    testImplementation("org.awaitility:awaitility:4.2.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
//    archiveBaseName.set("warehouse-service")
    manifest.attributes("Main-Class" to "net.ivm.lab.warehouse.WarehouseServiceApp")
}
