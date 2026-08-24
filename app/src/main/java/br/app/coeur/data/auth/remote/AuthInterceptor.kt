package br.app.coeur.data.auth.remote

import br.app.coeur.data.auth.local.TokenLocalDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        if (original.url.encodedPath.startsWith("/api/auth/")) {
            return chain.proceed(original)
        }

        val accessToken = tokenLocalDataSource.getAccessToken() ?: return chain.proceed(original)

        val authorized = original.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(authorized)
    }
}
