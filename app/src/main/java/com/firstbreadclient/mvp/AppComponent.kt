package com.firstbreadclient.mvp

import com.firstbreadclient.activity.AuthActivity
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(authActivity: AuthActivity)
}