package dev.giso.gradle.buildcache.azureblobstorage

import org.gradle.caching.configuration.BuildCacheConfiguration

fun BuildCacheConfiguration.azureBlobStorage(configuration: AzureBlobStorageBuildCache.() -> Unit) {
    registerBuildCacheService(
        AzureBlobStorageBuildCache::class.java,
        AzureBlobStorageBuildCacheServiceFactory::class.java
    )
    remote(AzureBlobStorageBuildCache::class.java, configuration)
}