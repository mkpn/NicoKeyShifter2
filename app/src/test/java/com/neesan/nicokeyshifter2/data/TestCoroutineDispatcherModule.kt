package com.neesan.nicokeyshifter2.data

import com.neesan.data.CoroutineDispatcherModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CoroutineDispatcherModule::class]
)
class TestDispatchersModule {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    fun provideUnconfinedTestDispatcher(): CoroutineDispatcher {
        return UnconfinedTestDispatcher()
    }
}