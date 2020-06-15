package dev.giso.gradle.buildcache.azureblobstorage

import org.gradle.caching.configuration.AbstractBuildCache

open class AzureBlobStorageBuildCache : AbstractBuildCache() {
    var url: String = ""
}
