package com.example.test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.test.registration.OTPActivity
import com.example.test.R


class ResendCodePopup(
    private val phoneNumber: String,
    private val closeButtonId: Int,
    private val resendButtonId: Int
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
            val otpActivity = activity as OTPActivity
            otpActivity.isClosePopUpClicked = true
            dismiss()
        }

        resendCodeButton.setOnClickListener {
            if (!isResending) {
                val otpActivity = activity as OTPActivity

                if (!otpActivity.isTimerRunning) {
                    isResending = true
                    otpActivity.sendOtp(phoneNumber, true)
                    otpActivity.startResendTimer()
                    dismiss()
                }
            }
        }
    }
}
