package com.example.vaccinewatch

import com.example.vaccinewatch.BuildConfig
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class User(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val createdAt: String,
    val dob: String,
    val medicalConditionRawJson: String?
)

data class VaccineHistory(
    val id: Long,
    val doseDate: String,
    val userId: Long?,
    val vaccineSchedId: Long?
)

data class VaccineSchedule(
    val id: Long,
    val vaccineCode: String,
    val vaccineName: String,
    val createdAt: String,
    val recDose: Long?,
    val doseAgeMonths: Long?,
    val isDeleted: Boolean?,
    val requiredConditionsRawJson: String?,
    val ineligibilityConditionsRawJson: String?,
    val doseMinIntervalMonths: Long?
)

private val http = OkHttpClient()

suspend fun fetchUserById(
    functionUrl: String = "https://mppdufhzmlxwgmjpsyse.supabase.co/functions/v1/getUserByID",
    userId: Long,
    accessToken: String = BuildConfig.SUPABASE_ANON_KEY
): User? = withContext(Dispatchers.IO) {

    val url = "$functionUrl?userId=$userId"

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

        User(
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

suspend fun fetchAllVaccines(
    functionUrl: String = "https://mppdufhzmlxwgmjpsyse.supabase.co/functions/v1/getAllVaccines",
    accessToken: String = BuildConfig.SUPABASE_ANON_KEY
): List<VaccineSchedule> = withContext(Dispatchers.IO) {

    val reqBuilder = Request.Builder().url(functionUrl)
    if (!accessToken.isNullOrBlank()) {
        reqBuilder.header("Authorization", "Bearer $accessToken")
    }

    val req = reqBuilder.get().build()

    http.newCall(req).execute().use { res ->
        if (!res.isSuccessful) throw RuntimeException("HTTP ${res.code}")

        val bodyStr = res.body?.string() ?: throw RuntimeException("Empty body")
        val root = JSONObject(bodyStr)

        // Expected: { "data": [ { ... }, { ... } ] }
        val arr = root.optJSONArray("data") ?: return@withContext emptyList()

        val out = ArrayList<VaccineSchedule>(arr.length())
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)

            out += VaccineSchedule(
                id = obj.getLong("id"),
                vaccineCode = obj.getString("vaccine_code"),
                vaccineName = obj.getString("vaccine_name"),
                createdAt = obj.getString("created_at"),
                recDose = obj.optLongOrNull("rec_dose"),
                doseAgeMonths = obj.optLongOrNull("dose_age_months"),
                isDeleted = obj.optBooleanOrNull("is_deleted"),
                requiredConditionsRawJson = obj.opt("required_conditions")?.toString(),
                ineligibilityConditionsRawJson = obj.opt("ineligibility_conditions")?.toString(),
                doseMinIntervalMonths = obj.optLongOrNull("dose_min_interval_months")
            )
        }

        out
    }
}

private fun JSONObject.optLongOrNull(key: String): Long? =
    if (has(key) && !isNull(key)) optLong(key) else null

private fun JSONObject.optBooleanOrNull(key: String): Boolean? =
    if (has(key) && !isNull(key)) optBoolean(key) else null

suspend fun fetchVaccineHistByUserID(
    userId: Long,
    functionUrl: String = "https://mppdufhzmlxwgmjpsyse.supabase.co/functions/v1/getVaccineHistByUserId",
    accessToken: String = BuildConfig.SUPABASE_ANON_KEY
): List<VaccineHistory> = withContext(Dispatchers.IO) {

    val url = "$functionUrl?userId=$userId"

    val reqBuilder = Request.Builder().url(url)

    if (!accessToken.isNullOrBlank()) {
        reqBuilder.header("Authorization", "Bearer $accessToken")
    }

    val req = reqBuilder.get().build()

    http.newCall(req).execute().use { res ->
        if (!res.isSuccessful) throw RuntimeException("HTTP ${res.code}")

        val bodyStr = res.body?.string() ?: throw RuntimeException("Empty body")
        val root = JSONObject(bodyStr)

        // Most common: { "data": [ { ... }, { ... } ] }
        val dataArr = when (val dataAny = root.opt("data")) {
            is org.json.JSONArray -> dataAny
            is org.json.JSONObject -> {
                // fallback if your function wraps it like { data: { rows: [...] } } or similar
                dataAny.optJSONArray("rows")
                    ?: dataAny.optJSONArray("vaccine_history")
                    ?: org.json.JSONArray()
            }
            else -> org.json.JSONArray()
        }

        val out = ArrayList<VaccineHistory>(dataArr.length())
        for (i in 0 until dataArr.length()) {
            val obj = dataArr.getJSONObject(i)
            out += VaccineHistory(
                id = obj.getLong("id"),
                doseDate = obj.getString("dose_date"),
                userId = obj.optLongOrNull("user_id"),
                vaccineSchedId = obj.optLongOrNull("vaccine_sched_id")
            )
        }
        out
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            try {
                val hist = fetchVaccineHistByUserID(userId = 1L)
                Log.d("FETCH_TEST", "hist count = ${hist.size}")
                hist.take(10).forEachIndexed { i, h ->
                    Log.d("FETCH_TEST", "[$i] $h")
                }
            } catch (e: Exception) {
                Log.e("FETCH_TEST", "fetchVaccineHistByUserID error", e)
            }
        }

        setContent {
            // Compose UI here
        }
    }
}