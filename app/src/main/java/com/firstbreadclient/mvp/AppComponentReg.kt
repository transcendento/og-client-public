package com.firstbreadclient.mvp

import com.firstbreadclient.compose.RegActivity
import dagger.Component

@Component(modules = [AppModuleReg::class])
interface AppComponentReg {
    fun inject(regActivity: RegActivity)
}