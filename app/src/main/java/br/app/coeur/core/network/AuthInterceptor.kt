package br.app.coeur.core.network

import br.app.coeur.core.auth.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        if (original.url.encodedPath.startsWith("/api/auth/")) {
            return chain.proceed(original)
        }

        val accessToken = tokenStorage.getAccessToken() ?: return chain.proceed(original)

        val authorized = original.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(authorized)
    }
}
