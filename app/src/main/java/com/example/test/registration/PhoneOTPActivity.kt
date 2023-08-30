package com.example.test.registration

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProvider
import br.com.sapereaude.maskedEditText.MaskedEditText
import com.example.test.R
import com.example.test.databinding.ActivityPhoneOtpBinding
import com.example.test.fragments.PopUpFragment

class PhoneOTPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneOtpBinding
    private lateinit var enterNum: MaskedEditText
    private lateinit var checkBox: CheckBox
    private lateinit var checkboxUncheckedDrawable: Drawable
    private lateinit var checkboxCheckedDrawable: Drawable
    private lateinit var sendOtpPageButton: Button
    private lateinit var viewModel: PhoneOTPActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[PhoneOTPActivityViewModel::class.java]

        enterNum = binding.enterYourNumber
        sendOtpPageButton = binding.phoneNumberNext

        checkBox = binding.agreeLogin
        checkboxUncheckedDrawable = resources.getDrawable(R.drawable.ic_check, null)
        checkboxCheckedDrawable = resources.getDrawable(R.drawable.ic_checked, null)

        checkBox.buttonDrawable = checkboxUncheckedDrawable
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            checkBox.buttonDrawable = if (isChecked) checkboxCheckedDrawable else checkboxUncheckedDrawable
        }

        sendOtpPageButton.setOnClickListener{
            val inputText = enterNum.rawText
            val isCheckBoxChecked = checkBox.isChecked

            viewModel.validateAndNavigate(inputText, isCheckBoxChecked)
        }

        viewModel.onValidationComplete.observe(this) { validationResult ->
            if (validationResult.first) {
                val phoneNumber = validationResult.second
                val change = Intent(this@PhoneOTPActivity, OTPActivity::class.java)
                change.putExtra("phone", phoneNumber)
                startActivity(change)
            } else {
                val errorMessage = validationResult.second
                val errorPopUp = PopUpFragment.newInstance(errorMessage)
                errorPopUp.show(supportFragmentManager, "errorPopUp")
            }
        }
    }
}