/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "0.10.0"
    id("org.nosphere.apache.rat") version "0.3.0"
    id("org.nosphere.honker") version "0.3.0"
}

group = "org.codeartisans"
version = "0.4.0-SNAPSHOT"

pluginBundle {
    website = "https://github.com/eskatos/creadur-rat-gradle"
    vcsUrl = "https://github.com/eskatos/creadur-rat-gradle"
    description = "Apache RAT (Release Audit Tool) Gradle Plugin"
    tags = listOf("apache", "release-audit", "license")
}

repositories {
    gradlePluginPortal()
}

dependencies {
    compileOnly("org.apache.rat:apache-rat:0.13")

    testImplementation("junit:junit:4.12")
    testImplementation(gradleTestKit())
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.3"
        apiVersion = "1.3"
    }
}

sourceSets {
    main {
        output.dir(tasks.honkerGenDependencies.get().outputDir, "builtBy" to tasks.honkerGenDependencies)
        output.dir(tasks.honkerGenLicense.get().outputDir, "builtBy" to tasks.honkerGenLicense)
        output.dir(tasks.honkerGenNotice.get().outputDir, "builtBy" to tasks.honkerGenNotice)
    }
}
honker.license = "Apache 2"
tasks.honkerGenNotice { footer = "This product includes software developed at\nThe Apache Software Foundation (http://www.apache.org/).\n" }
tasks.check { dependsOn(tasks.honkerCheck) }

tasks.rat {
    excludes.plusAssign(listOf(
            "README.md",
            ".gradletasknamecache", "gradle/wrapper/**", "gradlew*", "build/**", // Gradle
            ".nb-gradle/**", "*.iml", "*.ipr", "*.iws", "*.idea/**" // IDEs
    ))
}
tasks.check { dependsOn(tasks.rat) }
