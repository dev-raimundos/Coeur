package br.app.coeur.data.auth.remote

data class LoginRequest(val email: String, val password: String)

data class RefreshRequest(val refreshToken: String)

data class LogoutRequest(val refreshToken: String)

data class TokenResponse(
    val accessToken: String,
    val expiresAt: String,
    val refreshToken: String
)
