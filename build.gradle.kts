plugins {
    java
    idea
}

group = "tests"
version = "0.0"
var archash: String = "829da164e7"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    testImplementation("junit", "junit", "4.12")
    // yes I know I shouldn't get *every* module
    // I'll optimize this later though
    implementation("com.github.Anuken:Arc:$archash")
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}
tasks.withType<Test> {
    jvmArgs("--enable-preview")
}
tasks.withType<JavaExec> {
    jvmArgs("--enable-preview")
}