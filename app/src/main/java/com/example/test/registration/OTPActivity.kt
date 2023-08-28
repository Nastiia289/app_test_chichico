package com.example.test.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import br.com.sapereaude.maskedEditText.MaskedEditText
import com.example.test.R
import com.example.test.fragments.PopUpFragment
import com.example.test.fragments.ResendCodePopup
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {

    private lateinit var otpInput: MaskedEditText
    private lateinit var nextButton: Button
    lateinit var resendCode: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var mAuth: FirebaseAuth

    private var phoneNumber: String? = null
    private var verificationCode: String? = null
    private val timeoutSeconds = 60L
    var resendCounter = 30
    var isTimerRunning = false

    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken

    var isClosePopUpClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        auth = Firebase.auth
        mAuth = FirebaseAuth.getInstance()

        otpInput = findViewById(R.id.login_otp)
        nextButton = findViewById(R.id.login_sms_code_next_page)
        resendCode = findViewById(R.id.resend_otp_code)

        phoneNumber = intent.getStringExtra("phone")
        sendOtp(phoneNumber, false)

        nextButton.setOnClickListener {
            val enteredOtp = otpInput.rawText.toString()
            if (enteredOtp.length == 6) {
                val phoneAuthCredential = verificationCode?.let { it1 ->
                    PhoneAuthProvider.getCredential(
                        it1,
                        enteredOtp
                    )
                }
                if (phoneAuthCredential != null) {
                    signIn(phoneAuthCredential)
                } else {
                    showPop()
                }
            } else {
                showPop()
            }
        }
        resendCode.text = "Не приходить СМС ?"

        resendCode.setOnClickListener {
            if (isClosePopUpClicked) {
                isClosePopUpClicked = false
                isTimerRunning = false
                resendTimer?.cancel()
            } else {
                val closeButtonId = R.id.close_popup_button
                val resendButtonId = R.id.popup_button
                ResendCodePopup(phoneNumber!!, closeButtonId, resendButtonId)
                    .show(supportFragmentManager, "ResendCodePopup")
            }
        }
    }

    public fun sendOtp(phoneNumber: String?, isResend: Boolean) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signIn(phoneAuthCredential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    showPop()
                }

                override fun onCodeSent(s: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(s, p1)
                    verificationCode = s
                    resendingToken = p1
                }
            })

        if (isResend) {
            optionsBuilder.setForceResendingToken(resendingToken)
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    private fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val change = Intent(this@OTPActivity, NameLoginActivity::class.java)
                    change.putExtra("phone", phoneNumber)
                    startActivity(change)
                } else {
                  showPop()
                }
            }
    }

    private var resendTimer: Timer? = null // Змінна для таймера

    public fun startResendTimer() {
        resendCode.isEnabled = false
        resendCode.text = "Не приходить СМС ?"

        // Cancel the previous timer if it exists
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
    }

    private fun showPop() {
        val title = "Невірно введено код з смс. Повторіть спробу"
        val showPopUp = PopUpFragment(title)
        showPopUp.show(supportFragmentManager, "showPopUp")
    }

    private fun resendPop() {
        val title = "Смс з кодом було відправлено. Повторити відправку? "
        phoneNumber?.let {
            val closeButtonId = R.id.close_popup_button
            val resendButtonId = R.id.popup_button
            ResendCodePopup(it, closeButtonId, resendButtonId).show(supportFragmentManager, "ResendCodePopup")
        }
    }

}
