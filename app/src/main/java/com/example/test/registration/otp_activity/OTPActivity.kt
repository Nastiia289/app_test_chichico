package com.example.test.registration.otp_activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import br.com.sapereaude.maskedEditText.MaskedEditText
import com.example.test.R
import com.example.test.databinding.ActivityOtpBinding
import com.example.test.fragments.ResendCodePopup
import com.example.test.registration.name_activity.NameLoginActivity
import com.example.test.retrofit.ApiService
import com.example.test.retrofit.UserData
import com.example.test.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Timer
import java.util.TimerTask
import androidx.appcompat.app.AppCompatActivity
import com.example.test.fragments.PopUpFragment

class OTPActivity : AppCompatActivity() {

    private lateinit var viewModel: OTPActivityViewModel
    private lateinit var apiService: ApiService

    private lateinit var binding: ActivityOtpBinding
    private lateinit var otpInput: MaskedEditText
    private lateinit var nextButton: Button
    private var receivedOTP: Int? = null
    lateinit var resendCode: TextView

    var phoneNumber: String? = null
    private var verificationCode: Int? = null
    var resendCounter = 30
    var isTimerRunning = false
    var isClosePopUpClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[OTPActivityViewModel::class.java]
        apiService = RetrofitInstance.api

        otpInput = binding.loginOtp
        nextButton = binding.loginSmsCodeNextPage
        resendCode = binding.resendOtpCode

        phoneNumber = intent.getStringExtra("phone")

        nextButton.setOnClickListener {
            viewModel.signInButtonClick()
        }

        resendCode.text = "Не приходить СМС ?"

        resendCode.setOnClickListener {
            viewModel.resendButtonClick()
        }

        viewModel.onSignInClick.observe(this) { signInClick ->
            if (signInClick) {
                handleSignIn()
            }
        }

        viewModel.onResendClick.observe(this) { resendClick ->
            if (resendClick) {
                lifecycleScope.launch {
                    handleResend()
                }
            }
        }
    }

    private fun resendPop(phoneNumber: String?) {
        val title = "Смс з кодом було відправлено. Повторити відправку? "
        phoneNumber?.let {
            val closeButtonId = R.id.close_popup_button
            val resendButtonId = R.id.popup_button
            ResendCodePopup(this, closeButtonId, resendButtonId).show(
                supportFragmentManager,
                "com.example.test.fragments.ResendCodePopup"
            )
            initiateMobileCheck(phoneNumber)
        }
    }

    private fun handleResend() {
        if (isClosePopUpClicked) {
            isClosePopUpClicked = false
            isTimerRunning = false
        } else {
            resendPop(phoneNumber)
            isTimerRunning = true
            startResendTimer()
        }
    }

    private fun showPop(message: String) {
        val showPopUp = PopUpFragment(message)
        showPopUp.show(supportFragmentManager, "showPopUp")
    }

    private suspend fun sendMobileCheckRequest(phoneNumber: String) {
        try {
            if (isClosePopUpClicked) {
                return
            }
            Log.d("OTPActivity", "Checking phone number: $phoneNumber")

            val response: Response<List<UserData>> = apiService.checkMobile(phoneNumber)

            if (response.isSuccessful) {
                val responseData = response.body()
                Log.d("OTPActivity", "Response body: $responseData")

                if (response.code() == 201) {
                    if (!responseData.isNullOrEmpty()) {
                        val userData = responseData[0]
                        verificationCode = userData.code_otp
                        receivedOTP = userData.code_otp
                        Log.d("OTPActivity", "Received verification code: $verificationCode")

                        val change = Intent(this@OTPActivity, NameLoginActivity::class.java)
                        change.putExtra("phone", phoneNumber)
                        startActivity(change)
                    } else {
                        Log.e("OTPActivity", "Error: Server response is empty. Response code: ${response.code()}")
                        isClosePopUpClicked = true
                        showPop("Упс, щось пішло не так. Server response is empty.")
                    }
                } else {
                    Log.e("OTPActivity", "Error: HTTP ${response.code()}. Response body: $responseData")
                    isClosePopUpClicked = true
                    showPop("Упс, щось пішло не так. HTTP ${response.code()}")
                }
            } else {
                Log.e("OTPActivity", "Error: HTTP ${response.code()}. Response body: ${response.errorBody()?.string()}")
                isClosePopUpClicked = true
                showPop("Упс, щось пішло не так. HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("OTPActivity", "Error: ${e.message}")
            isClosePopUpClicked = true
            showPop("Сталася помилка: ${e.message}")
        }
    }

    fun initiateMobileCheck(phoneNumber: String?) {
        if (isClosePopUpClicked) {
            isClosePopUpClicked = false
            isTimerRunning = false
        } else {
            lifecycleScope.launch {
                if (phoneNumber != null) {
                    sendMobileCheckRequest(phoneNumber)
                }
            }
        }
    }

    private fun handleSignIn() {
        val enteredOtp = otpInput.rawText.toString()
        if (enteredOtp.length == 4) {
            val enteredCode = enteredOtp.toIntOrNull()
            Log.d("OTPActivity", "Entered code: $enteredCode, Verification code: $verificationCode")

            if (enteredCode != null && enteredCode == verificationCode) {
                val change = Intent(this@OTPActivity, NameLoginActivity::class.java)
                change.putExtra("phone", phoneNumber)
                startActivity(change)
            } else {
                showPop("Невірно введений OTP код")
            }
        } else {
            showPop("OTP повинен містити 4 цифри")
        }
    }


    var resendTimer: Timer? = null

    fun startResendTimer() {
        resendCode.isEnabled = false
        resendCode.text = "Не приходить СМС ?"

        resendTimer?.cancel()

        val handler = Handler(Looper.getMainLooper())
        resendTimer = Timer()
        resendTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    if (resendCounter > 0) {
                        resendCode.text = "Нове СМС буде надіслано через $resendCounter секунд"
                        resendCounter--
                    } else {
                        resendCounter = 30
                        resendTimer?.cancel()
                        isTimerRunning = false
                        resendCode.isEnabled = true
                        resendCode.text = "Не приходить СМС ?"
                    }
                }
            }
        }, 1000, 1000)

        Log.d("OTPActivity", "Resend timer started")
    }
}
