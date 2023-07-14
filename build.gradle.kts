plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "4.5.7"
  kotlin("plugin.spring") version "1.7.21"
  kotlin("plugin.jpa") version "1.7.21"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:1.1.13")
  implementation("org.apache.commons:commons-lang3:3.12.0")
  implementation("org.apache.commons:commons-text:1.10.0")

  implementation("com.oracle.database.jdbc:ojdbc10:19.16.0.0")

  runtimeOnly("org.hsqldb:hsqldb:2.7.0")
  runtimeOnly("org.flywaydb:flyway-core")

  implementation("javax.transaction:javax.transaction-api:1.3")
  implementation("javax.xml.bind:jaxb-api:2.3.1")

  implementation("org.springdoc:springdoc-openapi-ui:1.6.12")
  implementation("org.springdoc:springdoc-openapi-kotlin:1.6.12")
  implementation("org.springdoc:springdoc-openapi-data-rest:1.6.12")

  developmentOnly("org.springframework.boot:spring-boot-devtools:2.7.5")

  testImplementation("org.testcontainers:localstack:1.17.5")
  testImplementation("org.awaitility:awaitility-kotlin:4.2.0")
  testImplementation("io.jsonwebtoken:jjwt:0.9.1")
  testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
  testImplementation("com.github.tomakehurst:wiremock-standalone:2.27.2")
  testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
  testImplementation("org.mockito:mockito-inline:4.8.1")
  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:2.36.0")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(18))
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "18"
    }
  }

  test {
    maxHeapSize = "256m"
  }
}
