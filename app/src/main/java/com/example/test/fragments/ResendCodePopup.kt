package com.example.test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.test.R
import com.example.test.registration.otp_activity.OTPActivity
import com.example.test.retrofit.ApiService

class ResendCodePopup(
    private val otpActivity: OTPActivity,
    private val closeButtonId: Int,
    private val resendButtonId: Int,
    private val phoneNumber: String?,
    private val apiService: ApiService
) : DialogFragment() {

    private var isResending = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.resendpopup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closePopUp = view.findViewById<Button>(closeButtonId)
        val resendCodeButton = view.findViewById<Button>(resendButtonId)

        closePopUp.setOnClickListener {
            otpActivity.isClosePopUpClicked = true
            this.dismiss()
        }

        resendCodeButton.setOnClickListener {
            if (!isResending) {
                if (!otpActivity.isTimerRunning) {
                    isResending = true
                    otpActivity.initiateMobileCheck(otpActivity.phoneNumber)
                    otpActivity.startResendTimer()
                    this@ResendCodePopup.dismiss()
                }
            }
        }
    }
}
