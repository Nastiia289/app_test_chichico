package com.example.test.retrofit

data class UserData(
    val phoneNumber: String,
    val enteredOtp: Int?,
    val name: String?,
    val code_otp: Int?
) {
    constructor(phoneNumber: String, code_otp: Int?) : this(phoneNumber, null, null, code_otp)
}
