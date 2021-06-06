[![Release](https://jitpack.io/v/dev.giso/gradle-build-cache-azure-blob-storage.svg?style=flat-square)](https://jitpack.io/#dev.giso/gradle-build-cache-azure-blob-storage)
[![License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0)

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

## Debugging
To test and debug integration with your blob storage, you can use the
[AzureBlobStorageBuildCacheServiceIntegrationTest](src/test/kotlin/dev/giso/gradle/buildcache/azureblobstorage/AzureBlobStorageBuildCacheServiceIntegrationTest.kt).\
Just insert the URL of your blob storage there.
