package com.zmeid.urbandictionary.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.zmeid.urbandictionary.R
import com.zmeid.urbandictionary.viewmodel.MainActivityViewModel
import com.zmeid.urbandictionary.viewmodel.factory.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mainActivityViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MainActivityViewModel::class.java)
    }
}