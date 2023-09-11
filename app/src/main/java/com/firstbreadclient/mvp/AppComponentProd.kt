package com.firstbreadclient.mvp

import com.firstbreadclient.activity.ProdActivity
import dagger.Component

@Component(modules = [AppModuleProd::class])
interface AppComponentProd {
    fun inject(prodActivity: ProdActivity)
}