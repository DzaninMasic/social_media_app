package com.example.social_media.common.model.module

import com.example.social_media.data.repository.DataRepository
import com.example.social_media.presentation.home.addpost.AddPostPresenter
import com.example.social_media.presentation.home.feed.FeedPresenter
import com.example.social_media.presentation.home.settings.SettingsPresenter
import com.example.social_media.presentation.login.LoginPresenter
import com.example.social_media.presentation.register.RegisterPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {
    @Provides
    fun provideRegisterPresenter(repository: DataRepository) : RegisterPresenter {
        return RegisterPresenter(repository)
    }

    @Provides
    fun provideLoginPresenter(repository: DataRepository) : LoginPresenter {
        return LoginPresenter(repository)
    }

    @Provides
    fun provideAddPostPresenter(repository: DataRepository) : AddPostPresenter {
        return AddPostPresenter(repository)
    }

    @Provides
    fun provideFeedPresenter(repository: DataRepository) : FeedPresenter {
        return FeedPresenter(repository)
    }

    @Provides
    fun provideSettingsPresenter(repository: DataRepository) : SettingsPresenter {
        return SettingsPresenter(repository)
    }
}