package com.zmeid.urbandictionary.view.interfaces

import androidx.appcompat.widget.SearchView

interface SearchViewOnQueryTextChangedListener : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }
}