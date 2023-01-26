package com.example.social_media.common.model.module

import com.example.social_media.data.datasource.AuthDataSource
import com.example.social_media.data.datasource.DatabaseDataSource
import com.example.social_media.data.datasource.StorageDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
object DataSourceModule {

    @Provides
    fun provideAuthDataSource() : AuthDataSource {
        return AuthDataSource()
    }

    @Provides
    fun provideStorageDataSource() : StorageDataSource {
        return StorageDataSource()
    }

    @Provides
    fun provideDatabaseDataSource() : DatabaseDataSource {
        return DatabaseDataSource()
    }

}