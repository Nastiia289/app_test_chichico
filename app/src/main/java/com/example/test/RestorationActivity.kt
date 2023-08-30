package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.test.databinding.ActivityRestorationBinding

class RestorationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestorationBinding
    private lateinit var imageSlider: ImageSlider
    private lateinit var exit: ImageView
    private lateinit var toMenu: AppCompatButton
    private lateinit var delivery: AppCompatButton
    private lateinit var reserve: AppCompatButton
    private lateinit var restaurantLocation: LinearLayout
    private lateinit var phoneNumb: LinearLayout
    private lateinit var addReview: TextView
    private lateinit var viewModel: RestorationActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestorationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageSlider = binding.ImageSlider
        exit = binding.imageView
        toMenu = binding.showMenu
        delivery = binding.delivery
        reserve = binding.reserve
        restaurantLocation = binding.location
        phoneNumb = binding.phoneNumb
        addReview = binding.addReview

        viewModel = ViewModelProvider(this)[RestorationActivityViewModel::class.java]

        val slideModels = arrayListOf<SlideModel>()
        slideModels.add(SlideModel(R.drawable.chichico_back, ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.chichico_back, ScaleTypes.FIT))

        imageSlider.setImageList(slideModels)

        exit.setOnClickListener {
            viewModel.onGeneralPageClick()
        }

        restaurantLocation.setOnClickListener{
            viewModel.onRestaurantPageClick()
        }

        addReview.setOnClickListener{
            viewModel.onAddReviewClick()
        }

        viewModel.onGeneralPage.observe(this){
            val intent = Intent(this@RestorationActivity, GeneralPageActivity::class.java)
            startActivity(intent)
        }
        viewModel.onRestaurantPage.observe(this) {
            val intent = Intent(this@RestorationActivity, ChangLocation::class.java)
            startActivity(intent)
        }
        viewModel.onAddReview.observe(this){
            val intent = Intent(this@RestorationActivity, AddReviewActivity::class.java)
            startActivity(intent)
        }
    }
}
