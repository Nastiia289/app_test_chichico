package com.example.test.registration.phone_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhoneOTPActivityViewModel : ViewModel() {

    val onValidationComplete: MutableLiveData<Pair<Boolean, String>> by lazy {
        MutableLiveData<Pair<Boolean, String>>()
    }
    fun validateAndNavigate(inputText: String, isCheckBoxChecked: Boolean) {
        if (inputText.length == 9 && isCheckBoxChecked) {
            val phoneNumber = "+380$inputText"
            onValidationComplete.postValue(Pair(true, phoneNumber))
        } else {
            val errorMessage = when {
                inputText.isEmpty() || inputText.length < 9 -> "Невірно введений номер телефону. Повторіть спробу"
                inputText.length == 9 && !isCheckBoxChecked -> "Прочитайте, будь ласка,\nПравила і умови роботи компанії, поставте галочку, щоб продовжити реєстрацію"
                else -> "Помилка при введенні номеру телефону або встановленні прапорця"
            }
            onValidationComplete.postValue(Pair(false, errorMessage))
        }
    }
}
