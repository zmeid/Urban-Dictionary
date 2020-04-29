package com.zmeid.urbandictionary.view.interfaces

import androidx.appcompat.widget.SearchView

/**
 * Extends [SearchView.OnQueryTextListener] to suppress [onQueryTextSubmit] in order to have cleaner code in [com.zmeid.urbandictionary.view.ui.MainActivity]
 */
interface SearchViewOnQueryTextChangedListener : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }
}