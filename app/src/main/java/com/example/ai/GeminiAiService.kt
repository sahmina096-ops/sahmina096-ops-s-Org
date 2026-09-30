package com.example.ai

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

data class GroundingSource(
    val title: String,
    val uri: String
)

data class BiologySearchResult(
    val query: String,
    val content: String,
    val searchQueries: List<String> = emptyList(),
    val sources: List<GroundingSource> = emptyList(),
    val isError: Boolean = false,
    val errorMessage: String? = null
)

data class VeoGenerationResult(
    val prompt: String,
    val aspectRatio: String,
    val operationName: String? = null,
    val videoUrl: String? = null,
    val message: String,
    val isSuccess: Boolean = true
)

object GeminiAiService {
    private const val TAG = "GeminiAiService"
    private const val SEARCH_MODEL = "gemini-3.5-flash"
    private const val VEO_MODEL = "veo-3.1-fast-generate-preview"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    private fun getApiKey(): String {
        return try {
            val key = BuildConfig.GEMINI_API_KEY
            if (key.isBlank() || key == "MY_GEMINI_API_KEY") "" else key
        } catch (_: Exception) {
            ""
        }
    }

    val isApiKeyConfigured: Boolean
        get() = getApiKey().isNotEmpty()

    /**
     * Search Grounding using gemini-3.5-flash with googleSearch tool
     */
    suspend fun searchGroundedBiology(query: String): BiologySearchResult = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty()) {
            return@withContext BiologySearchResult(
                query = query,
                content = "Gemini API key is not configured. Please set your GEMINI_API_KEY in the AI Studio Secrets panel to enable real-time Google Search grounding.",
                isError = true,
                errorMessage = "API key missing"
            )
        }

        try {
            val promptText = """
                You are a senior molecular biologist and biochemistry professor.
                Provide an accurate, up-to-date, student-friendly biological explanation for the following query.
                Query: $query
                Explain the biochemical mechanisms, relevant proteins or amino acids, cellular functions, and recent scientific findings.
            """.trimIndent()

            val requestJson = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            put(JSONObject().apply { put("text", promptText) })
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)

                // Search Grounding tool
                val toolsArray = JSONArray().apply {
                    val toolObj = JSONObject().apply {
                        put("googleSearch", JSONObject())
                    }
                    put(toolObj)
                }
                put("tools", toolsArray)
            }

            val url = "$BASE_URL/$SEARCH_MODEL:generateContent?key=$apiKey"
            val requestBody = requestJson.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(TAG, "Search Grounding error (${response.code}): $responseBody")
                return@withContext BiologySearchResult(
                    query = query,
                    content = "Unable to fetch search results (${response.code}). Please verify your network or Gemini API quota.",
                    isError = true,
                    errorMessage = "HTTP ${response.code}"
                )
            }

            val json = JSONObject(responseBody)
            val candidates = json.optJSONArray("candidates")
            val candidate = candidates?.optJSONObject(0)
            val contentObj = candidate?.optJSONObject("content")
            val parts = contentObj?.optJSONArray("parts")

            val fullText = StringBuilder()
            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val p = parts.optJSONObject(i)
                    val t = p?.optString("text")
                    if (!t.isNullOrEmpty()) {
                        fullText.append(t)
                    }
                }
            }

            // Extract Grounding Metadata
            val groundingMetadata = candidate?.optJSONObject("groundingMetadata")
            val queriesList = mutableListOf<String>()
            val webQueries = groundingMetadata?.optJSONArray("webSearchQueries")
            if (webQueries != null) {
                for (i in 0 until webQueries.length()) {
                    val q = webQueries.optString(i)
                    if (q.isNotEmpty()) queriesList.add(q)
                }
            }

            val sourcesList = mutableListOf<GroundingSource>()
            val chunks = groundingMetadata?.optJSONArray("groundingChunks")
            if (chunks != null) {
                for (i in 0 until chunks.length()) {
                    val chunk = chunks.optJSONObject(i)
                    val web = chunk?.optJSONObject("web")
                    if (web != null) {
                        val title = web.optString("title", "Reference Link")
                        val uri = web.optString("uri", "")
                        if (uri.isNotEmpty()) {
                            sourcesList.add(GroundingSource(title = title, uri = uri))
                        }
                    }
                }
            }

            BiologySearchResult(
                query = query,
                content = fullText.toString().ifEmpty { "No response generated from search." },
                searchQueries = queriesList,
                sources = sourcesList.distinctBy { it.uri }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Search Grounding exception", e)
            BiologySearchResult(
                query = query,
                content = "Error performing Google Search Grounding: ${e.localizedMessage}",
                isError = true,
                errorMessage = e.message
            )
        }
    }

    /**
     * Veo Video Generation using veo-3.1-fast-generate-preview
     * Aspect ratio: 16:9 or 9:16
     */
    suspend fun generateVeoVideo(
        imageBitmap: Bitmap?,
        prompt: String,
        aspectRatio: String = "16:9" // "16:9" or "9:16"
    ): VeoGenerationResult = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty()) {
            return@withContext VeoGenerationResult(
                prompt = prompt,
                aspectRatio = aspectRatio,
                message = "Gemini API key is not configured. Please enter your GEMINI_API_KEY in the AI Studio Secrets panel.",
                isSuccess = false
            )
        }

        try {
            val validRatio = if (aspectRatio == "9:16") "9:16" else "16:9"

            val requestJson = JSONObject().apply {
                put("prompt", prompt)

                if (imageBitmap != null) {
                    val outputStream = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                    val base64Image = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

                    val imageObj = JSONObject().apply {
                        put("imageBytes", base64Image)
                        put("mimeType", "image/jpeg")
                    }
                    put("image", imageObj)
                }

                val configObj = JSONObject().apply {
                    put("numberOfVideos", 1)
                    put("aspectRatio", validRatio)
                    put("resolution", "720p")
                }
                put("config", configObj)
            }

            val url = "$BASE_URL/$VEO_MODEL:generateVideos?key=$apiKey"
            val requestBody = requestJson.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(TAG, "Veo generation error (${response.code}): $responseBody")
                return@withContext VeoGenerationResult(
                    prompt = prompt,
                    aspectRatio = validRatio,
                    message = "Veo request failed (${response.code}): ${JSONObject(responseBody).optJSONObject("error")?.optString("message") ?: response.message}",
                    isSuccess = false
                )
            }

            val json = JSONObject(responseBody)
            val opName = json.optString("name", "")

            VeoGenerationResult(
                prompt = prompt,
                aspectRatio = validRatio,
                operationName = opName,
                message = "Veo video generation initialized! Generation operation: $opName",
                isSuccess = true
            )
        } catch (e: Exception) {
            Log.e(TAG, "Veo exception", e)
            VeoGenerationResult(
                prompt = prompt,
                aspectRatio = aspectRatio,
                message = "Veo error: ${e.localizedMessage}",
                isSuccess = false
            )
        }
    }
}
