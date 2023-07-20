plugins {
	java
	id("org.springframework.boot") version "3.0.6"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "gr.crud"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

extra["testcontainersVersion"] = "1.18.1"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-validation:3.0.4")
	implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.4")
	implementation("com.h2database:h2:2.1.214")
	implementation("org.springframework.boot:spring-boot-starter-actuator:3.1.0")
	compileOnly("org.projectlombok:lombok:1.18.26")
	developmentOnly("org.springframework.boot:spring-boot-devtools:3.0.4")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
	implementation ("org.springframework.data:spring-data-redis:3.0.6")
	implementation ("io.lettuce:lettuce-core")
	implementation("org.apache.commons:commons-pool2")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
	testImplementation("io.rest-assured:rest-assured-all:5.3.0")
	testImplementation("io.rest-assured:rest-assured:5.3.0")
	testImplementation("io.rest-assured:json-path:5.3.0")
	testImplementation("io.rest-assured:xml-path:5.3.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}