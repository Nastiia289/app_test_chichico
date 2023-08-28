package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

class Restoration : AppCompatActivity() {

    private lateinit var imageSlider: ImageSlider
    private lateinit var exit: ImageView
    private lateinit var toMenu: AppCompatButton
    private lateinit var delivery: AppCompatButton
    private lateinit var reserve: AppCompatButton
    private lateinit var restaurantLocation: LinearLayout
    private lateinit var phoneNumb: LinearLayout
    private lateinit var addReview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restoration)

        imageSlider = findViewById(R.id.ImageSlider)
        exit = findViewById(R.id.imageView)
        toMenu = findViewById(R.id.show_menu)
        delivery = findViewById(R.id.delivery)
        reserve = findViewById(R.id.reserve)
        restaurantLocation = findViewById(R.id.location)
        phoneNumb = findViewById(R.id.phone_numb)
        addReview = findViewById(R.id.add_review)

        val slideModels = arrayListOf<SlideModel>()
        slideModels.add(SlideModel(R.drawable.chichico_back, ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.chichico_back, ScaleTypes.FIT))

        imageSlider.setImageList(slideModels)

        exit.setOnClickListener{
            val intent = Intent(this@Restoration, GeneralPage::class.java)
            startActivity(intent)
        }
        restaurantLocation.setOnClickListener {
            val intent = Intent(this@Restoration, ChangeLocation::class.java)
            startActivity(intent)
        }
        addReview.setOnClickListener{
            val intent = Intent(this@Restoration, AddReview::class.java)
            startActivity(intent)
        }
    }
}
