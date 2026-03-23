package com.example.valentinesgarage.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Put non-database app-wide dependencies here, e.g., Firebase, Retrofit, etc.
}