package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

class GeneralPage : AppCompatActivity() {
    private lateinit var next : ImageView
    private lateinit var toLocation: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_page)

        next = findViewById(R.id.myImageView)
        toLocation = findViewById(R.id.to_location)

        next.setOnClickListener{
            val intent = Intent(this@GeneralPage, Restoration::class.java)
            startActivity(intent)
        }
        toLocation.setOnClickListener{
            val intent = Intent(this@GeneralPage, ChangeLocation::class.java)
            startActivity(intent)
        }
    }
}