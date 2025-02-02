plugins {
    application
    checkstyle
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "3.0.1"
}

dependencies {
    // logging
    implementation(platform("org.slf4j:slf4j-bom:2.0.16"))
    implementation("org.slf4j:slf4j-api")
    implementation("org.slf4j:slf4j-simple")

    // testing
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(platform("org.mockito:mockito-bom:5.14.2"))
    testImplementation("org.mockito:mockito-core")

    // util
    implementation("commons-io:commons-io:2.18.0")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("uk.co.conoregan:themoviedbapi:2.3.1")
}

group = "uk.co.conoregan"
version = "2.0.1"
description = "ShowRenamer"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    modularity.inferModulePath = false
}

application {
    mainModule.set("uk.co.conoregan.showrenamer")
    mainClass.set("uk.co.conoregan.showrenamer.ShowRenamerApplication")
}

checkstyle {
    toolVersion = "10.17.0"
    configFile = file("config/checkstyle/checkstyle.xml")
}

tasks.withType<JavaCompile> {
    modularity.inferModulePath = false
}

javafx {
    version = "23.0.2"
    modules("javafx.controls", "javafx.fxml")
}

tasks.test {
    extensions.configure(org.javamodularity.moduleplugin.extensions.TestModuleOptions::class) {
        runOnClasspath = true
    }

    useJUnitPlatform()
}

jlink {
    options.set(listOf("--strip-debug", "--no-header-files", "--no-man-pages"))

    launcher {
        name = "Show Renamer"
    }

    forceMerge("slf4j")

    jpackage {
        val currentOs = org.gradle.internal.os.OperatingSystem.current()
        val imgType = when {
            currentOs.isWindows -> "ico"
            currentOs.isMacOsX -> "icns"
            else -> "png"
        }
        icon = "src/main/resources/images/show-renamer-icon.$imgType"

        installerOptions.addAll(
            listOf(
                "--vendor", "Conor Egan",
                "--app-version", version.toString()
            )
        )

        if (currentOs.isWindows) {
            installerOptions.addAll(
                listOf(
                    "--win-per-user-install",
                    "--win-dir-chooser",
                    "--win-menu",
                    "--verbose"
                )
            )
        }
    }
}
