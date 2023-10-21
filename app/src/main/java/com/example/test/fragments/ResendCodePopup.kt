package com.example.test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.test.registration.otp_activity.OTPActivity
import com.example.test.R
import com.example.test.retrofit.UserData
import kotlinx.coroutines.launch

class ResendCodePopup(
    private val userData: UserData,
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
            val otpActivity = activity as? OTPActivity
            otpActivity?.isClosePopUpClicked = true
            dismiss()
        }

        resendCodeButton.setOnClickListener {
            if (!isResending) {
                val otpActivity = activity as? OTPActivity

                if (otpActivity != null && !otpActivity.isTimerRunning) {
                    isResending = true

                    // Create a UserData object
                    val userData = UserData(
                        phoneNumber = phoneNumber,
                        enteredOtp = 0,
                        name = "",
                        code_otp = 0
                    )

                    otpActivity.lifecycleScope.launch {
                        otpActivity.sendOtp(userData, true)
                        otpActivity.startResendTimer()
                    }
                    dismiss()
                }
            }
        }
    }

}
