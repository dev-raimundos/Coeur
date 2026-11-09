package br.app.coeur.core.auth

interface TokenRefresher {
    /** Renews and stores the session tokens, returning the new access token. */
    suspend fun refreshAccessToken(refreshToken: String): String
}
