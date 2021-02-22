package com.firstbreadclient.mvp

import android.content.Context
import com.firstbreadclient.activity.AuthActivity
import com.firstbreadclient.mvp.model.AuthInteractor
import com.firstbreadclient.mvp.model.AuthInteractorImpl
import dagger.Module
import dagger.Provides

@Module
class AppModule constructor(var authActivity: AuthActivity) {
    @Provides
    fun provideAuthInteractor(): AuthInteractor {
        return AuthInteractorImpl(authActivity, authActivity)
    }
}