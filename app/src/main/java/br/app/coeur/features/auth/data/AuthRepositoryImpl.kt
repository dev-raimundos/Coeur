package br.app.coeur.features.auth.data

import br.app.coeur.core.auth.TokenStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        val result = authApi.login(LoginRequest(email, password))
        tokenStorage.saveTokens(result.accessToken, result.refreshToken)
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val refreshToken = tokenStorage.getRefreshToken()
        if (refreshToken != null) {
            authApi.logout(LogoutRequest(refreshToken))
        }
    }.also {
        tokenStorage.clearTokens()
    }

    override fun isLoggedIn(): Boolean = tokenStorage.getRefreshToken() != null
}
