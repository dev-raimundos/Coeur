package br.app.coeur.data.auth.remote

import br.app.coeur.data.auth.local.TokenLocalDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val authApi: AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        val failedAccessToken = response.request.header("Authorization")

        synchronized(this) {
            val currentAccessToken = tokenLocalDataSource.getAccessToken()
            if (currentAccessToken != null && "Bearer $currentAccessToken" != failedAccessToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentAccessToken")
                    .build()
            }

            val refreshToken = tokenLocalDataSource.getRefreshToken() ?: return null

            val result = try {
                runBlocking { authApi.refresh(RefreshRequest(refreshToken)) }
            } catch (e: Exception) {
                null
            }

            if (result == null) {
                tokenLocalDataSource.clearTokens()
                return null
            }

            tokenLocalDataSource.saveTokens(result.accessToken, result.refreshToken)

            return response.request.newBuilder()
                .header("Authorization", "Bearer ${result.accessToken}")
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
