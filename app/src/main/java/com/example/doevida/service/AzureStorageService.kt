package com.example.doevida.service

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object AzureStorageService {

    private const val AZURE_STORAGE_ACCOUNT = "doevidastorage"
    private const val AZURE_CONTAINER_NAME = "imagens-geral"

    // Token SAS para permitir o UPLOAD (escrita) no contêiner.
    private const val AZURE_SAS_TOKEN = "sv=2024-11-04&ss=b&srt=co&sp=rwlactfx&se=2026-11-11T07:46:08Z&st=2025-11-11T23:31:08Z&spr=https&sig=CXyHuqMX30rK9Any9UCg01LQzV4m78sabC2kHPRd2S8%3D"

    suspend fun uploadImageToAzure(context: Context, imageUri: Uri): String? {
        // A verificação de erro anterior foi REMOVIDA, pois estava causando o problema.
        return withContext(Dispatchers.IO) {
            val file = getFileFromUri(context, imageUri) ?: return@withContext null

            val client = OkHttpClient()
            val blobName = "${System.currentTimeMillis()}_${file.name.substringAfterLast('/')}"
            val baseUrl = "https://$AZURE_STORAGE_ACCOUNT.blob.core.windows.net/$AZURE_CONTAINER_NAME/$blobName"
            val uploadUrl = "$baseUrl?$AZURE_SAS_TOKEN"

            val mediaType = "image/jpeg".toMediaTypeOrNull()
            val requestBody = file.asRequestBody(mediaType)

            val request = Request.Builder()
                .url(uploadUrl)
                .put(requestBody)
                .addHeader("x-ms-blob-type", "BlockBlob")
                .build()

            val resultUrl = try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    baseUrl
                } else {
                    println("Falha no upload: ${response.code} ${response.message}")
                    println("Corpo da resposta: ${response.body?.string()}")
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            file.delete()
            resultUrl
        }
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        val tempFile = createTempImageFileForUpload(context)
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: return null
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            tempFile.delete()
            null
        }
    }

    private fun createTempImageFileForUpload(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File.createTempFile("UPLOAD_${timeStamp}_", ".jpg", context.cacheDir)
    }
}
