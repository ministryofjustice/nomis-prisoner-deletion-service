plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "4.0.0"
  kotlin("plugin.spring") version "1.5.30"
  kotlin("plugin.jpa") version "1.5.30"
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
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:1.0.3")
  implementation("org.apache.commons:commons-lang3:3.12.0")
  implementation("org.apache.commons:commons-text:1.9")

  implementation("com.oracle.database.jdbc:ojdbc10:19.13.0.0.1")

  runtimeOnly("org.hsqldb:hsqldb:2.6.1")
  runtimeOnly("org.flywaydb:flyway-core:8.3.0")

  implementation("javax.transaction:javax.transaction-api:1.3")
  implementation("javax.xml.bind:jaxb-api:2.3.1")

  implementation("org.springdoc:springdoc-openapi-ui:1.6.3")
  implementation("org.springdoc:springdoc-openapi-kotlin:1.6.3")
  implementation("org.springdoc:springdoc-openapi-data-rest:1.6.3")

  developmentOnly("org.springframework.boot:spring-boot-devtools")

  testImplementation("org.awaitility:awaitility-kotlin:4.1.1")
  testImplementation("io.jsonwebtoken:jjwt:0.9.1")
  testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
  testImplementation("com.github.tomakehurst:wiremock-standalone:2.27.2")
  testImplementation("org.mockito:mockito-inline:4.2.0")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(16))
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "16"
    }
  }

  test {
    maxHeapSize = "256m"
  }
}
