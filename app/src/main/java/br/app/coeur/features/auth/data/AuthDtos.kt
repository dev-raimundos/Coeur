package br.app.coeur.features.auth.data

data class LoginRequest(val email: String, val password: String)

data class RefreshRequest(val refreshToken: String)

data class LogoutRequest(val refreshToken: String)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long
)
