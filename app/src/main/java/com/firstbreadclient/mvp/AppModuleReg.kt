package com.firstbreadclient.mvp

import com.firstbreadclient.compose.RegActivity
import com.firstbreadclient.mvp.model.RegInteractor
import com.firstbreadclient.mvp.model.RegInteractorImpl
import dagger.Module
import dagger.Provides

@Module
class AppModuleReg constructor(private var regActivity: RegActivity){
    @Provides
    fun provideRegInteractor(): RegInteractor {
        return RegInteractorImpl(regActivity, regActivity)
    }

}