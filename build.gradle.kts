import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("org.sonarqube") version "3.5.0.2730"
    jacoco
    application
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.8"
    reportsDirectory.set(layout.projectDirectory.dir("build/jacocoReport"))
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "tmp-production_ldap-users-service-prototype")
        property("sonar.organization", "tmp-production")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.scm.disabled", "true")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "build/jacocoReport/test/jacocoTestReport.xml"
        )
        property(
            "sonar.coverage.exclusions",
            "src/main/kotlin/com/tmpproduction/ldapservice/apps/*," +
                    "src/main/kotlin/com/tmpproduction/ldapservice/impl/RestApiProviderService.kt," +
                    "src/main/kotlin/com/tmpproduction/ldapservice/impl/KafkaProviderService.kt"
        )
    }
}

group = "com.tmp-production.ldapservice"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.3")
    implementation("org.slf4j:slf4j-simple:2.0.3")

    implementation("io.ktor:ktor-server-core:2.1.3")
    implementation("io.ktor:ktor-server-netty:2.1.3")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("io.ktor:ktor-server-content-negotiation:2.1.3")

    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.3")

    implementation("io.ktor:ktor-client-core:2.1.3")
    implementation("io.ktor:ktor-client-cio:2.1.3")
    implementation("io.ktor:ktor-client-logging:2.1.3")

    implementation("org.apache.kafka:kafka-streams:3.3.1")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("io.ktor:ktor-client-mock:2.1.3")
    testImplementation("io.ktor:ktor-server-test-host:2.1.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.7.20")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("com.tmpproduction.ldapservice.apps.EntryPointKt")
}
