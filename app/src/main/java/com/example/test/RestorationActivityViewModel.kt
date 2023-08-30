package com.example.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestorationActivityViewModel: ViewModel() {

    val onGeneralPage: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val onRestaurantPage: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val onAddReview: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    public fun onGeneralPageClick() {
        onGeneralPage.postValue(true)
    }
    public fun onRestaurantPageClick(){
        onRestaurantPage.postValue(true)
    }
    public fun onAddReviewClick(){
        onAddReview.postValue(true)
    }

}