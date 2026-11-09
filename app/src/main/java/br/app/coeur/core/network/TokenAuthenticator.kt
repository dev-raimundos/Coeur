package br.app.coeur.core.network

import br.app.coeur.core.auth.TokenRefresher
import br.app.coeur.core.auth.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val tokenRefresher: TokenRefresher
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        val failedAccessToken = response.request.header("Authorization")

        synchronized(this) {
            val currentAccessToken = tokenStorage.getAccessToken()
            if (currentAccessToken != null && "Bearer $currentAccessToken" != failedAccessToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentAccessToken")
                    .build()
            }

            val refreshToken = tokenStorage.getRefreshToken() ?: return null

            val accessToken = try {
                runBlocking { tokenRefresher.refreshAccessToken(refreshToken) }
            } catch (e: Exception) {
                null
            }

            if (accessToken == null) {
                tokenStorage.clearTokens()
                return null
            }

            return response.request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
