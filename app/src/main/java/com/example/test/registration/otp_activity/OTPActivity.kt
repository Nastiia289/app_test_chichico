package com.example.test.registration.otp_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import com.example.test.fragments.PopUpFragment
import com.example.test.fragments.ResendCodePopup
import com.example.test.registration.name_activity.NameLoginActivity
import com.example.test.retrofit.ApiAdapter
import com.example.test.retrofit.ApiService
import com.example.test.retrofit.UserData
import com.example.test.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Timer
import java.util.TimerTask

class OTPActivity : AppCompatActivity() {

    private lateinit var viewModel: OTPActivityViewModel
    private lateinit var apiAdapter: ApiAdapter
    private lateinit var apiService: ApiService

    private lateinit var binding: ActivityOtpBinding
    private lateinit var otpInput: MaskedEditText
    private lateinit var nextButton: Button
    lateinit var resendCode: TextView

    var phoneNumber: String? = null
    private var verificationCode: Int? = null
    private val timeoutSeconds = 60L
    var resendCounter = 30
    var isTimerRunning = false

    var isClosePopUpClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[OTPActivityViewModel::class.java]
        setupApi()

        otpInput = binding.loginOtp
        nextButton = binding.loginSmsCodeNextPage
        resendCode = binding.resendOtpCode

        phoneNumber = intent.getStringExtra("phone")

        val request = UserData(
            phoneNumber = phoneNumber ?: "",
            enteredOtp = 0,
            name = "",
            code_otp = 0
        )

        lifecycleScope.launch {
            sendOtp(request, false)
        }

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

    private fun setupApi() {
        binding.loginOtp.apply {
            val emptyDataList: List<OTPActivity> = emptyList()
            apiAdapter = ApiAdapter(emptyDataList)
        }
        apiService = RetrofitInstance.api
    }

    suspend fun sendOtp(phoneNumber: UserData, isResend: Boolean) {
        val apiCall: Call<UserData> = if (isResend) {
            apiService.checkMobile(phoneNumber)
        } else {
            apiService.sendOtp(phoneNumber)
        }

        apiCall.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                val statusCode = response.code()

                when (statusCode) {
                    201 -> {
                        val responseData = response.body()
                        if (responseData != null) {
                            verificationCode = responseData.code_otp
                        }
                    }

                    204 -> {
                        // Обробка випадку, коли SMS не було відправлено
                    }

                    403 -> {
                        // Обробка випадку, коли доступ заборонено
                    }

                    else -> {
                        // Інші статуси відповіді
                        showPop()
                    }
                }

                // Лог результатів запиту
                Log.d("OTPActivity", "sendOtp request completed with status code: $statusCode")
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                // Обробка помилок
                Log.e("OTPActivity", "sendOtp request failed with exception: ${t.message}", t)
            }
        })
    }

    private fun handleSignIn() {
        val enteredOtp = otpInput.rawText.toString()
        if (enteredOtp.length == 6) {
            val enteredCode = enteredOtp.toIntOrNull()
            if (enteredCode != null && enteredCode == verificationCode) {
                val change = Intent(this@OTPActivity, NameLoginActivity::class.java)
                change.putExtra("phone", phoneNumber)
                startActivity(change)
            } else {
                showPop()
            }
        } else {
            showPop()
        }

        // Лог результату входу
        Log.d("OTPActivity", "handleSignIn completed")
    }



    private suspend fun handleResend() {
        if (isClosePopUpClicked) {
            isClosePopUpClicked = false
            isTimerRunning = false
        } else {
            resendPop(phoneNumber)
        }
    }

    private fun resendPop(phoneNumber: String?) {
        val title = "Смс з кодом було відправлено. Повторити відправку? "
        phoneNumber?.let {
            val closeButtonId = R.id.close_popup_button // Замініть це на ідентифікатор рядка з ресурсів
            val resendButtonId = R.id.popup_button // Замініть це на ідентифікатор рядка з ресурсів
            val request = UserData(
                phoneNumber = phoneNumber ?: "",
                enteredOtp = 0,
                name = "",
                code_otp = 0
            )
            ResendCodePopup(request, phoneNumber, closeButtonId, resendButtonId).show(supportFragmentManager, "ResendCodePopup")
        }
    }



    private fun showPop() {
        val title = "Невірно введено код з смс. Повторіть спробу"
        val showPopUp = PopUpFragment(title)
        showPopUp.show(supportFragmentManager, "showPopUp")
    }

    private var resendTimer: Timer? = null

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

        // Лог запуску таймера
        Log.d("OTPActivity", "Resend timer started")
    }

}
