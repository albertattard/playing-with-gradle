plugins {
    java
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(group = "commons-beanutils", name = "commons-beanutils", version = "1.9.4") {
        exclude(group = "commons-collections", module = "commons-collections")
    }
}
