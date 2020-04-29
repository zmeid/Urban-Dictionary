package com.zmeid.urbandictionary.view.ui

import android.view.View
import android.widget.ProgressBar
import dagger.android.support.DaggerAppCompatActivity

/**
 * It is a good practice to have a [BaseActivity] to hold common variables and methods of all activities.
 */
abstract class BaseActivity : DaggerAppCompatActivity() {

    fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
    }
}