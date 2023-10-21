package com.example.test.retrofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.registration.otp_activity.OTPActivity

class ApiAdapter(private val dataList: List<OTPActivity>) : RecyclerView.Adapter<ApiAdapter.ApiViewHolder>() {

    inner class ApiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiViewHolder {
        return ApiViewHolder(View(parent.context))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ApiViewHolder, position: Int) {
        val currentItem = dataList[position]
    }
}
