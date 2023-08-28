package com.example.test.registration

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import br.com.sapereaude.maskedEditText.MaskedEditText
import com.example.test.R
import com.example.test.fragments.PopUpFragment

class PhoneOTPActivity : AppCompatActivity() {

    private lateinit var enterNum: MaskedEditText
    private lateinit var checkBox: CheckBox
    private lateinit var checkboxUncheckedDrawable: Drawable
    private lateinit var checkboxCheckedDrawable: Drawable
    private lateinit var sendOtpPageButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_otp)

        enterNum = findViewById(R.id.enter_your_number)
        sendOtpPageButton = findViewById(R.id.phone_number_next)

        checkBox = findViewById(R.id.agree_login)
        checkboxUncheckedDrawable = resources.getDrawable(R.drawable.ic_check, null)
        checkboxCheckedDrawable = resources.getDrawable(R.drawable.ic_checked, null)

        checkBox.buttonDrawable = checkboxUncheckedDrawable
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox.buttonDrawable = checkboxCheckedDrawable
            } else {
                checkBox.buttonDrawable = checkboxUncheckedDrawable
            }
        }

        sendOtpPageButton.setOnClickListener{
            val inputText = enterNum.rawText
            if (inputText.length == 9 && checkBox.isChecked) {
                val phoneNumber = "+380$inputText"
                val change = Intent(this@PhoneOTPActivity, OTPActivity::class.java)
                change.putExtra("phone", phoneNumber)
                startActivity(change)
            } else {
                if (inputText.isEmpty() || inputText.length < 9 || checkBox.isChecked) {
                    val errorMessage = "Невірно введений номер телефону. Повторіть спробу"
                    val errorPopUp = PopUpFragment.newInstance(errorMessage)
                    errorPopUp.show(supportFragmentManager, "errorPopUp")
                }
                if(inputText.length == 9 && !checkBox.isChecked){
                    val errorMessage = "Прочитайте, будь ласка,\n" +
                            "Правила і умови роботи компанії, поставте галочку, щоб продовжити реєстрацію"
                    val errorPopUp = PopUpFragment.newInstance(errorMessage)
                    errorPopUp.show(supportFragmentManager, "errorPopUp")
                }
            }
        }
    }
    private fun showPop() {
        val title = "Прочитайте, будь ласка,\nПравила і умови роботи компанії, поставте галочку, щоб продовжити реєстрацію"
        val showPopUp = PopUpFragment(title)
        showPopUp.show(supportFragmentManager, "showPopUp")
    }
}
