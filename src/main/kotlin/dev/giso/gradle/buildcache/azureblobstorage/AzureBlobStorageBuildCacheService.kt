package dev.giso.gradle.buildcache.azureblobstorage

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.BufferedSink
import org.gradle.api.logging.Logging
import org.gradle.caching.*

class AzureBlobStorageBuildCacheService(
    config: AzureBlobStorageBuildCache,
    private val httpClient: OkHttpClient
) : BuildCacheService {

    private val baseUrl = config.url.toHttpUrl()
    private val log = Logging.getLogger(this::class.java)

    override fun load(key: BuildCacheKey, reader: BuildCacheEntryReader): Boolean {
        val response = try {
            log.info("Fetching remote cache entry: $key")
            httpClient.newCall(Request.Builder().get().url(itemUrl(key)).build()).execute()
        } catch (t: Throwable) {
            log.info("Fetching failed for: $key")
            throw BuildCacheException("Loading build cache entry failed", t)
        }
        log.info("Fetching response: $key - ${response.code} ${response.message}")
        return when {
            response.isSuccessful -> {
                response.body?.use { reader.readFrom(it.byteStream()) }
                true
            }
            response.code == java.net.HttpURLConnection.HTTP_NOT_FOUND -> false
            else -> throw BuildCacheException("Unexpected response code: ${response.code}")
        }.also { response.body?.close() }
    }

    override fun store(key: BuildCacheKey, writer: BuildCacheEntryWriter) {
        try {
            log.info("Storing remote cache entry: $key")
            val response =
                httpClient.newCall(
                    Request.Builder()
                        .put(CacheEntryRequestBody(writer))
                        .url(itemUrl(key))
                        .header("x-ms-blob-type", "BlockBlob")
                        .build()
                ).execute()
            log.info("Storing response: $key - ${response.code} ${response.message}")
            response.body?.close()
        } catch (t: Throwable) {
            log.info("Storing failed for: $key")
            throw BuildCacheException("Loading build cache entry failed", t)
        }
    }

    override fun close() {}

    private fun itemUrl(key: BuildCacheKey): HttpUrl = baseUrl.newBuilder().addPathSegment(key.hashCode).build()

    private class CacheEntryRequestBody(private val writer: BuildCacheEntryWriter) : okhttp3.RequestBody() {
        override fun contentType(): MediaType? = "application/vnd.gradle.build-cache-artifact.v1".toMediaType()

        override fun contentLength(): Long = writer.size

        override fun writeTo(sink: BufferedSink) {
            writer.writeTo(sink.outputStream())
        }

        override fun isOneShot(): Boolean = true
    }
}