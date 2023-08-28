package com.example.test
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.test.registration.PhoneOTPActivity
import com.example.test.utils.FirebaseUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            if (FirebaseUtil.isLoggedIn()){
                val intent = Intent(this@MainActivity, GeneralPage::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this@MainActivity, PhoneOTPActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 1000)
    }
}
