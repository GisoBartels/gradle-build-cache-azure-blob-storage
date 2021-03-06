# Gradle Remote Build Cache Plugin for Azure Blob Storage
A plugin for using an Azure Blob Storage as remote build cache.

## Usage
1. Create an Azure Blob Storage Account and Container
2. Create a "shared access signature" for this container with appropriate permissions
   * Allowed services: Blob
   * Allowed resource types: Object
3. Configure the cache in your settings.gradle\[.kts\]
```kotlin
// settings.gradle.kts
buildscript {
    repositories {
        maven { url = uri("https://jitpack.io") }
        mavenCentral()
    }
    dependencies {
        classpath("dev.giso:gradle-build-cache-azure-blob-storage:{version}")
    }
}

buildCache {
    azureBlobStorage {
        url = "https://{your-account}.blob.core.windows.net/{your-container}?{your-sas-token}"
        isPush = true
    }
}
```
 