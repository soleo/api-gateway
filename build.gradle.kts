import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
	java
	id("org.springframework.boot") version "2.7.5"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	`maven-publish`
}
tasks.named<BootJar>("bootJar") {
	mainClass.set("com.xinjiangshao.apigateway.ApiGatewayApplication")
}

tasks.named<BootBuildImage>("bootBuildImage") {
	docker {
		builderRegistry {
			token = System.getenv("GITHUB_TOKEN")
		}
	}
}


tasks.register("bootBuildImageDocker") {
	doFirst {
		println("token=${tasks.getByName<BootBuildImage>("bootBuildImage").docker.builderRegistry.token}")
	}
}
group = "com.xinjiangshao"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2021.0.5"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.cloud:spring-cloud-starter-gateway")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

publishing {
	publications {
		create<MavenPublication>("bootJava") {
			artifact(tasks.named("bootJar"))
		}
	}
	repositories {
		maven {
		  name = "GitHubPackages"
		  url = uri("https://maven.pkg.github.com/soleo/api-gateway")
		  credentials {
			username = System.getenv("GITHUB_ACTOR")
			password = System.getenv("GITHUB_TOKEN")
		  }
		}
	}
}

tasks.register("publishingConfiguration") {
	doLast {
		println(publishing.publications["bootJava"])
		println(publishing.repositories.getByName<MavenArtifactRepository>("maven").url)
	}
}