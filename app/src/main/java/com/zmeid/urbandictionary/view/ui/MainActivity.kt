package com.zmeid.urbandictionary.view.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zmeid.urbandictionary.R
import com.zmeid.urbandictionary.databinding.ActivityMainBinding
import com.zmeid.urbandictionary.model.UrbanApiResponseModel
import com.zmeid.urbandictionary.util.*
import com.zmeid.urbandictionary.view.adapter.UrbanAdapter
import com.zmeid.urbandictionary.view.interfaces.SearchViewOnQueryTextChangedListener
import com.zmeid.urbandictionary.viewmodel.MainActivityViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(), SearchViewOnQueryTextChangedListener, View.OnClickListener,
    DialogUtils.ChoiceClickedListener {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    @Inject
    lateinit var layoutManager: LinearLayoutManager

    @Inject
    lateinit var urbanAdapter: UrbanAdapter

    @Inject
    lateinit var apiErrorMessageGenerator: ApiErrorMessageGenerator

    @Inject
    lateinit var dialogUtils: DialogUtils

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var searchQueryDelayJob: Job? = null
    private lateinit var searchViewMenu: SearchView
    private lateinit var searchViewMenuItem: MenuItem
    private var wordToSearch: String by StringTrimDelegate()
    private var sortingPrefDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainActivityViewModel::class.java)
        observeUrbanDefinitionResult()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SORTING_PREF_DIALOG_IS_SHOWING_TAG, sortingPrefDialog?.isShowing ?: false)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val isSortPrefDialogShowing = savedInstanceState.getBoolean(SORTING_PREF_DIALOG_IS_SHOWING_TAG, false)
        if (isSortPrefDialogShowing) showSortingPrefDialog()
    }

    private fun observeUrbanDefinitionResult() {
        mainActivityViewModel.urbanDefinitionResult.observe(this, Observer {
            Timber.d("URBAN SEARCH RESPONSE: \n $it")
            handleDefinitionResult(it)
        })
    }

    private fun handleDefinitionResult(apiResponseWrapper: ApiResponseWrapper<UrbanApiResponseModel>) {
        when (apiResponseWrapper.status) {
            ApiResponseWrapper.Status.LOADING -> {
                showProgressBar(binding.progressBarMainActivity)
                hideUserMessageText()
                hideRetryButton()
            }
            ApiResponseWrapper.Status.SUCCESS -> {
                hideProgressBar(binding.progressBarMainActivity)
                hideUserMessageText()
                hideRetryButton()
                val urbanList = apiResponseWrapper.data!!.urbanList
                urbanAdapter.submitList(urbanList)
                if (urbanList.isEmpty()) showUserMessage(getString(R.string.nothing_found))
            }
            ApiResponseWrapper.Status.ERROR -> {
                hideProgressBar(binding.progressBarMainActivity)
                val errorMessage = apiErrorMessageGenerator.generateErrorMessage(apiResponseWrapper.exception!!)
                urbanAdapter.submitList(null)
                showUserMessage(errorMessage)
                showRetryButton()
            }
        }
    }

    override fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerviewUrbanResults.layoutManager = layoutManager
        binding.recyclerviewUrbanResults.adapter = urbanAdapter

        binding.buttonRetry.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        initSearchMenu(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sort -> {
                showSortingPrefDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initSearchMenu(menu: Menu) {
        searchViewMenuItem = menu.findItem(R.id.menu_search)
        searchViewMenu = searchViewMenuItem.actionView as SearchView
        searchViewMenu.maxWidth = Integer.MAX_VALUE // THis is hack to solve close button wrong alignment
        observeUrbanLastSearchWord()
        searchViewMenu.queryHint = getString(R.string.search_view_query_hint)
        searchViewMenu.setOnQueryTextListener(this)
    }

    private fun observeUrbanLastSearchWord() {
        mainActivityViewModel.urbanLastSearchWord.observeOnce(this, Observer {
            searchViewMenuItem.expandActionView()
            searchViewMenu.setQuery(it, false)
        })
    }

    override fun onQueryTextChange(word: String): Boolean {
        wordToSearch = word
        searchQueryDelayJob?.cancel()
        searchQueryDelayJob = lifecycleScope.launch {
            delay(700)
            if (wordToSearch.isNotEmpty()) {
                mainActivityViewModel.searchDefinition(wordToSearch, false)
            } else if (urbanAdapter.itemCount == 0) {
                withContext(Dispatchers.Main) {
                    showUserMessage(getString(R.string.type_to_get_definitions))
                }
            }
        }
        return true
    }

    private fun showUserMessage(message: String) {
        binding.textViewUserMessage.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    private fun hideUserMessageText() {
        binding.textViewUserMessage.visibility = View.GONE
    }

    private fun showRetryButton() {
        binding.buttonRetry.visibility = View.VISIBLE
    }

    private fun hideRetryButton() {
        binding.buttonRetry.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.buttonRetry.id -> {
                mainActivityViewModel.searchDefinition(wordToSearch, true)
            }
        }
    }

    private fun showSortingPrefDialog() {
        sortingPrefDialog = dialogUtils.createSortingPrefDialog()
        sortingPrefDialog?.show()
    }

    override fun choiceClicked(shouldSortByThumbsUp: Boolean) {
        mainActivityViewModel.sortDefinitionResults(shouldSortByThumbsUp)
    }
}