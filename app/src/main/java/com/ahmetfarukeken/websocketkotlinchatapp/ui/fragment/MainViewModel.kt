package com.ahmetfarukeken.websocketkotlinchatapp.ui.fragment

import androidx.lifecycle.MutableLiveData
import com.ahmetfarukeken.websocketkotlinchatapp.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor() : BaseViewModel(){
    private val isWebSocketError = MutableLiveData<Boolean>()
    private val isWebSocketLoading = MutableLiveData<Boolean>()
}