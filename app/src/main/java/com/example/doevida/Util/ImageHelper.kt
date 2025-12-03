package com.example.doevida.util

import android.util.Log

object ImageHelper {
    // URL base para emulador acessar localhost
    private const val BASE_URL_EMULATOR = "http://10.0.2.2:8080"
    
    // URL base para Azure (produção)
    // Atualizado para corresponder exatamente à URL do Retrofit, incluindo o path da API
    private const val BASE_URL_AZURE = "https://doevida.azurewebsites.net/v1/doevida"
    
    // IMPORTANTE: Use false para produção (Azure)
    private const val USE_LOCAL = false
    
    /**
     * Constrói a URL completa da imagem a partir do path retornado pelo backend
     */
    fun getImageUrl(path: String?): String {
        if (path.isNullOrEmpty()) {
            Log.w("ImageHelper", "Path vazio ou nulo")
            return ""
        }
        
        var processedPath = path.replace("\\", "/") // Corrige barras invertidas do Windows
        
        // 1. Se já for uma URL completa válida
        if (processedPath.startsWith("http://") || processedPath.startsWith("https://")) {
             // Se for localhost/127.0.0.1 e estivermos usando emulador, corrige
             if (USE_LOCAL && (processedPath.contains("localhost") || processedPath.contains("127.0.0.1"))) {
                 val adjustedUrl = processedPath.replace("localhost", "10.0.2.2")
                                                .replace("127.0.0.1", "10.0.2.2")
                 Log.d("ImageHelper", "URL ajustada para emulador: $adjustedUrl")
                 return adjustedUrl
             }
             return processedPath
        }

        // 2. Limpeza de caminhos absolutos do servidor
        if (processedPath.contains("/uploads/")) {
            processedPath = "/uploads/" + processedPath.substringAfter("/uploads/")
        } else if (processedPath.contains("uploads/")) {
            processedPath = "/uploads/" + processedPath.substringAfter("uploads/")
        } else {
            if (!processedPath.startsWith("/")) {
                processedPath = "/$processedPath"
            }
        }

        val baseUrl = if (USE_LOCAL) BASE_URL_EMULATOR else BASE_URL_AZURE
        val fullUrl = "$baseUrl$processedPath"
        
        Log.d("ImageHelper", "URL Final Construída: $fullUrl (Original: $path)")
        return fullUrl
    }
    
    fun isValidImageUrl(url: String?): Boolean {
        return !url.isNullOrEmpty() && 
               (url.startsWith("http://") || url.startsWith("https://"))
    }
}
