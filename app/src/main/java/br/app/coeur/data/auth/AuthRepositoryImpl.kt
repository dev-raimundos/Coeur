package br.app.coeur.data.auth

import br.app.coeur.data.auth.local.TokenLocalDataSource
import br.app.coeur.data.auth.remote.AuthApi
import br.app.coeur.data.auth.remote.LoginRequest
import br.app.coeur.data.auth.remote.LogoutRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenLocalDataSource: TokenLocalDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        val result = authApi.login(LoginRequest(email, password))
        tokenLocalDataSource.saveTokens(result.accessToken, result.refreshToken)
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val refreshToken = tokenLocalDataSource.getRefreshToken()
        if (refreshToken != null) {
            authApi.logout(LogoutRequest(refreshToken))
        }
    }.also {
        tokenLocalDataSource.clearTokens()
    }

    override fun isLoggedIn(): Boolean = tokenLocalDataSource.getRefreshToken() != null
}
