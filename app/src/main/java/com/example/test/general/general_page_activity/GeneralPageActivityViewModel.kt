package com.example.test.general.general_page_activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class GeneralPageActivityViewModel : ViewModel() {

    val onSignInClick: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val onToLocationClick: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    public fun signInButtonClick() {
        onSignInClick.postValue(true)
    }

    public fun toLocationClick() {
        onToLocationClick.postValue(true)
    }
}
