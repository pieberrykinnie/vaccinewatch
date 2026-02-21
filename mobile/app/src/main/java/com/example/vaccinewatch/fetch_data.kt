package com.example.vaccinewatch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

private val http = OkHttpClient()

suspend fun fetchUserById(
    functionUrl: String,
    userId: Long,
    accessToken: String? = null
): User? = withContext(Dispatchers.IO) {

    val url = "$functionUrl?id=$userId"

    val reqBuilder = Request.Builder().url(url)
    if (!accessToken.isNullOrBlank()) {
        reqBuilder.header("Authorization", "Bearer $accessToken")
    }
    val req = reqBuilder.get().build()

    val res = http.newCall(req).execute()
    try {
        if (!res.isSuccessful) throw RuntimeException("HTTP ${res.code}")

        val bodyStr = res.body?.string() ?: throw RuntimeException("Empty body")
        val root = JSONObject(bodyStr)

        val data = root.optJSONObject("data") ?: return@withContext null

        return@withContext User(
            id = data.getLong("id"),
            firstName = data.getString("first_name"),
            lastName = data.getString("last_name"),
            createdAt = data.getString("created_at"),
            dob = data.getString("dob"),
            medicalConditionRawJson = data.opt("medical_condition")?.toString()
        )
    } finally {
        res.close()
    }
}