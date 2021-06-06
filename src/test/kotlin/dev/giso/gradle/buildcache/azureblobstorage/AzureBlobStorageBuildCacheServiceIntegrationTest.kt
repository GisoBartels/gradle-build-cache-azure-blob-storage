package dev.giso.gradle.buildcache.azureblobstorage

import okhttp3.OkHttpClient
import org.gradle.caching.BuildCacheEntryReader
import org.gradle.caching.BuildCacheEntryWriter
import org.gradle.caching.BuildCacheKey
import org.gradle.internal.impldep.org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import java.io.InputStream
import java.io.OutputStream

internal class AzureBlobStorageBuildCacheServiceIntegrationTest {

    private val config = AzureBlobStorageBuildCache().apply { url = error("Insert your blob storage URL here") }

    private val service = AzureBlobStorageBuildCacheService(config, OkHttpClient())

    @Test
    internal fun `cache entry is stored correctly`() {
        val content = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr"
        val cacheKey = CacheKey(content)

        service.store(cacheKey, CacheEntry(content))

        service.load(cacheKey, ContentVerifier(expectedContent = content))
    }

    private class CacheKey(content: String) : BuildCacheKey {
        private val key = "TEST-" + content.hashCode().toString()
        override fun getDisplayName(): String = "Random cache key: $hashCode"
        override fun getHashCode(): String = key
        override fun toByteArray(): ByteArray = hashCode.toByteArray()
    }

    private class CacheEntry(val content: String) : BuildCacheEntryWriter {
        override fun writeTo(output: OutputStream) {
            output.writer().use { it.write(content) }
        }

        override fun getSize(): Long = content.length.toLong()
    }

    private class ContentVerifier(val expectedContent: String) : BuildCacheEntryReader {
        override fun readFrom(input: InputStream) {
            val actualContent = input.reader().use { it.readText() }
            assertEquals(expectedContent, actualContent)
        }
    }
}