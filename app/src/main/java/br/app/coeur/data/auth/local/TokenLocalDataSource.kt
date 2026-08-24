package br.app.coeur.data.auth.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_tokens")

@Singleton
class TokenLocalDataSource @Inject constructor(@ApplicationContext private val context: Context) {

    private val aead: Aead by lazy {
        AeadConfig.register()
        AndroidKeysetManager.Builder()
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withSharedPref(context, "coeur_tink_keyset", "coeur_tink_prefs")
            .withMasterKeyUri("android-keystore://coeur_master_key")
            .build()
            .keysetHandle
            .getPrimitive(RegistryConfiguration.get(), Aead::class.java)
    }

    fun getAccessToken(): String? = readDecrypted(KEY_ACCESS_TOKEN)

    fun getRefreshToken(): String? = readDecrypted(KEY_REFRESH_TOKEN)

    fun saveTokens(accessToken: String, refreshToken: String) = runBlocking {
        context.tokenDataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = encrypt(accessToken)
            prefs[KEY_REFRESH_TOKEN] = encrypt(refreshToken)
        }
    }

    fun clearTokens() = runBlocking {
        context.tokenDataStore.edit { it.clear() }
    }

    private fun readDecrypted(key: Preferences.Key<String>): String? = runBlocking {
        val stored = context.tokenDataStore.data.first()[key] ?: return@runBlocking null
        decrypt(stored)
    }

    private fun encrypt(plainText: String): String {
        val encrypted = aead.encrypt(plainText.toByteArray(), null)
        return Base64.getEncoder().encodeToString(encrypted)
    }

    private fun decrypt(cipherText: String): String {
        val decoded = Base64.getDecoder().decode(cipherText)
        return String(aead.decrypt(decoded, null))
    }

    private companion object {
        val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}
