package com.ncs.chatsystem.di

import com.ncs.chatsystem.firebaseauth.repository.AuthRepository
import com.ncs.chatsystem.firebaseauth.repository.AuthRepositoryImpl
import com.ncs.chatsystem.firebaseauth.repository.RealtimeDBRepository
import com.ncs.chatsystem.firebaseauth.repository.RealtimeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesFirebaseAuthRepository(
        repo: AuthRepositoryImpl
    ): AuthRepository
    @Binds
    abstract fun providesRealtimeRepository(
        repo: RealtimeDBRepository
    ): RealtimeRepository
}