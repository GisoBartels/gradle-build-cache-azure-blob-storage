package dev.giso.gradle.buildcache.azureblobstorage

import okhttp3.OkHttpClient
import org.gradle.caching.BuildCacheService
import org.gradle.caching.BuildCacheServiceFactory

class AzureBlobStorageBuildCacheServiceFactory : BuildCacheServiceFactory<AzureBlobStorageBuildCache> {
    override fun createBuildCacheService(
        configuration: AzureBlobStorageBuildCache,
        describer: BuildCacheServiceFactory.Describer
    ): BuildCacheService =
        AzureBlobStorageBuildCacheService(
            configuration,
            OkHttpClient()
        )
}