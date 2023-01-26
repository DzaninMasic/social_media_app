package com.example.social_media.common.model.module

import com.example.social_media.data.datasource.AuthDataSource
import com.example.social_media.data.datasource.DatabaseDataSource
import com.example.social_media.data.datasource.StorageDataSource
import com.example.social_media.data.repository.DataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
object RepositoryModule {

    @Provides
    fun provideDataRepository(authDataSource: AuthDataSource,
                              storageDataSource: StorageDataSource,
                              databaseDataSource: DatabaseDataSource) : DataRepository{
        return DataRepository(authDataSource,storageDataSource,databaseDataSource)
    }

}