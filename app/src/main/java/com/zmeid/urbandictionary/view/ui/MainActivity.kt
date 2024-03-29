package com.zmeid.urbandictionary.view.ui

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.zmeid.urbandictionary.R
import com.zmeid.urbandictionary.databinding.ActivityMainBinding
import com.zmeid.urbandictionary.model.UrbanApiResponseModel
import com.zmeid.urbandictionary.util.*
import com.zmeid.urbandictionary.view.adapter.OnItemClickListener
import com.zmeid.urbandictionary.view.adapter.UrbanAdapter
import com.zmeid.urbandictionary.view.interfaces.SearchViewOnQueryTextChangedListener
import com.zmeid.urbandictionary.viewmodel.MainActivityViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

/**
 * [MainActivity] is the only activity of the application.
 *
 * View binding is enabled. Communicates with [MainActivityViewModel].
 */
class MainActivity : BaseActivity(), SearchViewOnQueryTextChangedListener, View.OnClickListener,
    DialogUtils.ChoiceClickedListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var viewModelProviderFactory: ViewModelProvider.Factory
    @Inject lateinit var layoutManager: LinearLayoutManager
    @Inject lateinit var urbanAdapter: UrbanAdapter
    @Inject lateinit var errorMessageGenerator: ErrorMessageGenerator
    @Inject lateinit var dialogUtils: DialogUtils
    @Inject lateinit var shareTextIntent: Intent
    @Inject lateinit var dataSourceFactory: DefaultDataSourceFactory
    @Inject lateinit var simpleExoPlayer: SimpleExoPlayer

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var searchQueryDelayJob: Job? = null
    private lateinit var searchViewMenu: SearchView
    private lateinit var searchViewMenuItem: MenuItem
    private var wordToSearch: String by StringTrimDelegate()
    private var sortingPrefDialog: AlertDialog? = null
    private var urbanPlaySoundUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerviewUrbanResults.layoutManager = layoutManager
        binding.recyclerviewUrbanResults.adapter = urbanAdapter
        setAdapterItemClickListener()

        binding.buttonRetry.setOnClickListener(this)

        binding.swipeRefreshLayout.setOnRefreshListener(this)

        mainActivityViewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainActivityViewModel::class.java)
        observeUrbanDefinitionResult()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        sortingPrefDialog?.isShowing?.let { outState.putBoolean(SORTING_PREF_DIALOG_IS_SHOWING_TAG, it) }
        outState.putLong(SOUND_PLAYER_TIME_POSITION, simpleExoPlayer.contentPosition)
        outState.putString(SOUND_PLAYER_URL, urbanPlaySoundUrl)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val isSortPrefDialogShowing = savedInstanceState.getBoolean(SORTING_PREF_DIALOG_IS_SHOWING_TAG, false)
        if (isSortPrefDialogShowing) showSortingPrefDialog()

        val soundTimePosition = savedInstanceState.getLong(SOUND_PLAYER_TIME_POSITION, 0L)
        urbanPlaySoundUrl = savedInstanceState.getString(SOUND_PLAYER_URL, "")
        if (soundTimePosition > 0 && urbanPlaySoundUrl.isNotBlank()) {
            playUrbanSound(urbanPlaySoundUrl, soundTimePosition)
        }
    }

    /**
     * Observes if there are any changes in urbanDefinitionResult live data and calls [handleDefinitionResult]
     */
    private fun observeUrbanDefinitionResult() {
        mainActivityViewModel.urbanDefinitionResult.observe(this, Observer {
            Timber.d("URBAN SEARCH RESPONSE: \n $it")
            handleDefinitionResult(it)
        })
    }

    /**
     * Handles definition results according to [ApiResponseWrapper]'s status.
     *
     * If [ApiResponseWrapper.Status.LOADING], it shows progress bar, hides user messages and hides retry button.
     * If [ApiResponseWrapper.Status.SUCCESS], updates [UrbanAdapter] with received data, hides progress bar, hides user messages and hides retry button. If the received data is empty; shows a message to user.
     * If [ApiResponseWrapper.Status.ERROR], it shows the error message and retry button. Clears recyclerview.
     */
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
                val errorMessage = errorMessageGenerator.generateErrorMessage(apiResponseWrapper.exception!!)
                urbanAdapter.submitList(null)
                showUserMessage(errorMessage)
                showRetryButton()
            }
        }
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

    /**
     * Initializes search menu and sets [SearchViewOnQueryTextChangedListener].
     */
    private fun initSearchMenu(menu: Menu) {
        searchViewMenuItem = menu.findItem(R.id.menu_search)
        searchViewMenu = searchViewMenuItem.actionView as SearchView
        searchViewMenu.maxWidth = Integer.MAX_VALUE // This is hack to solve close button wrong alignment.
        observeUrbanLastSearchWord()
        searchViewMenu.queryHint = getString(R.string.search_view_query_hint)
        searchViewMenu.setOnQueryTextListener(this)
    }

    /**
     * If search view is not empty and the devices is rotated; it observes once to get last searched word.
     */
    private fun observeUrbanLastSearchWord() {
        mainActivityViewModel.urbanLastSearchWord.observeOnce(this, Observer {
            searchViewMenuItem.expandActionView()
            searchViewMenu.setQuery(it, false)
            wordToSearch = it
        })
    }

    override fun onQueryTextChange(word: String): Boolean {
        searchQueryDelayJob?.cancel()
        searchQueryDelayJob = lifecycleScope.launch {
            delay(700)
            if (word.isNotEmpty()) {
                wordToSearch = word
                mainActivityViewModel.searchDefinition(wordToSearch, false)
            } else if (urbanAdapter.itemCount == 0) {
                withContext(Dispatchers.Main) {
                    showUserMessage(getString(R.string.type_to_get_definitions))
                }
            }
        }
        return true
    }

    /**
     * Shows a message to user in the center of activity.
     */
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
        sortingPrefDialog!!.show()
    }

    override fun choiceClicked(shouldSortByThumbsUp: Boolean) {
        mainActivityViewModel.sortDefinitionResults(shouldSortByThumbsUp)
    }

    /**
     * Catches swap-to-refresh event.
     */
    override fun onRefresh() {
        if (wordToSearch.isNotEmpty()) mainActivityViewModel.searchDefinition(wordToSearch, true)
        binding.swipeRefreshLayout.isRefreshing = false
    }

    /**
     * Catches clicks on share and play sound buttons.
     */
    private fun setAdapterItemClickListener() {
        urbanAdapter.setOnItemClickedListener(object : OnItemClickListener {
            override fun onShareClicked(text: String) {
                val sendIntent = shareTextIntent.putExtra(Intent.EXTRA_TEXT, text)

                if (sendIntent.resolveActivity(packageManager) != null) {
                    startActivity(sendIntent)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.no_send_text_intent_resolver),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onPlaySoundClicked(url: String) {
                Timber.d("Play Sound Clicked Clicked: $url")
                urbanPlaySoundUrl = url
                playUrbanSound(url, 0L)
            }
        })
    }

    /**
     * Sets the media URL for [simpleExoPlayer], makes sound player visible and plays the sound as stream in background. When playing sound is finished, sound player is invisible.
     *
     * If user pauses the player, in case of 3 seconds of in activity, player becomes invisible.
     *
     *  [simpleExoPlayer] should be released in [onDestroy].
     */
    private fun playUrbanSound(url: String, resumeTime: Long) {
        binding.urbanPlayerControlView.player = simpleExoPlayer
        binding.urbanPlayerControlView.visibility = View.VISIBLE
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url))
        simpleExoPlayer.apply {
            prepare(mediaSource)
            seekTo(resumeTime)
            playWhenReady = true
        }
        simpleExoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                Timber.e(error)
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.media_player_general_error),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    urbanPlaySoundUrl = ""
                    binding.urbanPlayerControlView.visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroy() {
        simpleExoPlayer.release()
        super.onDestroy()
    }
}