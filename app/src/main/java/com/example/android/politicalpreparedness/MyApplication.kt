package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.repository.RepositoryInterface

class MyApplication: Application() {

    val repository : RepositoryInterface
    get() = ServiceLocator.provideRepository(this)

}