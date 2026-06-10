package com.anni.pregnancytracker.di

import com.anni.pregnancytracker.domain.calendar.PregnancyCalendarBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun providePregnancyCalendarBuilder(): PregnancyCalendarBuilder = PregnancyCalendarBuilder()
}
