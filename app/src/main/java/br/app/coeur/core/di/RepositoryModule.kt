package br.app.coeur.core.di

import br.app.coeur.core.auth.TokenRefresher

import br.app.coeur.features.auth.data.AuthRepository
import br.app.coeur.features.auth.data.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTokenRefresher(impl: AuthRepositoryImpl): TokenRefresher
}
