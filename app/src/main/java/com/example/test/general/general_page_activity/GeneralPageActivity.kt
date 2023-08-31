package com.example.test.general.general_page_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.example.test.general.restauration_activity.RestorationActivity
import com.example.test.general.chang_location_activity.ChangLocation
import com.example.test.databinding.ActivityGeneralPageBinding

class GeneralPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeneralPageBinding
    private lateinit var next: ImageView
    private lateinit var toLocation: LinearLayout
    private lateinit var viewModel: GeneralPageActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneralPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[GeneralPageActivityViewModel::class.java]

        next = binding.myImageView
        toLocation = binding.toLocation

        next.setOnClickListener {
            viewModel.signInButtonClick()
        }

        toLocation.setOnClickListener {
            viewModel.toLocationClick()
        }

        viewModel.onSignInClick.observe(this) {
            val intent = Intent(this@GeneralPageActivity, RestorationActivity::class.java)
            startActivity(intent)
        }

        viewModel.onToLocationClick.observe(this) {
            val intent = Intent(this@GeneralPageActivity, ChangLocation::class.java)
            startActivity(intent)
        }
    }
}
