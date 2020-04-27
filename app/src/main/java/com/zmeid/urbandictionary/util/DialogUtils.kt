package com.zmeid.urbandictionary.util

import android.app.Activity
import android.app.AlertDialog
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.zmeid.urbandictionary.R
import com.zmeid.urbandictionary.view.ui.BaseActivity
import javax.inject.Inject

class DialogUtils @Inject constructor(
    private val activity: Activity,
    private val sharedPreferences: SharedPreferences
) : LifecycleObserver {

    private var alertDialogSortingPref: AlertDialog? = null

    init {
        (activity as BaseActivity).lifecycle.addObserver(this)
    }

    fun createSortingPrefDialog(): AlertDialog {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.sort_definitions)
        val choiceItems = arrayOf(activity.getString(R.string.byThumbsUp), activity.getString(R.string.byThumbsDown))
        val shouldSortByThumbsUp = sharedPreferences.getBoolean(SHARED_PREF_SHOULD_SORT_BY_THUMBS_UP, true)

        val checkedItem = if (shouldSortByThumbsUp) {
            0
        } else {
            1
        }

        alertDialogBuilder.setSingleChoiceItems(choiceItems, checkedItem) { dialog, which ->
            when (which) {
                0 -> (activity as ChoiceClickedListener).choiceClicked(true)
                1 -> (activity as ChoiceClickedListener).choiceClicked(false)
            }
            dialog.dismiss()
        }
        alertDialogSortingPref = alertDialogBuilder.create()
        return alertDialogSortingPref!!
    }

    interface ChoiceClickedListener {
        fun choiceClicked(shouldSortByThumbsUp: Boolean)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun dismissAlertDialogSortingPref() {
        alertDialogSortingPref?.dismiss()
    }
}