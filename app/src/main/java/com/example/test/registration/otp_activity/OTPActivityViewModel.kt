package com.example.test.registration.otp_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OTPActivityViewModel : ViewModel() {
    var data = 0

    val currentNumber: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val currentBoolean: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val onSignInClick: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val onResendClick: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val verificationCode: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    public fun signInButtonClick() {
        onSignInClick.postValue(true)
    }

    public fun resendButtonClick() {
        onResendClick.postValue(true)
    }
}
