package com.example.muslim.di

import com.example.muslim.data.remote.repository.PrayerTimesRepositoryImpl
import com.example.muslim.domain.repository.PrayerTimesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providePrayerTimesRepository(impl: PrayerTimesRepositoryImpl): PrayerTimesRepository {
        return impl
    }
}