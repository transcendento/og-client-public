package com.firstbreadclient.mvp

import com.firstbreadclient.activity.ProdActivity
import com.firstbreadclient.mvp.model.ProdInteractor
import com.firstbreadclient.mvp.model.ProdInteractorImpl
import dagger.Module
import dagger.Provides

@Module
class AppModuleProd constructor(private var prodActivity: ProdActivity){
    @Provides
    fun provideProdInteractor(): ProdInteractor {
        return ProdInteractorImpl(prodActivity)
    }

}