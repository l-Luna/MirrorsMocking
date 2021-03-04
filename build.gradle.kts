plugins {
    java
    idea
}

group = "tests"
version = "0.0"
var archash: String = "bfac70a18ab9158efefcd36f3ff43738c96733a0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    testCompile("junit", "junit", "4.12")
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