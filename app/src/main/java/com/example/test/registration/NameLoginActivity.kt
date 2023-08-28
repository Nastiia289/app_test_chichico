package com.example.test.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.test.GeneralPage
import com.example.test.R
import com.example.test.fragments.PopUpFragment
import com.example.test.model.UserModel
import com.example.test.utils.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.Timestamp

class NameLoginActivity : AppCompatActivity() {
    private lateinit var toNextPage: Button
    private lateinit var userName: EditText
    private lateinit var userModel: UserModel

    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        toNextPage = findViewById(R.id.from_name_page)
        userName = findViewById(R.id.user_name)

        phoneNumber = intent.getStringExtra("phone")
        userModel = UserModel(phoneNumber, "", Timestamp.now())

        getUserName()

        toNextPage.setOnClickListener {
            setUserName()
        }
    }

    private fun setUserName() {
        val username = userName.text.toString()
        if (username.isEmpty() || username.length < 3) {
           showPop()
        }
        userModel.username = username
        userModel.phone = phoneNumber // Додайте цей рядок для запису phoneNumber
        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val change = Intent(this@NameLoginActivity, GeneralPage::class.java)
                change.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(change)
            }
        }
    }

    private fun getUserName() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document: DocumentSnapshot? = task.result
                if (document != null) {
                    val userModel: UserModel? = document.toObject(UserModel::class.java)
                    if (userModel != null) {
                        val name = userModel.username
                        userName.setText(name)
                    }
                }
            }
        }
    }
    private fun showPop() {
        val title = "Поле ім’я обов’язкове для заповнення"
        val showPopUp = PopUpFragment(title)
        showPopUp.show(supportFragmentManager, "showPopUp")
    }
}
