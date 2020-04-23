package com.zmeid.urbandictionary.viewmodel

import androidx.lifecycle.ViewModel
import com.zmeid.urbandictionary.repository.UrbanRepository
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val urbanRepository: UrbanRepository): ViewModel() {


}