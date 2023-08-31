package com.example.test.registration.name_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.test.general.general_page_activity.GeneralPageActivity
import com.example.test.databinding.ActivityNameBinding
import com.example.test.fragments.PopUpFragment
import com.example.test.model.UserModel
import com.example.test.utils.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.Timestamp

class NameLoginActivity : AppCompatActivity() {

    private lateinit var viewModel: NameLoginActivityViewModel
    private lateinit var binding: ActivityNameBinding
    private lateinit var toNextPage: Button
    private lateinit var userName: EditText
    private lateinit var userModel: UserModel

    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[NameLoginActivityViewModel::class.java]

        toNextPage = binding.fromNamePage
        userName = binding.userName

        phoneNumber = intent.getStringExtra("phone")
        userModel = UserModel(phoneNumber, "", Timestamp.now())

        getUserName()

        toNextPage.setOnClickListener {
            val username = userName.text.toString()
            viewModel.setNameAndNavigate(username, phoneNumber!!)
        }

        toNextPage.setOnClickListener{
            viewModel.onNameSetClick()
        }
        viewModel.onNameSet.observe(this) { nameSet ->
            if (nameSet) {
                val change = Intent(this@NameLoginActivity, GeneralPageActivity::class.java)
                change.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(change)
            } else {
                showPop()
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
