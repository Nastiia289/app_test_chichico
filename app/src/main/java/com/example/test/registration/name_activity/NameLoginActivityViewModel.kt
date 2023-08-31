package com.example.test.registration.name_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.utils.FirebaseUtil
import com.example.test.model.UserModel
import com.google.firebase.Timestamp

class NameLoginActivityViewModel : ViewModel() {

    val onNameSet: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    public fun onNameSetClick() {
        onNameSet.postValue(true)
    }

    fun setNameAndNavigate(username: String, phoneNumber: String) {
        if (username.isNotEmpty() && username.length >= 3) {
            val userModel = UserModel(phoneNumber, username, Timestamp.now())
            FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onNameSet.postValue(true)
                }
            }
        }
    }
}
